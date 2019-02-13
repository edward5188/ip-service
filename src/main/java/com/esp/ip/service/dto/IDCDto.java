package com.esp.ip.service.dto;

import com.alibaba.fastjson.JSONObject;

public class IDCDto extends BaseDto{

    private final String countryName;
    private final String regionName;
    private final String cityName;
    private final String ownerDomain;
    private final String ispDomain;
    private final String idc;

    public IDCDto(String[] data) {
        int idx = 0;
        this.countryName = get(data,idx);
        this.regionName = get(data,++idx);
        this.cityName = get(data,++idx);
        this.ownerDomain = get(data,++idx);
        this.ispDomain = get(data,++idx);
        this.idc = get(data,++idx);
    }

    public String getCountryName() {
        return countryName;
    }

    public String getRegionName() {
        return regionName;
    }

    public String getCityName() {
        return cityName;
    }

    public String getOwnerDomain() {
        return ownerDomain;
    }

    public String getIspDomain() {
        return ispDomain;
    }

    public String getIDC() {
        return idc;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
