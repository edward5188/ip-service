package com.esp.ip.service.dto;

import com.alibaba.fastjson.JSONObject;

public class CityDto extends BaseDto {

    private final String countryName;
    private final String regionName;
    private final String cityName;
    private final String ownerDomain;
    private final String ispDomain;
    private final String latitude;
    private final String longitude;
    private final String timezone;
    private final String utcOffset;
    private final String chinaAdminCode;
    private final String iddCode;
    private final String countryCode;
    private final String continentCode;
    private final String idc;
    private final String baseStation;
    private final String countryCode3;
    private final String europeanUnion;
    private final String currencyCode;
    private final String currencyName;
    private final String anycast;

    public CityDto(String[] data) {
        int idx = 0;
        this.countryName = get(data,idx);
        this.regionName = get(data,++idx);
        this.cityName = get(data,++idx);
        this.ownerDomain = get(data,++idx);
        this.ispDomain = get(data,++idx);
        this.latitude = get(data,++idx);
        this.longitude = get(data,++idx);
        this.timezone = get(data,++idx);
        this.utcOffset = get(data,++idx);
        this.chinaAdminCode = get(data,++idx);
        this.iddCode = get(data,++idx);
        this.countryCode = get(data,++idx);
        this.continentCode = get(data,++idx);
        this.idc = get(data,++idx);
        this.baseStation = get(data,++idx);
        this.countryCode3 = get(data,++idx);
        this.europeanUnion = get(data,++idx);
        this.currencyCode = get(data,++idx);
        this.currencyName = get(data,++idx);
        this.anycast = get(data,++idx);
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

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getTimezone() {
        return timezone;
    }

    public String getUtcOffset() {
        return utcOffset;
    }

    public String getChinaAdminCode() {
        return chinaAdminCode;
    }

    public String getIddCode() {
        return iddCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getContinentCode() {
        return continentCode;
    }

    public String getIDC() {
        return idc;
    }

    public String getBaseStation() {
        return baseStation;
    }

    public String getCountryCode3() {
        return countryCode3;
    }

    public String getEuropeanUnion() {
        return europeanUnion;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public String getAnycast() {
        return anycast;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}