package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.controller;

import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service.ISysAgHouseService2;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;
import java.util.Set;

/**
 *
 * @Author: qinyg
 * @Date: 2020/12/15 13:45
 * @tips:
 */
@RestController
@RequestMapping("/agsupport/bimResource/buildingLib")
@Api(value = "房屋相关接口", description = "预览房屋相关接口")
@ApiIgnore
public class AgHouseViewV3Controller {
    private static final Logger logger = LoggerFactory.getLogger(AgHouseViewV3Controller.class);

    @Autowired
    private ISysAgHouseService2 resourceService;

    /**
     * 因为要匹配动态参数，目前暂时支持多层/preview/1/2/3/4/5.../20/...
     */
    @ApiOperation(value = "预览房屋信息",notes = "预览房屋信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "map", value = "预览房屋信息的路径如：/source/图片分类/timg.jpg", dataType = "String"),
    })
    @GetMapping(value ={
            "/preview/{s1}",
            "/preview/{s1}/{s2}",
            "/preview/{s1}/{s2}/{s3}",
            "/preview/{s1}/{s2}/{s3}/{s4}",
            "/preview/{s1}/{s2}/{s3}/{s4}/{s5}",
            "/preview/{s1}/{s2}/{s3}/{s4}/{s5}/{s6}",
            "/preview/{s1}/{s2}/{s3}/{s4}/{s5}/{s6}/{s7}",
            "/preview/{s1}/{s2}/{s3}/{s4}/{s5}/{s6}/{s7}/{s8}",
            "/preview/{s1}/{s2}/{s3}/{s4}/{s5}/{s6}/{s7}/{s8}/{s9}",
            "/preview/{s1}/{s2}/{s3}/{s4}/{s5}/{s6}/{s7}/{s8}/{s9}/{s10}",
            "/preview/{s1}/{s2}/{s3}/{s4}/{s5}/{s6}/{s7}/{s8}/{s9}/{s10}/{s11}",
            "/preview/{s1}/{s2}/{s3}/{s4}/{s5}/{s6}/{s7}/{s8}/{s9}/{s10}/{s11}/{s12}",
            "/preview/{s1}/{s2}/{s3}/{s4}/{s5}/{s6}/{s7}/{s8}/{s9}/{s10}/{s11}/{s12}/{s13}",
            "/preview/{s1}/{s2}/{s3}/{s4}/{s5}/{s6}/{s7}/{s8}/{s9}/{s10}/{s11}/{s12}/{s13}/{s14}",
            "/preview/{s1}/{s2}/{s3}/{s4}/{s5}/{s6}/{s7}/{s8}/{s9}/{s10}/{s11}/{s12}/{s13}/{s14}/{s15}",
            "/preview/{s1}/{s2}/{s3}/{s4}/{s5}/{s6}/{s7}/{s8}/{s9}/{s10}/{s11}/{s12}/{s13}/{s14}/{s15}/{s16}",
            "/preview/{s1}/{s2}/{s3}/{s4}/{s5}/{s6}/{s7}/{s8}/{s9}/{s10}/{s11}/{s12}/{s13}/{s14}/{s15}/{s16}/{s17}",
            "/preview/{s1}/{s2}/{s3}/{s4}/{s5}/{s6}/{s7}/{s8}/{s9}/{s10}/{s11}/{s12}/{s13}/{s14}/{s15}/{s16}/{s17}/{s18}",
            "/preview/{s1}/{s2}/{s3}/{s4}/{s5}/{s6}/{s7}/{s8}/{s9}/{s10}/{s11}/{s12}/{s13}/{s14}/{s15}/{s16}/{s17}/{s18}/{s19}",
            "/preview/{s1}/{s2}/{s3}/{s4}/{s5}/{s6}/{s7}/{s8}/{s9}/{s10}/{s11}/{s12}/{s13}/{s14}/{s15}/{s16}/{s17}/{s18}/{s19}/{s20}",
            "/preview/{s1}/{s2}/{s3}/{s4}/{s5}/{s6}/{s7}/{s8}/{s9}/{s10}/{s11}/{s12}/{s13}/{s14}/{s15}/{s16}/{s17}/{s18}/{s19}/{s20}/{s21}",
            "/preview/{s1}/{s2}/{s3}/{s4}/{s5}/{s6}/{s7}/{s8}/{s9}/{s10}/{s11}/{s12}/{s13}/{s14}/{s15}/{s16}/{s17}/{s18}/{s19}/{s20}/{s21}/{s22}",
            "/preview/{s1}/{s2}/{s3}/{s4}/{s5}/{s6}/{s7}/{s8}/{s9}/{s10}/{s11}/{s12}/{s13}/{s14}/{s15}/{s16}/{s17}/{s18}/{s19}/{s20}/{s21}/{s22}/{s23}",
            "/preview/{s1}/{s2}/{s3}/{s4}/{s5}/{s6}/{s7}/{s8}/{s9}/{s10}/{s11}/{s12}/{s13}/{s14}/{s15}/{s16}/{s17}/{s18}/{s19}/{s20}/{s21}/{s22}/{s23}/{s24}",
            "/preview/{s1}/{s2}/{s3}/{s4}/{s5}/{s6}/{s7}/{s8}/{s9}/{s10}/{s11}/{s12}/{s13}/{s14}/{s15}/{s16}/{s17}/{s18}/{s19}/{s20}/{s21}/{s22}/{s23}/{s24}/{s25}",
            "/preview/{s1}/{s2}/{s3}/{s4}/{s5}/{s6}/{s7}/{s8}/{s9}/{s10}/{s11}/{s12}/{s13}/{s14}/{s15}/{s16}/{s17}/{s18}/{s19}/{s20}/{s21}/{s22}/{s23}/{s24}/{s25}/{s26}",
            "/preview/{s1}/{s2}/{s3}/{s4}/{s5}/{s6}/{s7}/{s8}/{s9}/{s10}/{s11}/{s12}/{s13}/{s14}/{s15}/{s16}/{s17}/{s18}/{s19}/{s20}/{s21}/{s22}/{s23}/{s24}/{s25}/{s26}/{s27}",
            "/preview/{s1}/{s2}/{s3}/{s4}/{s5}/{s6}/{s7}/{s8}/{s9}/{s10}/{s11}/{s12}/{s13}/{s14}/{s15}/{s16}/{s17}/{s18}/{s19}/{s20}/{s21}/{s22}/{s23}/{s24}/{s25}/{s26}/{s27}/{s28}",
            "/preview/{s1}/{s2}/{s3}/{s4}/{s5}/{s6}/{s7}/{s8}/{s9}/{s10}/{s11}/{s12}/{s13}/{s14}/{s15}/{s16}/{s17}/{s18}/{s19}/{s20}/{s21}/{s22}/{s23}/{s24}/{s25}/{s26}/{s27}/{s28}/{s29}",
            "/preview/{s1}/{s2}/{s3}/{s4}/{s5}/{s6}/{s7}/{s8}/{s9}/{s10}/{s11}/{s12}/{s13}/{s14}/{s15}/{s16}/{s17}/{s18}/{s19}/{s20}/{s21}/{s22}/{s23}/{s24}/{s25}/{s26}/{s27}/{s28}/{s29}/{s30}",
    })
    public void  preview(@PathVariable Map<String, String> map, HttpServletResponse response){
        String path = getResourceViewPath(map);
        if(path == null){
            return;
        }
        ServletOutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            String baseFile = resourceService.getBaseFilePath();
            inputStream = new FileInputStream(baseFile + File.separator + path);
            //将inputStream装成byte[]
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            if (bytes != null) {
                response.addHeader("Content-Length", bytes.length + "");
                String s = null;
                try{
                    s = new String(path.substring(path.lastIndexOf("/"), path.length()).getBytes());
                }catch (Exception e){
                    try{
                        s = new String(path.substring(path.lastIndexOf("\\"), path.length()).getBytes());
                    }catch (Exception e1){

                    }
                }
                response.addHeader("Content-Disposition", "filename=" + s);
                //下面展示的是下载的弹框提示，如果要打开，就不需要设置
//        response.setContentType("application/octet-stream");
                outputStream = response.getOutputStream();
                outputStream.write(bytes);
            }
        } catch (FileNotFoundException e) {
            logger.info(e.getMessage());
        } catch (IOException e) {
            logger.info(e.getMessage());
        }catch (Exception e) {
            logger.info(e.getMessage());
        }finally {
            try{
                if(inputStream != null){
                    inputStream.close();
                }
            }catch (IOException e){
                logger.info(e.getMessage());
            }
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                logger.info(e.getMessage());
            }
        }
    }

    public String getResourceViewPath(Map<String, String> map) {
        //获取map的路径
        if(map != null && map.size() > 0){
            String path = "";
            //遍历参数
            Set<Map.Entry<String, String>> entrySet = map.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                String value = entry.getValue();
                path += value + "/";
            }
            //去掉最后一个“/”
            return path.substring(0, path.length() - 1);
        }
        return null;
    }
}
