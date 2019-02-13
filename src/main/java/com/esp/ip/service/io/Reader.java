package com.esp.ip.service.io;

import com.esp.ip.service.dto.BaseStationDto;
import com.esp.ip.service.dto.CityDto;
import com.esp.ip.service.dto.DistrictDto;
import com.esp.ip.service.dto.IDCDto;
import com.esp.ip.service.exception.IPFormatException;
import com.esp.ip.service.exception.InvalidDatabaseException;
import com.esp.ip.service.exception.NotFoundException;
import com.esp.ip.service.model.MetaData;
import sun.net.util.IPAddressUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.alibaba.fastjson.JSON.parseObject;
import static com.esp.ip.service.constant.Constant.DEFAULT_LANGUAGE;
import static java.util.Arrays.copyOfRange;

/**
 * @author edward
 * @date 2019/1/7
 */
public class Reader {

    private int fileSize;
    private int nodeCount;
    private MetaData meta;
    private byte[] data;
    private int v4offset;

    public Reader(String name) throws InvalidDatabaseException {
        init(readAllAsStream(name));
    }

    private byte[] readAllAsStream(String name){
        ByteArrayOutputStream output = null;
        FileInputStream input= null;
        try{
            File file = new File(name);
            if(!file.exists()){
                throw new NotFoundException("database file not found");
            }
            input =  new FileInputStream(name);
            output = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            for (int len;-1!=(len = input.read(buffer));) {
                output.write(buffer, 0, len);
            }
            return output.toByteArray();
        } catch (IOException e) {
            return null;
        } finally {
            if(null!=output){
                try {
                    output.close();
                } catch (IOException e) {
                   //ignore
                }
            }
            if(null!=input){
                try {
                    input.close();
                } catch (IOException e) {
                    //ignore
                }
            }
        }
    }

    public boolean reload(String name) {
        try {
            init(readAllAsStream(name));
            return true;
        } catch (InvalidDatabaseException e) {
            return false;
        }
    }

    private void init(byte[] data) throws InvalidDatabaseException {
        if(null == data){
            throw new InvalidDatabaseException("database file error");
        }
        fileSize = data.length;
        int metaLength = Long.valueOf(bytesToLong(data[0], data[1], data[2], data[3])).intValue();
        meta = parseObject(new String(copyOfRange(data, 4, metaLength + 4)), MetaData.class);
        if ((meta.getTotalSize() + metaLength + 4) != fileSize) {
            throw new InvalidDatabaseException("database file size error");
        }
        this.data = copyOfRange(data, metaLength + 4, fileSize);
        nodeCount = meta.getNodeCount();
        if (isIPv4()){
            int node = 0;
            for (int i = 0; i < 96 && node < nodeCount; i++) {
                node = readNode(node, i >= 80?1:0);
            }
            v4offset = node;
        }
    }

    public Map<String, String> findMap(String ip, String language) throws IPFormatException, InvalidDatabaseException {
        String[] data = find(ip, language);
        if (data != null) {
            String[] fields = getSupportFields();
            Map<String, String> m = new HashMap<>();
            for (int i = 0, len = data.length; i < len; i++) {
                m.put(fields[i], data[i]);
            }
            return m;
        }
        return null;
    }

    public String[] find(String ip, String language) throws IPFormatException, InvalidDatabaseException {
        try {
            Map<String, Integer> supportLanguages = meta.getLanguages();
            int off = supportLanguages.get(language);
            byte[] ipv;
            if (ip.indexOf(":") > 0) {
                ipv = IPAddressUtil.textToNumericFormatV6(ip);
                if (ipv == null) {
                    throw new IPFormatException("ipv6 format error");
                }else if (!isIPv6()){
                    throw new IPFormatException("no support ipv6");
                }
            } else if (ip.indexOf(".") > 0) {
                ipv = IPAddressUtil.textToNumericFormatV4(ip);
                if (ipv == null) {
                    throw new IPFormatException("ipv4 format error");
                }else if (!isIPv4()){
                    throw new IPFormatException("no support ipv4");
                }
            } else {
                throw new IPFormatException("ip format error");
            }
            int fieldSize = meta.getFields().length;
            return copyOfRange(resolve(findNode(ipv)).split("\t", fieldSize * supportLanguages.size()), off, off + fieldSize);
        } catch (NullPointerException | NotFoundException e) {
            return null;
        }
    }

    private int findNode(byte[] binary) throws NotFoundException {
        int node = 0;
        final int bit = binary.length * 8;
        if (bit == 32) {
            node = v4offset;
        }
        for (int i = 0; i < bit; i++) {
            if (node > nodeCount) {
                break;
            }
            node = readNode(node, 1 & ((0xFF & binary[i / 8]) >> 7 - (i % 8)));
        }
        if (node > nodeCount) {
            return node;
        }
        throw new NotFoundException("ip not found");
    }

    private String resolve(int node) throws InvalidDatabaseException {
        try {
            final int resoloved = node - nodeCount + nodeCount * 8;
            if (resoloved >= fileSize) {
                throw new InvalidDatabaseException("database resolve error");
            }
            byte b = 0;
            int size = Long.valueOf(bytesToLong(b,b,data[resoloved],data[resoloved+1])).intValue();
            if (data.length < (resoloved + 2 + size)) {
                throw new InvalidDatabaseException("database resolve error");
            }
            return new String(data, resoloved + 2, size, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new InvalidDatabaseException("database resolve error");
        }
    }

    private int readNode(int node, int index) {
        int off = node * 8 + index * 4;
        return Long.valueOf(bytesToLong(data[off],data[off+1],data[off+2], data[off+3])).intValue();
    }

    private long bytesToLong(byte a, byte b, byte c, byte d) {
        return int2long((a & 0xff) << 24 | (b & 0xff) << 16 | (c & 0xff) << 8 | d & 0xff);
    }

    private long int2long(int i) {
        long l = i & 0x7fffffffL;
        return i < 0?(l | 0x080000000L):l;
    }

    public CityDto getCity(String ip){
        try {
            String[] data = find(ip, DEFAULT_LANGUAGE);
            return null!= data? new CityDto(data):null;
        } catch (IPFormatException | InvalidDatabaseException e) {
            return null;
        }
    }

    public BaseStationDto getBaseStation(String ip){
        try {
            String[] data = find(ip, DEFAULT_LANGUAGE);
            return null!= data? new BaseStationDto(data):null;
        } catch (IPFormatException | InvalidDatabaseException e) {
            return null;
        }
    }

    public DistrictDto getDistrict(String ip){
        try {
            String[] data = find(ip, DEFAULT_LANGUAGE);
            return null!= data? new DistrictDto(data):null;
        } catch (IPFormatException | InvalidDatabaseException e) {
            return null;
        }
    }

    public IDCDto getIDC(String ip){
        try {
            String[] data = find(ip, DEFAULT_LANGUAGE);
            return null!= data? new IDCDto(data):null;
        } catch (IPFormatException | InvalidDatabaseException e) {
            return null;
        }
    }

    public MetaData getMeta() {
        return meta;
    }

    public byte[] getData() {
        return data;
    }

    public boolean isIPv4() {
        return (meta.getIpVersion() & 0x01) == 0x01;
    }

    public boolean isIPv6() {
        return (meta.getIpVersion() & 0x02) == 0x02;
    }

    public int getBuildUTCTime() {
        return meta.getBuild();
    }

    public String getPrintFields() {
        return Arrays.toString(meta.getFields());
    }

    public String[] getSupportFields() {
        return meta.getFields();
    }

    public String getSupportLanguages() {
        return meta.getLanguages().keySet().toString();
    }
}