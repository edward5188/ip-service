package com.esp.ip.service.dto;

/**
 * @author edward
 * @date 2019/1/7
 */
public abstract class BaseDto {

    protected String get(String[] data, int idx){
        return data.length > idx? data[idx]:"";
    }
}
