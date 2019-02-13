package com.esp.ip.service.controller;

import com.esp.ip.service.dto.BaseStationDto;
import com.esp.ip.service.dto.CityDto;
import com.esp.ip.service.dto.DistrictDto;
import com.esp.ip.service.dto.IDCDto;
import com.esp.ip.service.io.Reader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author edward
 * @date 2019/1/7
 */
@RestController
@RequestMapping("/api")
public class IPController {

    @Autowired
    private Reader reader;

    @RequestMapping(value = "/city/{ip:.+}",method = RequestMethod.GET)
    public CityDto getCity(@PathVariable String ip) {
        return reader.getCity(ip);
    }

    @RequestMapping(value = "/baseStation/{ip:.+}",method = RequestMethod.GET)
    public BaseStationDto getBaseStation(@PathVariable String ip) {
        return reader.getBaseStation(ip);
    }

    @RequestMapping(value = "/district/{ip:.+}",method = RequestMethod.GET)
    public DistrictDto getDistrict(@PathVariable String ip) {
        return reader.getDistrict(ip);
    }

    @RequestMapping(value = "/idc/{ip:.+}",method = RequestMethod.GET)
    public IDCDto getIDC(@PathVariable String ip){
        return reader.getIDC(ip);
    }
}
