package com.augurit.agcloud.agcom.agsupport.sc.statistics.controller;

import com.augurit.agcloud.agcom.agsupport.domain.AgDataSubject;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.StringUtils;
import com.augurit.agcloud.agcom.agsupport.sc.statistics.service.IAgDataOverviewService;
import com.augurit.agcloud.agcom.agsupport.sc.statistics.service.IAgDataSubjectService;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author zhangmingyang
 * @Description: 资源数据主题接口
 * @date 2019-09-02 10:10
 */
@Api(value = "资源数据主题接口",description = "资源数据主题接口")
@RestController
@RequestMapping("/agsupport/dataSubject")
public class AgDataSubjectController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AgDataSubjectController.class);

    @Autowired
    private IAgDataSubjectService agDataSubjectService;

    @Autowired
    private IAgDataOverviewService agDataOverviewService;

    @ApiOperation(value = "获取所有数据主题",notes = "获取所有数据主题接口")
    @RequestMapping(value = "/findAllSubject",method = RequestMethod.GET)
    public ContentResultForm findAllSubject(){
        ContentResultForm resultForm = new ContentResultForm(true);
        resultForm.setContent(agDataSubjectService.findAllSubject());
        return resultForm;
    }

    @ApiOperation(value = "保存或修改数据主题",notes = "保存或修改数据主题接口")
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public ContentResultForm save(AgDataSubject agDataSubject){
        ContentResultForm resultForm = new ContentResultForm(true);
        if (StringUtils.isNotBlank(agDataSubject.getId())){
            // 更新修改的主题的名称跟目录
            AgDataSubject subject = agDataSubjectService.selectById(agDataSubject.getId());
            String xpath = "";
            String oldXpath = "";
            subject.setSubjectName(agDataSubject.getSubjectName());
            oldXpath = subject.getXpath();
            xpath = oldXpath.substring(0, oldXpath.lastIndexOf("/") + 1) + agDataSubject.getSubjectName();
            subject.setXpath(xpath);
            agDataSubjectService.updateSubject(subject);

            // 再修改子目录的路径
            if(!xpath.equals(oldXpath)){
                List<AgDataSubject> oldlist = agDataSubjectService.findByXpath(oldXpath);
                for (AgDataSubject dataSubject : oldlist){
                    dataSubject.setXpath(dataSubject.getXpath().replaceFirst(oldXpath,xpath));
                }
                if (oldlist.size() > 0){
                    agDataSubjectService.updateSubjectBatch(oldlist);
                }
            }

        }else {
            Integer subjectLevel = agDataSubject.getSubjectLevel();
            if (subjectLevel == 0){
                AgDataSubject agDataSubject1 = agDataSubjectService.selectByName(agDataSubject.getSubjectName());
                if (agDataSubject1 != null){
                    resultForm.setMessage("专题已存在!");
                    resultForm.setSuccess(false);
                    return resultForm;
                }
            }
            List<AgDataSubject> agDataSubjects = agDataSubjectService.selectByParenIdAndName(agDataSubject.getParentId(), agDataSubject.getSubjectName());
            if (agDataSubjects.size()>0){
                resultForm.setMessage("子目录重复,请重新创建目录!");
                resultForm.setSuccess(false);
                return resultForm;
            }
            agDataSubject.setId(UUID.randomUUID().toString());
            String parentId = agDataSubject.getParentId();
            if (StringUtils.isBlank(parentId)){
                agDataSubject.setXpath("/"+agDataSubject.getSubjectName());
            }else {
                AgDataSubject pDataSubject = agDataSubjectService.selectById(parentId);
                agDataSubject.setXpath(pDataSubject.getXpath() + "/"+agDataSubject.getSubjectName());
            }

            agDataSubjectService.insert(agDataSubject);
        }
        resultForm.setMessage("保存成功!");
        return resultForm;
    }

    @ApiOperation(value = "根据ID删除数据主题和主题的数据",notes = "根据ID删除数据主题和主题的数据接口")
    @ApiImplicitParam(name = "id",value = "数据专题id",dataType = "string")
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.DELETE)
    public ContentResultForm delete(@PathVariable("id") String id){
        ContentResultForm resultForm = new ContentResultForm(true);
        try {
           agDataSubjectService.deleteSubjects(id);
        }catch (Exception e){
            resultForm.setSuccess(false);
            resultForm.setMessage("删除失败!");
            LOGGER.error("删除异常",e);
        }
        return resultForm;
    }

    @ApiOperation(value = "同级主题目录排序",notes = "同级主题目录排序接口")
    @ApiImplicitParam(name = "ids",value = "拖拽目录之后的同级目录的ID拼接成的字符串,逗号分隔",dataType = "string")
    @RequestMapping(value = "/subjectSort",method = RequestMethod.POST)
    public ContentResultForm subjectSort(String ids){
        ContentResultForm resultForm = new ContentResultForm(true);
        if (StringUtils.isNotBlank(ids)){
            String[] subjectIds = ids.split(",");
            try {
                List<AgDataSubject> agDataSubjects = agDataSubjectService.selectByIds(subjectIds);
                List<AgDataSubject> updateList = new ArrayList<>();
                for (int i = 0;i<agDataSubjects.size();i++){
                    AgDataSubject dataSubject = new AgDataSubject();
                    dataSubject.setId(subjectIds[i]);
                    dataSubject.setOrderNo(i);
                    updateList.add(dataSubject);
                }
                agDataSubjectService.updateSubjectBatch(updateList);
            }catch (Exception e){
                resultForm.setMessage("目录排序出错了!");
                resultForm.setSuccess(false);
                LOGGER.error("主题目录排序异常",e);
            }

        }
        return resultForm;
    }

}
