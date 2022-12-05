package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimCheck.controller;

import com.augurit.agcloud.agcom.agsupport.common.exception.SourceException;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgBimCheckStandard;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimCheck.service.IAgBimCheckStandardService;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.StringUtils;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @ClassName AgBimCheckStandardController
 * @Author lizih
 * @Date 2020/12/23 9:21
 * @Version 1.0
 */
@Api(value = "审查规范库", description = "审查规范库相关接口")
@RestController
@RequestMapping("/agsupport/bimCheck/standard")
public class AgBimCheckStandardController {
    private static final Logger logger = LoggerFactory.getLogger(AgBimCheckStandardController.class);

    @Autowired
    private IAgBimCheckStandardService agBimCheckStandardService;

    @ApiOperation(value = "分页获取审查规范条文", notes = "分页获取审查规范条文接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "category", value = "条文所属类型，不传参则全部查询", dataType = "String"),
            @ApiImplicitParam(name = "rows", value = "分页参数:page=1&rows=10", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "分页参数:page=1&rows=10", dataType = "String")
    })
    @GetMapping(value = "/find")
    public ContentResultForm find(String category, Page page) {
        try{
            PageInfo<AgBimCheckStandard> pageInfo = agBimCheckStandardService.find(category, page);
            return new ContentResultForm(true, PageHelper.toEasyuiPageInfo(pageInfo));
        } catch (SourceException se){
            logger.info(se.getMessage());
            return new ContentResultForm(false, null, "查询失败"+se.getMessage());
        } catch (Exception e){
            logger.info(e.getMessage());
            return new ContentResultForm(false, null, "查询失败");
        }
    }

    @ApiOperation(value = "获取审查规范条文", notes = "获取审查规范条文接口，非分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "category", value = "条文所属类型，不传参则全部查询", dataType = "String"),
            @ApiImplicitParam(name = "paramType", value = "默认为1：非分页查询所有条文。2：获取所有条文类型。3：获取特定条文类型下的规范名称,必传category,可选clause", dataType = "String"),
            @ApiImplicitParam(name = "clause", value = "条纹规范库名称，配合paramType=3使用", dataType = "String"),
    })
    @GetMapping(value = "/get")
    public ContentResultForm get(String category,
                                 @RequestParam(value = "paramType", defaultValue = "1") String paramType,
                                 String clause
                                 ) {
        try{
            if ("1".equals(paramType)){
                List<AgBimCheckStandard> list = agBimCheckStandardService.get(category);
                return new ContentResultForm(true, list);
            }
            if ("2".equals(paramType)){
                List<String> categories = agBimCheckStandardService.getCategories();
                return new ContentResultForm(true,categories);
            }
            if ("3".equals(paramType) && StringUtils.isNotEmpty(category) && StringUtils.isEmpty(clause)){
                List<String> clauses = agBimCheckStandardService.getClauses(category);
                return new ContentResultForm(true,clauses);
            }
            if ("3".equals(paramType) && StringUtils.isNotEmpty(category) && StringUtils.isNotEmpty(clause)){
                List<AgBimCheckStandard> clauseContents = agBimCheckStandardService.getClauseContents(category,clause);
                return new ContentResultForm(true,clauseContents);
            }
            return new ContentResultForm(false,null,"请检查paramType或category传入参数是否符合规范");
        } catch (SourceException se){
            logger.info(se.getMessage());
            return new ContentResultForm(false, null, "查询失败"+se.getMessage());
        } catch (Exception e){
            logger.info(e.getMessage());
            return new ContentResultForm(false, null, "查询失败");
        }
    }

    @ApiOperation(value = "获取审查规范条文树结构", notes = "获取审查规范条文，按照序号整理成树结构")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "category", value = "条文所属类型，必传", dataType = "String"),
            @ApiImplicitParam(name = "clause", value = "规范审查条文，必传", dataType = "String")
    })
    @GetMapping(value = "/tree")
    public ContentResultForm tree(String category, String clause) {
        if (StringUtils.isEmpty(category) || StringUtils.isEmpty(clause)){
            return new ContentResultForm(false,null,"category和clause为必传参数");
        }
        try{
            Map<String, Object> mapTree = agBimCheckStandardService.tree(category, clause);
            return new ContentResultForm(true,mapTree);
        } catch (SourceException se){
            logger.info(se.getMessage());
            return new ContentResultForm(false, null, "查询失败"+se.getMessage());
        } catch (Exception e){
            logger.info(e.getMessage());
            return new ContentResultForm(false, null, "查询失败");
        }
    }

}
