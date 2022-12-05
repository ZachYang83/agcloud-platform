package com.augurit.agcloud.agcom.agsupport.sc.counties.controller;

import com.augurit.agcloud.agcom.agsupport.domain.AgCounties;
import com.augurit.agcloud.agcom.agsupport.sc.counties.service.IAgCounties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by czh on 2018-05-29.
 */
@RestController
@RequestMapping("/agsupport/counties")
public class AgCountiesController {

    @Autowired
    public IAgCounties iAgCounties;

    @RequestMapping("/getCounties")
    public List<AgCounties> getCounties() throws Exception {
        return iAgCounties.getCounties();
    }

    @RequestMapping("/getTown")
    public List<AgCounties> getTown(String xzqhdm) throws Exception {
        return iAgCounties.getTown(xzqhdm);
    }

}
