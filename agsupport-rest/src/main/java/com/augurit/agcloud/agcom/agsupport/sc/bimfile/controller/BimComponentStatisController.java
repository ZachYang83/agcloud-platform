package com.augurit.agcloud.agcom.agsupport.sc.bimfile.controller;

import com.augurit.agcloud.agcom.agsupport.domain.AgSupDatasource;
import com.augurit.agcloud.agcom.agsupport.sc.agdatasource.service.IAgSupDatasource;
import com.augurit.agcloud.agcom.agsupport.sc.bimfile.service.IBimComponentStatis;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.common.util.page.Pager;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(value = "BIM模型构件统计",description = "BIM模型构件统计相关接口")
@RequestMapping("agsupport/bim")
public class BimComponentStatisController {

    @Value("${bim.attribute.datasource}")
    private String datasourceName;
    @Autowired
    private IBimComponentStatis iBimComponentStatis;

    @Autowired
    private IAgSupDatasource iAgSupDatasource;


    @ApiOperation(value = "根据BIM模型名称统计模型构件",notes = "根据BIM模型名称统计模型构件")
    @ApiImplicitParam(name = "name",value = "BIM模型名称")
    @RequestMapping("componentStatis")
    ContentResultForm componentStatisticsBy(String tableName, int pageSize, int pageNum) throws Exception {

        Map resultList = new HashMap();
        try {
            Pager page = new Pager(pageNum, pageSize);

            AgSupDatasource datasource = iAgSupDatasource.selectDataSourceByName(datasourceName);
            if (datasource == null) return new ContentResultForm<List>(false, null,"找不到BIM属性数据库，请在后台进行配置");

            //1. 判断表是否存在，即判断建筑信息模型的属性表是否导入到数据库
            boolean exist = iBimComponentStatis.bimAttributeDataIsExist(tableName, datasource.getId());
            if (!exist) return new ContentResultForm<List>(false, null,"BIM属性数据库数据中找不到属性表《"+tableName+"》");

            resultList = iBimComponentStatis.componentStatisticsBy(tableName, datasource.getId(),page);

        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<List>(false, null,"访问出错");
        }
        return new ContentResultForm<Map>(true, resultList, "查询成功");
    }

}
