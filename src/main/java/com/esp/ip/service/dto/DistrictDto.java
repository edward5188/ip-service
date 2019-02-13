package com.esp.ip.service.dto;

import com.alibaba.fastjson.JSONObject;

public class DistrictDto extends BaseDto{

    private final String countryName;
    private final String regionName;
    private final String cityName;
    private final String districtName;
    private final String chinaAdminCode;
    private final String coveringRadius;
    private final String latitude;
    private final String longitude;

    public DistrictDto(String[] data) {
        int idx = 0;
        this.countryName = get(data,idx);
        this.regionName = get(data,++idx);
        this.cityName = get(data,++idx);
        this.districtName = get(data,++idx);
        this.chinaAdminCode = get(data,++idx);
        this.coveringRadius = get(data,++idx);
        this.latitude = get(data,++idx);
        this.longitude = get(data,++idx);
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

    public String getDistrictName() {
        return districtName;
    }

    public String getChinaAdminCode() {
        return chinaAdminCode;
    }

    public String getCoveringRadius() {
        return coveringRadius;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
