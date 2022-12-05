package com.augurit.agcloud.agcom.agsupport.sc.identify.mancar.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.exception.SourceException;
import com.augurit.agcloud.agcom.agsupport.common.util.io.FileUtil;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgIdentifyMancarResult;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgIdentifyMancarResultExample;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgIdentifyMancarSource;
import com.augurit.agcloud.agcom.agsupport.mapper.AgIdentifyMancarResultCustomMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.auto.AgIdentifyMancarResultMapper;
import com.augurit.agcloud.agcom.agsupport.sc.identify.mancar.domain.AgIdentifyMancarResultResult;
import com.augurit.agcloud.agcom.agsupport.sc.identify.mancar.domain.AgIdentifyMancarResultParam;
import com.augurit.agcloud.agcom.agsupport.sc.identify.mancar.domain.AgIdentifyMancarResultStatisticsDomain;
import com.augurit.agcloud.agcom.agsupport.sc.identify.mancar.domain.AgIdentifyMancarResultStatisticsResult;
import com.augurit.agcloud.agcom.agsupport.sc.identify.mancar.service.IAgIdentifyMancarResultService;
import com.augurit.agcloud.agcom.agsupport.sc.identify.mancar.service.IAgIdentifyMancarSourceService;
import com.augurit.agcloud.agcom.agsupport.util.ExcelUtil;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.github.pagehelper.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: qinyg
 * @Date: 2020/12/28 11:33
 * @tips:
 */
@Service
public class AgIdentifyMancarResultServiceImpl implements IAgIdentifyMancarResultService {

    private static final Logger log = LoggerFactory.getLogger(AgIdentifyMancarResultServiceImpl.class);

    @Autowired
    private AgIdentifyMancarResultMapper identifyMancarResultMapper;
    @Autowired
    private IAgIdentifyMancarSourceService identifyMancarSourceService;
    @Autowired
    private AgIdentifyMancarResultCustomMapper identifyMancarResultCustomMapper;

    @Override
    @Transactional
    public void add(AgIdentifyMancarResultParam param, MultipartFile file) {
        //如果视频不为null，需要插入识别视频
        if(file != null && !file.isEmpty()){
            AgIdentifyMancarSource source = new AgIdentifyMancarSource();
            source.setId(param.getSourceId());
            source.setName("人车识别" + param.getIdentifyTime());
            source.setStatus("1");//1已识别
            identifyMancarSourceService.add(source, file);
        }

        //插入识别结果
        AgIdentifyMancarResult result = new AgIdentifyMancarResult();
        //取值，赋值
        result.setId(UUID.randomUUID().toString());
        result.setSourceId(param.getSourceId());
        result.setCreateTime(new Date());
        result.setNumCar(param.getNumCar());
        result.setNumPeople(param.getNumPeople());
        result.setRemark(param.getRemark());
        if(!StringUtils.isEmpty(param.getIdentifyTime()) && !StringUtils.isEmpty(param.getBaseTime())){
            try {
                DateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date identifyTime = sf.parse(param.getBaseTime());
                identifyTime.setTime(identifyTime.getTime() + param.getIdentifyTime() * 1000);
                result.setIdentifyTime(identifyTime);
            } catch (ParseException e) {
                log.info(e.getMessage());
            }
        }
        identifyMancarResultMapper.insert(result);
    }

    @Override
    public AgIdentifyMancarResultResult get(String sourceId, String startTimeParam, String endTimeParam) {
        //返回值封装
        AgIdentifyMancarResultResult result = new AgIdentifyMancarResultResult();

        //赋值最小时间
        Date minIdentifyTime = getMinOrMaxIdentifyTime(sourceId, true);
        if(minIdentifyTime == null){
            return result;
        }
        result.setStartTime(minIdentifyTime);

        //赋值最大时间
        Date maxIdentifyTime = getMinOrMaxIdentifyTime(sourceId, false);
        if(maxIdentifyTime == null){
            return result;
        }
        result.setEndTime(maxIdentifyTime);

        //分页查询列表
        //查询参数封装
        AgIdentifyMancarResultExample example = new AgIdentifyMancarResultExample();
        example.setOrderByClause("identify_time asc");
        AgIdentifyMancarResultExample.Criteria criteria = example.createCriteria();
        criteria.andSourceIdEqualTo(sourceId);
        //时间格式验证
        if(!StringUtils.isEmpty(startTimeParam)){
            Date date = checkTimeFormat(startTimeParam);
            criteria.andIdentifyTimeGreaterThanOrEqualTo(date);
        }
        //时间格式验证
        if(!StringUtils.isEmpty(endTimeParam)){
            Date date = checkTimeFormat(endTimeParam);
            criteria.andIdentifyTimeLessThanOrEqualTo(date);
        }
        List<AgIdentifyMancarResult> list = identifyMancarResultMapper.selectByExample(example);
        result.setList(list);

        return result;
    }

    /**
     *
     * @Author: qinyg
     * @Date: 2020/12/29 14:31
     * @tips: 获取最大或者最小的时间
     * @param sourceId
     * @param isMin true最小时间；false最大时间
     * @return
     */
    private Date getMinOrMaxIdentifyTime(String sourceId, boolean isMin){
        String orderByClause = "identify_time desc";
        if(isMin){
            orderByClause = "identify_time asc";
        }
        //查询参数封装
        AgIdentifyMancarResultExample example = new AgIdentifyMancarResultExample();

        //识别时间升序，查询最小时间,只要一条数据
        example.setOrderByClause(orderByClause);
        example.createCriteria().andSourceIdEqualTo(sourceId);
        Page page = new Page(1, 1);
        PageHelper.startPage(page);
        List<AgIdentifyMancarResult> results = identifyMancarResultMapper.selectByExample(example);
        if(results == null || results.size() ==0){
            return null;
        }
        return results.get(0).getIdentifyTime();
    }


    private Date checkTimeFormat(String time){
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sf.parse(time);
            return date;
        } catch (ParseException e) {
            throw new SourceException("时间格式不正确，必须是（yyyy-MM-dd HH:mm:ss）格式");
        }
    }
    @Override
    public AgIdentifyMancarResultStatisticsResult statisticsPeopleAndCarNum(String sourceId, Integer times) {
        //返回值封装
        AgIdentifyMancarResultStatisticsResult result = new AgIdentifyMancarResultStatisticsResult();
        //最大时间
        Date maxIdentifyTime = getMinOrMaxIdentifyTime(sourceId, false);
        if(maxIdentifyTime == null){
            return result;
        }
        result.setEndTime(maxIdentifyTime);

        ///最小时间
        Date minIdentifyTime = getMinOrMaxIdentifyTime(sourceId, true);
        if(minIdentifyTime == null){
            return result;
        }
        result.setStartTime(minIdentifyTime);

        //计算最大时间和最小时间的每隔N秒内的数据
        long maxIdentifyTimeLong = maxIdentifyTime.getTime();
        long minIdentifyTimeLong = minIdentifyTime.getTime();
        //差值
        long sub = maxIdentifyTimeLong - minIdentifyTimeLong;
        //计算循环次数，如果求余能够为0，次数刚好就是商；否则就需要商+1
        int loopNum = 0;
        if(sub / 1000 % times == 0){
            loopNum = (int)(sub / 1000 / times);
        } else {
            loopNum = (int)(sub / 1000 / times) + 1;
        }
        //返回值封装
        List<AgIdentifyMancarResultStatisticsDomain> numList = new ArrayList<>();
        Date queryDateStart = null;
        for(long i = 1; i <= loopNum; i++){
            //查询的开始时间，如果未赋值，就初始化赋值成最小值
            if(queryDateStart == null){
                queryDateStart = minIdentifyTime;
            }
            //查询的结束时间，是查询开始时间+间隔几秒
            Date queryDateEnd = new Date();
            long timeTime = queryDateStart.getTime();
            queryDateEnd.setTime(timeTime + (times * 1000));

            //统计查询结果
            AgIdentifyMancarResult mancarResult = identifyMancarResultCustomMapper.statisticsPeopleAndCar(sourceId, queryDateStart, queryDateEnd);
            if(mancarResult != null){
                //赋值
                AgIdentifyMancarResultStatisticsDomain domain = new AgIdentifyMancarResultStatisticsDomain();
                domain.setTotalNumPeople(mancarResult.getNumPeople());
                domain.setTotalNumCar(mancarResult.getNumCar());
                domain.setIdentifyTimeStart(queryDateStart);
                domain.setIdentifyTimeEnd(queryDateEnd);
                //最后一次的时间段时间是最大时间，但是查询的时候，不需要用此时间，此时间需要接口返回，保证最大时间一致
                if(i == loopNum){
                    domain.setIdentifyTimeEnd(maxIdentifyTime);
                }
                numList.add(domain);
            }
            //开始时间赋值成结束时间
            queryDateStart = queryDateEnd;
        }
        result.setList(numList);
        return result;
    }

    @Override
    public void exportExcel(String sourceId, String startTimeParam, String endTimeParam, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //查询视频信息
        AgIdentifyMancarSource mancarSource = identifyMancarSourceService.getById(sourceId);
        if(mancarSource == null){
            return ;
        }

        //查询参数封装
        AgIdentifyMancarResultExample example = new AgIdentifyMancarResultExample();
        example.setOrderByClause("identify_time asc");
        AgIdentifyMancarResultExample.Criteria criteria = example.createCriteria();
        criteria.andSourceIdEqualTo(sourceId);
        //时间格式验证
        if(!StringUtils.isEmpty(startTimeParam)){
            Date date = checkTimeFormat(startTimeParam);
            criteria.andIdentifyTimeGreaterThanOrEqualTo(date);
        }
        //时间格式验证
        if(!StringUtils.isEmpty(endTimeParam)){
            Date date = checkTimeFormat(endTimeParam);
            criteria.andIdentifyTimeLessThanOrEqualTo(date);
        }
        List<AgIdentifyMancarResult> result = identifyMancarResultMapper.selectByExample(example);

        if(result != null &&  result.size() > 0){
            List<List<String>> excelLists = new ArrayList<>();
            SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");

            //设置表头
            List<String> excelRowHeaders = new ArrayList<>();
            excelRowHeaders.add("时间");
            excelRowHeaders.add("人数");
            excelRowHeaders.add("车辆数");
            excelLists.add(excelRowHeaders);
            //设置内容
            for(AgIdentifyMancarResult re : result){
                List<String> excelRows = new ArrayList<>();
                excelRows.add(sf.format(re.getIdentifyTime()));
                excelRows.add(re.getNumPeople().toString());
                excelRows.add(re.getNumCar().toString());
                excelLists.add(excelRows);
            }
            String fileName = mancarSource.getName() + ".xls";
            fileName = FileUtil.encodeFileName(fileName, request);
            ExcelUtil.exportExcel(response, excelLists, "人、车流量识别报告",fileName, 15);
        }
    }
}
