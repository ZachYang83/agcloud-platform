package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimCheck.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.exception.SourceException;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgBimCheckStandard;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgBimCheckStandardExample;
import com.augurit.agcloud.agcom.agsupport.mapper.AgBimCheckStandardCustomMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.auto.AgBimCheckStandardMapper;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimCheck.service.IAgBimCheckStandardService;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.StringUtils;
import com.augurit.agcloud.agcom.agsupport.util.ExcelUtil;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName AgBimCheckStandardServiceImpl
 * @Author lizih
 * @Date 2020/12/18 16:40
 * @Version 1.0
 */
@Service
public class AgBimCheckStandardServiceImpl implements IAgBimCheckStandardService {
    // excel 文件格式
    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";
    // excel数据格式模板 模板字符串，模板定义：excel列号| 备注(属性名)| 属性名
    private static final String MODEL_TETX = "1|规范审查条文|clause,2|条文序号|serial,3|是否强条|enforce,4|条文内容拆解|clauseContent,5|关联模型信息|associateModel";

    @Autowired
    private AgBimCheckStandardMapper agBimCheckStandardMapper;

    @Autowired
    private AgBimCheckStandardCustomMapper agBimCheckStandardCustomMapper;

    @Override
    public PageInfo<AgBimCheckStandard> find(String category, Page page) {
        if (StringUtils.isEmpty(category)){
            PageHelper.startPage(page);
            List<AgBimCheckStandard> list = agBimCheckStandardMapper.selectByExample(null);
            return new PageInfo<>(list);
        }
        AgBimCheckStandardExample bimCheckStandardExample = new AgBimCheckStandardExample();
        bimCheckStandardExample.createCriteria().andClauseCategoryEqualTo(category);
        PageHelper.startPage(page);
        List<AgBimCheckStandard> list = agBimCheckStandardMapper.selectByExample(bimCheckStandardExample);
        return new PageInfo<>(list);
    }

    @Override
    public void save(AgBimCheckStandard agBimCheckStandard) {
        if (StringUtils.isEmpty(agBimCheckStandard.getClause().trim())){
            throw new SourceException("规范审查条文不能为空,请检查");
        }
        if (StringUtils.isNotEmpty(agBimCheckStandard.getId())) {
            // 修改时间
            agBimCheckStandard.setModifyTime(new Timestamp(new Date().getTime()));
            agBimCheckStandardMapper.updateByPrimaryKeySelective(agBimCheckStandard);
        } else {
            // 创建时间
            agBimCheckStandard.setCreateTime(new Timestamp(new Date().getTime()));
            agBimCheckStandard.setId(UUID.randomUUID().toString());
            agBimCheckStandardMapper.insertSelective(agBimCheckStandard);
        }
    }

    @Override
    public void deleteByIds(String[] ids) {
        for (String id:ids){
            int deleteReturnInt = agBimCheckStandardMapper.deleteByPrimaryKey(id);
            if (deleteReturnInt<1){
                throw new SourceException("请检查id是否存在");
            }
        }
    }

    @Override
    public void excelImport(MultipartFile file) {
        try{
            if (file == null) {
                throw new SourceException("文件不能为空");
            }
            String fileName = file.getOriginalFilename();
            if (!fileName.endsWith(EXCEL_XLSX) || fileName.endsWith(EXCEL_XLS)) {
                throw new SourceException("文件类型不正确，必须为.xls或者.xlsx格式");
            }
            // excel 转换审查条目对象集合
            List<AgBimCheckStandard> bimCheckStandardClauseList = ExcelUtil.readExcel(file, AgBimCheckStandard.class, MODEL_TETX, "clauseCategory",0,1);
            // 遍历集合
            bimCheckStandardClauseList.forEach(clause -> {
                // 新增，赋值uuid
                clause.setId(UUID.randomUUID().toString());
                // 创建时间
                clause.setCreateTime(new Timestamp(new Date().getTime()));
                if (StringUtils.isNotEmpty(clause.getClause())){
                    clause.setClause(clause.getClause().replaceAll("\\s",""));
                }
            });
            System.out.println(bimCheckStandardClauseList);
            // 批量对象
            agBimCheckStandardCustomMapper.insertList(bimCheckStandardClauseList);

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
