package com.augurit.agcloud.agcom.agsupport.sc.io.JsonStore.controller;

import com.alibaba.fastjson.JSON;
import com.augurit.agcloud.agcom.agsupport.common.util.UploadUtil;
import com.augurit.agcloud.agcom.agsupport.common.util.io.FileUtil;
import com.augurit.agcloud.agcom.agsupport.common.util.io.UploadFile;
import com.augurit.agcloud.agcom.agsupport.domain.AgJsonStore;
import com.augurit.agcloud.agcom.agsupport.sc.io.JsonStore.service.IAgJsonStore;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Json管理
 */
@RestController
@RequestMapping(value = "/agsupport/io/jsonstore")
@Api(value = "JSON管理",description = "JSON管理相关接口")
public class AgJsonStoreController {

    public static final String IMAGE_PATH = "ui-static/agcloud/agcom/ui/sc/io/jsonstore/image";


    @Autowired
    IAgJsonStore iAgJsonStore;


    /**
     * 样式配置页面
     *
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/index.html")
    public ModelAndView index(Model model) throws Exception {
        return new ModelAndView("agcloud/agcom/agsupport/io/jsonstore/index");
    }


    /**
     * 获取一条信息
     *
     * @param id
     * @return
     */
    @GetMapping("/getById")
    @ApiOperation(value = "获取一条信息")
    @ApiParam(value = "信息id")
    public ContentResultForm getById(@RequestParam(defaultValue = "") String id) {
        try {
            if (StringUtils.isBlank(id)) {
                return new ContentResultForm(false, "json信息ID不能为空!");
            }
            AgJsonStore jsonStore_out = iAgJsonStore.getById(id);
            if (null == jsonStore_out) {
                return new ContentResultForm(true, "暂无数据!");
            }
            return new ContentResultForm(true, jsonStore_out);
        } catch (Exception e) {
            return new ContentResultForm(false, e,e.getMessage());
        }
    }

    /**
     * 获取所有信息
     *
     * @return
     */
    @GetMapping("/getAll")
    @ApiOperation(value = "获取所有信息")
    public Object getAll() {
        try {
            List<AgJsonStore> agJsonStoreList = iAgJsonStore.getAll();
            if (null == agJsonStoreList || agJsonStoreList.size() == 0) {
                return new ContentResultForm(true, null,"暂无数据!");
            }
            return agJsonStoreList;
        } catch (Exception e) {
            return new ContentResultForm(false, e,e.getMessage());
        }
    }


    /**
     * 删除一条信息
     *
     * @param paramId
     * @return
     */
    @DeleteMapping("/deleteById")
    @ApiOperation(value = "删除一条信息")
    public ContentResultForm deleteById(@ApiParam(value = "信息id") String paramId) {
        try {
            if (StringUtils.isBlank(paramId)) {
                return new ContentResultForm(false, null, "json信息ID不能为空");
            }
            boolean falg = iAgJsonStore.deleteById(paramId);
            if (!falg) {
                return new ContentResultForm(false, null, "删除失败");
            }
            return new ContentResultForm(true, null, "删除成功");
        } catch (Exception e) {
            return new ContentResultForm(false, e,e.getMessage());
        }
    }


    /**
     * 删除更多信息
     *
     * @param paramIds
     * @return
     */
    @DeleteMapping("/deleteMany")
    @ApiOperation(value = "删除更多信息")
    public ContentResultForm deleteMany(@ApiParam("json信息ID") String paramIds) {
        try {
            if (StringUtils.isBlank(paramIds)) {
                return new ContentResultForm(false, null, "请求参数异常");
            }
            List<String> idsList = Arrays.asList(paramIds.split(","));
            boolean falg = iAgJsonStore.deleteMany(idsList);
            if (!falg) {
                return new ContentResultForm(false, null, "删除失败");
            }
            return new ContentResultForm(true, null, "删除成功");
        } catch (Exception e) {
            return new ContentResultForm(false, e,e.getMessage());
        }
    }

    /**
     * 添加一条信息
     *
     * @param agJsonStore
     * @return
     */
    @PostMapping("/save")
    @ApiOperation(value = "添加一条信息")
    public ContentResultForm save(@ApiParam(value = "json信息") AgJsonStore agJsonStore, HttpServletRequest request) {
        try {
            agJsonStore.setId(UUID.randomUUID().toString());
            agJsonStore.setCreateTime(new Date());
            String url = request.getRequestURL().toString().substring(0, request.getRequestURL().toString().lastIndexOf("/"));
            agJsonStore.setUrl(url + "/getJsonById/" + agJsonStore.getId());
            boolean falg = iAgJsonStore.save(agJsonStore);
            if (!falg) {
                return new ContentResultForm(false, null, "添加失败");
            }
            return new ContentResultForm(true, agJsonStore.getId(), "添加成功");
        } catch (Exception e) {
            return new ContentResultForm(false, e,e.getMessage());
        }
    }

    /**
     * 编辑一条信息
     *
     * @param agJsonStore
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "编辑一条信息")
    public ContentResultForm update(@ApiParam(value = "json信息") AgJsonStore agJsonStore) {
        try {
            agJsonStore.setCreateTime(null);
            boolean falg = iAgJsonStore.update(agJsonStore);
            if (!falg) {
                return new ContentResultForm(false, null, "编辑失败");
            }
            return new ContentResultForm(true, agJsonStore.getId(), "编辑成功");
        } catch (Exception e) {
            return new ContentResultForm(false, e,e.getMessage());
        }
    }

    @ApiOperation(
            value = "分页获取数据",
            notes = "分分页获取数据接口"
    )
    @ApiImplicitParams({@ApiImplicitParam(
            name = "agJsonStore",
            required = false,
            value = "样式对象信息",
            dataType = "AgStyle"
    ), @ApiImplicitParam(
            name = "page",
            required = true,
            value = "分页参数:/agsupport/agJsonStore/getByDomainAndUsage?page=1&rows=10&name=",
            dataType = "Page"
    )})
    @RequestMapping(
            value = {"/getByDomainAndUsage"},
            method = {RequestMethod.GET}
    )
    public ContentResultForm getByDomainAndUsage(AgJsonStore agJsonStore, Page page) throws Exception {
        PageInfo<AgJsonStore> pageInfo = iAgJsonStore.getByDomainAndUsage(agJsonStore, page);
        EasyuiPageInfo<AgJsonStore> result = PageHelper.toEasyuiPageInfo(pageInfo);
        return new ContentResultForm(true, result);
    }


    /**
     * 获取单条json
     *
     * @param id
     * @return
     */
    @GetMapping("/getJsonById/{id}")
    @ApiOperation(value = "获取Json数据")
    public Object getJsonById(@ApiParam(value = "区域名称") @PathVariable(name = "id") String id) {
        try {
            if (StringUtils.isBlank(id)) {
                return new ContentResultForm(false, "json信息ID不能为空!");
            }
            AgJsonStore jsonStore_out = iAgJsonStore.getById(id);
            if (null == jsonStore_out) {
                return new ContentResultForm(true, "暂无数据!");
            }
            return jsonStore_out.getJson();
        } catch (Exception e) {
            return new ContentResultForm(false, e,e.getMessage());
        }
    }

}
