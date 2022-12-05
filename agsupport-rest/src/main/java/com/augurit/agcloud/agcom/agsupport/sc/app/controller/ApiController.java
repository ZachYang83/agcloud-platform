package com.augurit.agcloud.agcom.agsupport.sc.app.controller;

import com.augurit.agcloud.framework.security.user.OpusLoginUser;
import com.augurit.agcloud.framework.util.JsonMapper;
import com.augurit.agcloud.framework.util.JsonUtils;
import com.augurit.agcloud.agcom.agsupport.sc.app.common.Result;
import com.augurit.agcloud.agcom.agsupport.sc.app.common.ResultCode;
import com.augurit.agcloud.agcom.agsupport.sc.app.common.VerifyToken;
import com.augurit.agcloud.agcom.agsupport.sc.app.service.AppProjectService;
import com.augurit.agcloud.agcom.agsupport.sc.dir.controller.form.DirTree;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Auther: zhangmingyang
 * @Date: 2018/9/29 16:24
 * @Description: 移动端接口
 */
@RestController
@RequestMapping("/agsupport/app")
public class ApiController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private AppProjectService appProjectService;

    @RequestMapping("getProjectList")
    public String getProjectList(HttpServletRequest request){
        /*try {
            OpusLoginUser opusLoginUser = SecurityContext.getOpusLoginUser();
            if(null == opusLoginUser){
                return JsonUtils.toJson(Result.failure(ResultCode.FAILURE));
            }
        }catch (Exception e){
            e.printStackTrace();
            return JsonUtils.toJson(Result.failure(ResultCode.FAILURE));
        }*/
        String token = request.getHeader("Authorization");
        Claims claims = VerifyToken.verifyToken(token);
        if (null == claims){
            return JsonUtils.toJson(Result.failure(ResultCode.TOKEN_INVALID));
        }
        OpusLoginUser opusLoginUser = VerifyToken.getOpusLoginUser(claims);
        List<Map> projectList = new ArrayList<>();
        try {
            projectList = appProjectService.getProjectList(opusLoginUser.getUser().getLoginName());
        }catch (Exception e){
            LOGGER.error("Query project error ! caused by: ",e);
            return JsonUtils.toJson(Result.failure(ResultCode.FAILURE));
        }
        Result result = Result.success(projectList);
        return JsonUtils.toJson(result);
    }

    @RequestMapping("getProjectLayersDirTree")
    public String getProjectLayersDirTree(String projectId, HttpServletRequest request){
        if (org.apache.commons.lang.StringUtils.isBlank(projectId)){
            LOGGER.error("projectId is null!");
            Result result = new Result(1,"专题id不能为空!");
            return JsonUtils.toJson(result);
        }
        String token = request.getHeader("Authorization");
        Claims claims = VerifyToken.verifyToken(token);
        if (null == claims){
            return JsonUtils.toJson(Result.failure(ResultCode.TOKEN_INVALID));
        }
        OpusLoginUser opusLoginUser = VerifyToken.getOpusLoginUser(claims);
        List<DirTree> dirTrees = new ArrayList<>();
        try {
            dirTrees = appProjectService.getTreeByProjectId(projectId, opusLoginUser.getUser().getUserId());
        }catch (Exception e){
            LOGGER.error("Query project error ! caused by: ",e);
            return JsonUtils.toJson(Result.failure(ResultCode.FAILURE));
        }
        Result result = Result.success(dirTrees);
        JsonMapper mapper = new JsonMapper(JsonInclude.Include.ALWAYS);//不去掉为空字段
        return mapper.toJson(result);
    }


    @RequestMapping("getLayersDirTree")
    public String getLayersDirTree(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        Claims claims = VerifyToken.verifyToken(token);
        if (null == claims){
            return JsonUtils.toJson(Result.failure(ResultCode.TOKEN_INVALID));
        }
        OpusLoginUser opusLoginUser = VerifyToken.getOpusLoginUser(claims);
        List<DirTree> layersDirTree = new ArrayList<>();
        try {
            layersDirTree = appProjectService.getLayersDirTree(opusLoginUser.getUser().getUserId());
        }catch (Exception e){
            LOGGER.error("Query LayersDirTree error ! caused by: ",e);
            return JsonUtils.toJson(Result.failure(ResultCode.FAILURE));
        }
        Result result = Result.success(layersDirTree);
        JsonMapper mapper = new JsonMapper(JsonInclude.Include.ALWAYS);//不去掉为空字段
        return mapper.toJson(result);
    }

}
