package com.augurit.agcloud.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @RequestMapping("/index")
    public ModelAndView index(){
        return new ModelAndView("grxm/demo/index");
    }

    //other method.......
}
