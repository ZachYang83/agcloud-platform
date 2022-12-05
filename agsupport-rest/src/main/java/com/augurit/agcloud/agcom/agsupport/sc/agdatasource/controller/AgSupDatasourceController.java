package com.augurit.agcloud.agcom.agsupport.sc.agdatasource.controller;

import com.augurit.agcloud.agcom.agsupport.common.datasource.DbConstants;
import com.augurit.agcloud.agcom.agsupport.common.datasource.DynamicDataSource;
import com.augurit.agcloud.agcom.agsupport.common.util.DESedeUtil;
import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;
import com.augurit.agcloud.agcom.agsupport.domain.AgSupDatasource;
import com.augurit.agcloud.agcom.agsupport.sc.agdatasource.service.IAgSupDatasource;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.JsonUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author zhangmingyang
 * @Description: 数据源管理controller
 * @date 2019-01-11 11:51
 */
@Api(value = "数据源管理",description = "数据源管理相关接口")
@RestController
@RequestMapping("/agsupport/supdatasource")
public class AgSupDatasourceController {
    private final static Logger LOGGER = LoggerFactory.getLogger(AgSupDatasourceController.class);
    @Autowired
    private IAgSupDatasource supDatasource;
    @RequestMapping("/index.html")
    public ModelAndView index(){
        return new ModelAndView("agcloud/agcom/agsupport/agdatasource/index");
    }

    @ApiOperation(value = "根据数据源名称和分页条件查询数据源信息",notes = "根据数据源名称和分页条件查询数据源信息接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name",value = "数据源名称",dataType = "String"),
            @ApiImplicitParam(name = "page",value = "分页参数:page=1&rows=10",dataType = "Page"),
    })
    @RequestMapping(value = "/findDataSourceList",method = RequestMethod.GET)
    public ContentResultForm findList(String name, Page page) throws Exception {
        PageInfo<AgSupDatasource> list = supDatasource.findList(name, page);
        EasyuiPageInfo<AgSupDatasource> agSupDatasourceEasyuiPageInfo = PageHelper.toEasyuiPageInfo(list);
        return new ContentResultForm<EasyuiPageInfo>(true,agSupDatasourceEasyuiPageInfo);
    }

    @ApiOperation(value = "查询所有数据源信息",notes = "查询所有数据源信息接口")
    @RequestMapping(value = "/findAllDataSourceList",method = RequestMethod.GET)
    public ContentResultForm findAllList() throws Exception {
        return new ContentResultForm<List>(true,supDatasource.findAllList());
    }

    @ApiOperation(value = "新增或修改数据源信息",notes = "新增或修改数据源信息接口")
    //@ApiImplicitParam(name = "datasource",value = "数据源对象",dataType = "AgSupDatasource")
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public ResultForm save(AgSupDatasource datasource, HttpServletRequest request) throws Exception {
        if(StringUtils.isNotBlank(datasource.getId())){
            String dbType = datasource.getDbType();
            if ("oracle".equals(dbType.toLowerCase())){
                datasource.setDbUrl("jdbc:oracle:thin:@"+datasource.getIp()+":"+datasource.getPort()+"/"+datasource.getSid());
            }else if("postgresql".equals(dbType.toLowerCase())){
                datasource.setDbUrl("jdbc:postgresql://"+datasource.getIp()+":"+datasource.getPort()+"/"+datasource.getSid());
            }
            //修改了数据源密码时，需要对密码加密
            AgSupDatasource datasourcePwd = (AgSupDatasource)this.getDatasourceById(datasource.getId()).getContent();
            if (!datasource.getPassword().equals(datasourcePwd.getPassword())){
                datasource.setPassword(DESedeUtil.desEncrypt(datasource.getPassword()));
            }
            supDatasource.updateDataSource(datasource);
            // 修改了数据源信息，需要关闭原来的数据源
            DynamicDataSource.getInstance().closeDatasource(datasource.getId());
        }else {
            datasource.setId(UUID.randomUUID().toString());
            String dbType = datasource.getDbType();
            if ("oracle".equals(dbType.toLowerCase())){
                datasource.setDbUrl("jdbc:oracle:thin:@"+datasource.getIp()+":"+datasource.getPort()+"/"+datasource.getSid());
            }else if("postgresql".equals(dbType.toLowerCase())){
                datasource.setDbUrl("jdbc:postgresql://"+datasource.getIp()+":"+datasource.getPort()+"/"+datasource.getSid());
            }
            datasource.setPassword(DESedeUtil.desEncrypt(datasource.getPassword()));
            int insert = supDatasource.insert(datasource);
        }
        return new ResultForm(true,"保存成功");
    }

    @ApiOperation(value = "删除数据源",notes = "删除数据源接口")
    @ApiImplicitParam(name = "ids",value = "数据源id,多个id英文逗号分隔",required = true,dataType = "String")
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    public ResultForm detele(String ids){
        String result = "删除失败!";
        if (StringUtils.isNotBlank(ids)){
            String[] split = ids.split(",");
            if (split != null && split.length>0){
                try {
                    for (String id : split){
                        supDatasource.deleteDataSourceById(id);
                        //成功删除数据源记录时，需要从缓存中删除
                        DynamicDataSource.getInstance().closeDatasource(id);
                    }
                    return new ResultForm(true,"删除成功");
                } catch (Exception e) {
                    result = "删除失败!";
                    LOGGER.error("删除出出错");
                    e.printStackTrace();
                }
            }
        }
        return  new ResultForm(false,result+"id为空");
    }

    @ApiOperation(value = "根据id获取数据源",notes = "根据id获取数据源接口")
    @ApiImplicitParam(name = "id",value = "数据源id",required = true,dataType = "String")
    @RequestMapping(value = "/getDatasourceById/{id}",method = RequestMethod.GET)
    public ContentResultForm getDatasourceById(@PathVariable String id) throws Exception {
        AgSupDatasource agSupDatasource = supDatasource.selectDataSourceById(id);
        return new ContentResultForm<AgSupDatasource>(true,agSupDatasource);
    }

    @ApiOperation(value = "测试数据源是否能连接成功",notes = "测试数据源是否能连接成功接口")
    @RequestMapping(value = "/testConn",method = RequestMethod.GET)
    public ResultForm testConn(AgSupDatasource agSupDatasource){
        String ip = agSupDatasource.getIp();
        String dbType = agSupDatasource.getDbType().toLowerCase();
        String userName = agSupDatasource.getUserName();
        String password = "";
        if(StringUtils.isNotBlank(agSupDatasource.getId())){
            password = DESedeUtil.desDecrypt(agSupDatasource.getPassword());
        }else {
            password = agSupDatasource.getPassword();
        }

        String port = agSupDatasource.getPort();
        String sid = agSupDatasource.getSid();
        String dbUrl = "";
        if ("oracle".equals(dbType)){
            dbUrl = "jdbc:oracle:thin:@"+ip+":"+port+"/"+sid;
        }else if("postgresql".equals(dbType.toLowerCase())){
            dbUrl = "jdbc:postgresql://"+ip+":"+port+"/"+sid;
        }else if ("mysql".equals(dbType)){
            dbUrl = "jdbc:mysql://"+ip+":"+port+"/"+sid;
        }
        ResultForm result = testConnection(dbType, userName, password, dbUrl);
        return result;
    }

    public ResultForm testConnection(String dbType,String userName,String password,String dbUrl){
        ResultForm result = null;
        String driverClassname = "";
        if (DbConstants.ORACLE.equals(dbType)){
            driverClassname = DbConstants.ORACLE_DRIVER2;
        }else if (DbConstants.POSTGRESQL.equals(dbType)){
            driverClassname = DbConstants.POSTGRESQL_DRIVER;
        }else if (DbConstants.MYSQL.equals(dbType)){
            driverClassname = DbConstants.MYSQL_DRIVER;
        }
        try {
            Class.forName(driverClassname);
            DriverManager.setLoginTimeout(8);
            Connection connection = DriverManager.getConnection(dbUrl, userName, password);
            if (connection != null){
                result =new ResultForm(true,"数据库连接成功!");
            }
            else{
                result = new ResultForm(false,"数据库连接失败! 请检查数据库连接信息");
            }
        } catch (ClassNotFoundException e) {
            result = new ResultForm(false,"ClassNotFoundException:数据库连接失败! 请检查数据库连接信息");
            e.printStackTrace();
        } catch (SQLException e) {
            result = new ResultForm(false,"SQLException:数据库连接失败! 请检查数据库连接信息");
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 校验数据源是否已经跟图层关联
     * @return
     */
    @ApiOperation(value = "校验数据源是否已经跟图层关联",notes = "校验数据源是否已经跟图层关联接口")
    @ApiImplicitParam(name = "ids",value = "数据源的id，多个id英文逗号分隔",required = true,dataType = "String")
    @RequestMapping(value = "/checkDatasource",method = RequestMethod.GET)
    public ResultForm checkDataSource(String ids) throws Exception{
        List<AgLayer> vectorList = supDatasource.findVectorList();
        String[] split = ids.split(",");
        for (String datasourceId : split){
            for (AgLayer layer:vectorList){
                String data = layer.getData();
                Map<String, Object> stringObjectMap = JsonUtils.mapFromJson(data);
                Object dataSourceId = stringObjectMap.get("dataSourceId");
                if (dataSourceId != null && dataSourceId.toString().length()>0 && dataSourceId.toString().equals(datasourceId)){
                    return new ResultForm(true,"数据源已关联图层数据");
                }
            }
        }
        return new ResultForm(false,"数据源未关联图层数据");
    }

}
