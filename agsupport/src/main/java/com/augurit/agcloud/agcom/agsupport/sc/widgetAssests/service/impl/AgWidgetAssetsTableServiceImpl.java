package com.augurit.agcloud.agcom.agsupport.sc.widgetAssests.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.exception.SourceException;
import com.augurit.agcloud.agcom.agsupport.domain.auto.*;
import com.augurit.agcloud.agcom.agsupport.mapper.AgWidgetAssetsCustomMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.auto.AgWidgetAssetsColumnsMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.auto.AgWidgetAssetsProjectMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.auto.AgWidgetAssetsTableMapper;
import com.augurit.agcloud.agcom.agsupport.sc.widgetAssests.service.IAgWidgetAssetsTableService;
import com.augurit.agcloud.agcom.agsupport.util.WidgetAssetsUtils;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @Author: qinyg
 * @Date: 2020/11/17
 * @tips:
 */
@Service
public class AgWidgetAssetsTableServiceImpl implements IAgWidgetAssetsTableService {

    @Autowired
    private AgWidgetAssetsTableMapper agThematicTableMapper;
    @Autowired
    private AgWidgetAssetsColumnsMapper agThematicColumnsMapper;
    @Autowired
    private AgWidgetAssetsCustomMapper agThematicCustomMapper;
    @Autowired
    private AgWidgetAssetsProjectMapper agThematicProjectMapper;

    @Override
    public PageInfo<AgWidgetAssetsTable> find(String appSoftId, String tableName, Page page) {
        AgWidgetAssetsTableExample example = new AgWidgetAssetsTableExample();
        example.setOrderByClause("create_time desc");
        AgWidgetAssetsTableExample.Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(appSoftId)) {
            criteria.andAppSoftIdEqualTo(appSoftId);
        }
        //模糊查询
        if (!StringUtils.isEmpty(tableName)) {
            criteria.andTableNameLike("%" + tableName + "%");
        }
        PageHelper.startPage(page);
        List<AgWidgetAssetsTable> list = agThematicTableMapper.selectByExample(example);
        return new PageInfo<>(list);
    }

    @Override
    @Transactional
    public void add(AgWidgetAssetsTable table, List<AgWidgetAssetsColumns> columns) {
        //查询专题项目映射表
        String projectIdentity = getProjectUniqueIdfIsExist(table.getAppSoftId());

        //判断当前项目下的表名是否唯一，用项目id和表名作为查询条件
        AgWidgetAssetsTableExample example = new AgWidgetAssetsTableExample();
        AgWidgetAssetsTableExample.Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(table.getAppSoftId())) {
            criteria.andAppSoftIdEqualTo(table.getAppSoftId());
        }
        if (!StringUtils.isEmpty(table.getTableName())) {
            criteria.andTableNameEqualTo(table.getTableName());
        }
        int count = agThematicTableMapper.countByExample(example);
        if (count != 0) {
            throw new SourceException("表名不能重复");
        }
        //专题表主键id
        String thematicTableId = UUID.randomUUID().toString();

        String needCreateTableName = WidgetAssetsUtils.TABLE_PREFIX + projectIdentity + "_" + table.getTableName();
        //拼接插入表的sql
        StringBuffer createTableSql = new StringBuffer();
        createTableSql.append("create table ");
        createTableSql.append(needCreateTableName);
        createTableSql.append("( ");
        //遍历属性
        for (AgWidgetAssetsColumns column : columns) {
            if (!StringUtils.isEmpty(column.getColumnName())) {
                //字段名非法验证
                isLegalCertainField(column.getColumnName());

                createTableSql.append(column.getColumnName());
                createTableSql.append(getColumnType(column.getColumnType()));
                createTableSql.append(",");

                //设置sql的同时，数据入库
                column.setId(UUID.randomUUID().toString());
                column.setThematicTableId(thematicTableId);
                column.setCreateTime(new Date());
                agThematicColumnsMapper.insert(column);
            }
        }
        //赋值设置主键CONSTRAINT "ag_thematic_table_pkey" PRIMARY KEY ("id")
        createTableSql.append(" constraint ");
        createTableSql.append(needCreateTableName);
        //默认id作为主键
        createTableSql.append("_pkey primary key (id) ");
        createTableSql.append(")");

        //插入表，此处插入可能会报异常，如：表名已存在，需要自定义异常处理
        try {
            agThematicCustomMapper.executeDefineSql(createTableSql.toString());
        } catch (Exception e) {
            throw new SourceException("添加失败，表民或者属性不能包含关键词");
        }
        //添加专题表数据
        table.setCreateTime(new Date());
        table.setId(thematicTableId);
        agThematicTableMapper.insert(table);
    }

    private String getColumnType(String columnType) {
        //1字符；2整数；3 JSON；4 文件
        if ("1".equals(columnType)) {
            return " varchar(255)";
        }
        if ("2".equals(columnType)) {
            return " int4";
        }
        if ("3".equals(columnType)) {
            return " text";
        }
        if ("4".equals(columnType)) {
            return " bytea";
        }
        //默认
        return " varchar(255)";
    }
    // 判断某个字段名是否合法,不合法则抛出异常，合法则不做操作
    private void isLegalCertainField(String fieldName){
        String regxCheck = "[a-z_]{1,15}";
        Boolean fieldNameLegal = Pattern.matches(regxCheck, fieldName.trim());
        if (!fieldNameLegal){
            throw new SourceException("字段名非法，只能小写字母或下划线，1到15个字符，错误字段："+ fieldName);
        }
    }

    @Override
    @Transactional
    public void deletes(String ids) {
        String[] deleteIdArray = ids.split(",");
        //查询当前的数据
        AgWidgetAssetsTable thematicTableCheckProject = agThematicTableMapper.selectByPrimaryKey(deleteIdArray[0]);
        if (thematicTableCheckProject == null) {
            throw new SourceException("参数错误，参数：" + deleteIdArray[0]);
        }

        //查询专题项目映射表
        String projectIdentity = getProjectUniqueIdfIsExist(thematicTableCheckProject.getAppSoftId());

        for (String id : deleteIdArray) {
            //查询当前的数据
            AgWidgetAssetsTable thematicTable = agThematicTableMapper.selectByPrimaryKey(id);
            if (thematicTable != null) {
                //drop 表
                agThematicCustomMapper.executeDefineSql("drop table " + WidgetAssetsUtils.TABLE_PREFIX + projectIdentity + "_" + thematicTable.getTableName());
                //删除专题表数据
                agThematicTableMapper.deleteByPrimaryKey(id);
                //删除专题表下字段表数据
                AgWidgetAssetsColumnsExample columnsExample = new AgWidgetAssetsColumnsExample();
                columnsExample.createCriteria().andThematicTableIdEqualTo(id);
                agThematicColumnsMapper.deleteByExample(columnsExample);
            }
        }
    }

    @Override
    @Transactional
    public void update(AgWidgetAssetsTable table, List<AgWidgetAssetsColumns> columns, String deleteColumnIds) {
        String id = table.getId();

        //专题表数据
        AgWidgetAssetsTable dbAgWidgetAssetsTable = agThematicTableMapper.selectByPrimaryKey(id);
        if (dbAgWidgetAssetsTable == null) {
            throw new SourceException("id参数不正确，找不到专题表");
        }

        //查询专题项目映射表
        String projectIdentity = getProjectUniqueIdfIsExist(dbAgWidgetAssetsTable.getAppSoftId());

        String dbTableName = dbAgWidgetAssetsTable.getTableName();

        //表名重复验证
        checkThematicTableTableNameIsExist(dbAgWidgetAssetsTable.getAppSoftId(), table.getTableName(), dbAgWidgetAssetsTable.getId());

        //需要删除的字段
        if (!StringUtils.isEmpty(deleteColumnIds)) {
            for (String deleteColumn : deleteColumnIds.split(",")) {
                if (!StringUtils.isEmpty(deleteColumn)) {
                    //查询当前数据
                    AgWidgetAssetsColumns agThematicColumns = agThematicColumnsMapper.selectByPrimaryKey(deleteColumn);
                    //判断数据必须存在
                    if (agThematicColumns == null) {
                        throw new SourceException("参数不正确，找不到专题属性表" + deleteColumn);
                    }
                    //数据库删除字段
                    agThematicColumnsMapper.deleteByPrimaryKey(deleteColumn);

                    //alter 数据库表删除字段
                    agThematicCustomMapper.executeDefineSql("alter table " + WidgetAssetsUtils.TABLE_PREFIX + projectIdentity + "_" + dbTableName + " drop column " + agThematicColumns.getColumnName());
                }
            }
        }
        //需要修改或者添加的字段
        if (columns != null && columns.size() != 0) {
            for (AgWidgetAssetsColumns agThematicColumns : columns) {
                if(StringUtils.isEmpty(agThematicColumns.getColumnName())){
                    continue;
                }
                //字段名非法验证
                isLegalCertainField(agThematicColumns.getColumnName());

                //id为空，就是需要添加的字段，id不为空，需要修改的字段
                if (StringUtils.isEmpty(agThematicColumns.getId())) {
                    //需要添加，添加的，必须不能重复，需要做判断
                    AgWidgetAssetsColumnsExample columnsExample = new AgWidgetAssetsColumnsExample();
                    columnsExample.createCriteria().andThematicTableIdEqualTo(id).andColumnNameEqualTo(agThematicColumns.getColumnName());
                    int count = agThematicColumnsMapper.countByExample(columnsExample);
                    if (count != 0) {
                        throw new SourceException("属性字段重复，不能重复添加。字段是：" + agThematicColumns.getColumnName());
                    }

                    //添加数据库表数据
                    agThematicColumns.setId(UUID.randomUUID().toString());
                    agThematicColumns.setThematicTableId(id);
                    agThematicColumns.setCreateTime(new Date());
                    agThematicColumnsMapper.insert(agThematicColumns);

                    // alter 添加属性
                    agThematicCustomMapper.executeDefineSql("alter table " + WidgetAssetsUtils.TABLE_PREFIX + projectIdentity + "_" + dbTableName + " add " + agThematicColumns.getColumnName() + getColumnType(agThematicColumns.getColumnType()));
                }
                if (!StringUtils.isEmpty(agThematicColumns.getId())) {
                    //是否需要修改，id相同，name相同，就不需要修改
                    AgWidgetAssetsColumns checkIsEqual = agThematicColumnsMapper.selectByPrimaryKey(agThematicColumns.getId());
                    if (checkIsEqual == null) {
                        throw new SourceException("修改属性id错误，不存在次数据id=" + agThematicColumns.getId() + " 属性=" + agThematicColumns.getColumnName());
                    }
                    //只有名称不相同才修改
                    if (!checkIsEqual.getColumnName().equals(agThematicColumns.getColumnName())) {
                        //需要修改，修改的名称也不能重名，需要排除掉本身
                        AgWidgetAssetsColumnsExample columnsExample = new AgWidgetAssetsColumnsExample();
                        columnsExample.createCriteria().andThematicTableIdEqualTo(id).andColumnNameEqualTo(agThematicColumns.getColumnName()).andIdNotEqualTo(agThematicColumns.getId());
                        int count = agThematicColumnsMapper.countByExample(columnsExample);
                        if (count != 0) {
                            throw new SourceException("属性字段重复，不能重复修改。字段是：" + agThematicColumns.getColumnName());
                        }

                        //db数据属性
                        AgWidgetAssetsColumns dbAgWidgetAssetsColumns = agThematicColumnsMapper.selectByPrimaryKey(agThematicColumns.getId());

                        // alter 修改属性
                        agThematicCustomMapper.executeDefineSql("alter table " + WidgetAssetsUtils.TABLE_PREFIX + projectIdentity + "_" + dbTableName + " rename " + dbAgWidgetAssetsColumns.getColumnName() + " to " + agThematicColumns.getColumnName());

                        //修改数据库表数据
                        dbAgWidgetAssetsColumns.setColumnName(agThematicColumns.getColumnName());
                        agThematicColumnsMapper.updateByPrimaryKey(dbAgWidgetAssetsColumns);
                    }
                }
            }
        }

        //查看是否修改表名
        if (!dbTableName.equals(table.getTableName())) {
            //alter 表名改变，需要修改表名
            agThematicCustomMapper.executeDefineSql("alter table " + WidgetAssetsUtils.TABLE_PREFIX + projectIdentity + "_" + dbTableName + " rename to " + WidgetAssetsUtils.TABLE_PREFIX + projectIdentity + "_" + table.getTableName());
        }
        //需要更新的属性
        AgWidgetAssetsTable updateAgWidgetAssetsTable = new AgWidgetAssetsTable();
        updateAgWidgetAssetsTable.setTableName(table.getTableName());
        updateAgWidgetAssetsTable.setRemark(table.getRemark());
        updateAgWidgetAssetsTable.setModifyTime(new Date());
        updateAgWidgetAssetsTable.setId(id);
        agThematicTableMapper.updateByPrimaryKeySelective(updateAgWidgetAssetsTable);
    }

    private void checkThematicTableTableNameIsExist(String appSoftId, String tableName, String thematicTableId) {
        AgWidgetAssetsTableExample example = new AgWidgetAssetsTableExample();
        example.createCriteria().andAppSoftIdEqualTo(appSoftId).andTableNameEqualTo(tableName).andIdNotEqualTo(thematicTableId);
        int countByExample = agThematicTableMapper.countByExample(example);
        if (countByExample != 0) {
            throw new SourceException("表名不能重复，表名：" + tableName);
        }
    }

    private String getProjectUniqueIdfIsExist(String appSoftId) {
        AgWidgetAssetsProjectExample projectExample = new AgWidgetAssetsProjectExample();
        projectExample.createCriteria().andAppSoftIdEqualTo(appSoftId);
        List<AgWidgetAssetsProject> agThematicProjects = agThematicProjectMapper.selectByExample(projectExample);
        if (agThematicProjects == null || agThematicProjects.size() == 0) {
            throw new SourceException("请修改项目标示，再添加数据");
        }
        //查询项目所映射的标识，然后拼接起来，作为一个完整的表名
        String projectIdentity = agThematicProjects.get(0).getUniqueIdf();
        if (StringUtils.isEmpty(projectIdentity)) {
            throw new SourceException("请修改项目标示，再添加数据");
        }
        return projectIdentity;
    }

    @Override
    public List<AgWidgetAssetsColumns> getThematicColumns(String thematicTableId) {
        AgWidgetAssetsColumnsExample example = new AgWidgetAssetsColumnsExample();
        example.setOrderByClause("create_time asc");
        example.createCriteria().andThematicTableIdEqualTo(thematicTableId);
        List<AgWidgetAssetsColumns> agThematicColumns = agThematicColumnsMapper.selectByExample(example);
        return agThematicColumns;
    }

    @Override
    @Transactional
    public void updateThematicProjectNeedAlterThematicTable(String thematicProjectId, String updateUniqueIdf) {
        //需要修改该项目标识下的表名
        //查询该项目标识下的所有表
        AgWidgetAssetsProject dbThematicProject = agThematicProjectMapper.selectByPrimaryKey(thematicProjectId);

        //是否有唯一标识，如果没有，不需要修改
        if (StringUtils.isEmpty(dbThematicProject.getUniqueIdf()) && StringUtils.isEmpty(updateUniqueIdf)) {
            return;
        }

        //表名是否有修改，如果没有，不需要修改
        if (dbThematicProject.getUniqueIdf().equals(updateUniqueIdf)) {
            return;
        }
        //有项目标识
        if (dbThematicProject != null && !StringUtils.isEmpty(dbThematicProject.getAppSoftId())) {
            AgWidgetAssetsTableExample thematicTableExample = new AgWidgetAssetsTableExample();
            thematicTableExample.createCriteria().andAppSoftIdEqualTo(dbThematicProject.getAppSoftId());
            List<AgWidgetAssetsTable> agThematicTables = agThematicTableMapper.selectByExample(thematicTableExample);
            if (agThematicTables != null && agThematicTables.size() > 0) {
                for (AgWidgetAssetsTable thematicTable : agThematicTables) {
                    if (!StringUtils.isEmpty(thematicTable.getTableName())) {
                        String oldDbcreatedTableName = WidgetAssetsUtils.TABLE_PREFIX + dbThematicProject.getUniqueIdf() + "_" + thematicTable.getTableName();
                        String newDbcreatedTableName = WidgetAssetsUtils.TABLE_PREFIX + updateUniqueIdf + "_" + thematicTable.getTableName();
                        //alter 表名改变，需要修改表名
                        agThematicCustomMapper.executeDefineSql("alter table " + oldDbcreatedTableName + " rename to " + newDbcreatedTableName);
                    }
                }
            }
        }
    }


}
