package com.esp.ip.service;

import com.esp.ip.service.exception.InvalidDatabaseException;
import com.esp.ip.service.io.Reader;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author edward
 * @date 2019/1/7
 */
public class IPTest {

    @Test
    public void testIp(){
        try {
            Reader reader = new Reader("dat/ip.dat");
            System.out.println(reader.isIPv4());
            System.out.println(reader.isIPv6());
            System.out.println(LocalDateTime.ofEpochSecond(reader.getBuildUTCTime(),0, ZoneOffset.ofHours(8)));
            System.out.println(reader.getSupportLanguages());
            System.out.println(reader.getCity("49.85.255.39"));
        } catch (InvalidDatabaseException e) {
            e.printStackTrace();
        }
    }
}
