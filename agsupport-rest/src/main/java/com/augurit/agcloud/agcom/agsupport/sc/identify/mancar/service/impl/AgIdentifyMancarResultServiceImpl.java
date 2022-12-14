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
        //??????????????????null???????????????????????????
        if(file != null && !file.isEmpty()){
            AgIdentifyMancarSource source = new AgIdentifyMancarSource();
            source.setId(param.getSourceId());
            source.setName("????????????" + param.getIdentifyTime());
            source.setStatus("1");//1?????????
            identifyMancarSourceService.add(source, file);
        }

        //??????????????????
        AgIdentifyMancarResult result = new AgIdentifyMancarResult();
        //???????????????
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
        //???????????????
        AgIdentifyMancarResultResult result = new AgIdentifyMancarResultResult();

        //??????????????????
        Date minIdentifyTime = getMinOrMaxIdentifyTime(sourceId, true);
        if(minIdentifyTime == null){
            return result;
        }
        result.setStartTime(minIdentifyTime);

        //??????????????????
        Date maxIdentifyTime = getMinOrMaxIdentifyTime(sourceId, false);
        if(maxIdentifyTime == null){
            return result;
        }
        result.setEndTime(maxIdentifyTime);

        //??????????????????
        //??????????????????
        AgIdentifyMancarResultExample example = new AgIdentifyMancarResultExample();
        example.setOrderByClause("identify_time asc");
        AgIdentifyMancarResultExample.Criteria criteria = example.createCriteria();
        criteria.andSourceIdEqualTo(sourceId);
        //??????????????????
        if(!StringUtils.isEmpty(startTimeParam)){
            Date date = checkTimeFormat(startTimeParam);
            criteria.andIdentifyTimeGreaterThanOrEqualTo(date);
        }
        //??????????????????
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
     * @tips: ?????????????????????????????????
     * @param sourceId
     * @param isMin true???????????????false????????????
     * @return
     */
    private Date getMinOrMaxIdentifyTime(String sourceId, boolean isMin){
        String orderByClause = "identify_time desc";
        if(isMin){
            orderByClause = "identify_time asc";
        }
        //??????????????????
        AgIdentifyMancarResultExample example = new AgIdentifyMancarResultExample();

        //???????????????????????????????????????,??????????????????
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
            throw new SourceException("????????????????????????????????????yyyy-MM-dd HH:mm:ss?????????");
        }
    }
    @Override
    public AgIdentifyMancarResultStatisticsResult statisticsPeopleAndCarNum(String sourceId, Integer times) {
        //???????????????
        AgIdentifyMancarResultStatisticsResult result = new AgIdentifyMancarResultStatisticsResult();
        //????????????
        Date maxIdentifyTime = getMinOrMaxIdentifyTime(sourceId, false);
        if(maxIdentifyTime == null){
            return result;
        }
        result.setEndTime(maxIdentifyTime);

        ///????????????
        Date minIdentifyTime = getMinOrMaxIdentifyTime(sourceId, true);
        if(minIdentifyTime == null){
            return result;
        }
        result.setStartTime(minIdentifyTime);

        //??????????????????????????????????????????N???????????????
        long maxIdentifyTimeLong = maxIdentifyTime.getTime();
        long minIdentifyTimeLong = minIdentifyTime.getTime();
        //??????
        long sub = maxIdentifyTimeLong - minIdentifyTimeLong;
        //??????????????????????????????????????????0?????????????????????????????????????????????+1
        int loopNum = 0;
        if(sub / 1000 % times == 0){
            loopNum = (int)(sub / 1000 / times);
        } else {
            loopNum = (int)(sub / 1000 / times) + 1;
        }
        //???????????????
        List<AgIdentifyMancarResultStatisticsDomain> numList = new ArrayList<>();
        Date queryDateStart = null;
        for(long i = 1; i <= loopNum; i++){
            //????????????????????????????????????????????????????????????????????????
            if(queryDateStart == null){
                queryDateStart = minIdentifyTime;
            }
            //?????????????????????????????????????????????+????????????
            Date queryDateEnd = new Date();
            long timeTime = queryDateStart.getTime();
            queryDateEnd.setTime(timeTime + (times * 1000));

            //??????????????????
            AgIdentifyMancarResult mancarResult = identifyMancarResultCustomMapper.statisticsPeopleAndCar(sourceId, queryDateStart, queryDateEnd);
            if(mancarResult != null){
                //??????
                AgIdentifyMancarResultStatisticsDomain domain = new AgIdentifyMancarResultStatisticsDomain();
                domain.setTotalNumPeople(mancarResult.getNumPeople());
                domain.setTotalNumCar(mancarResult.getNumCar());
                domain.setIdentifyTimeStart(queryDateStart);
                domain.setIdentifyTimeEnd(queryDateEnd);
                //??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                if(i == loopNum){
                    domain.setIdentifyTimeEnd(maxIdentifyTime);
                }
                numList.add(domain);
            }
            //?????????????????????????????????
            queryDateStart = queryDateEnd;
        }
        result.setList(numList);
        return result;
    }

    @Override
    public void exportExcel(String sourceId, String startTimeParam, String endTimeParam, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //??????????????????
        AgIdentifyMancarSource mancarSource = identifyMancarSourceService.getById(sourceId);
        if(mancarSource == null){
            return ;
        }

        //??????????????????
        AgIdentifyMancarResultExample example = new AgIdentifyMancarResultExample();
        example.setOrderByClause("identify_time asc");
        AgIdentifyMancarResultExample.Criteria criteria = example.createCriteria();
        criteria.andSourceIdEqualTo(sourceId);
        //??????????????????
        if(!StringUtils.isEmpty(startTimeParam)){
            Date date = checkTimeFormat(startTimeParam);
            criteria.andIdentifyTimeGreaterThanOrEqualTo(date);
        }
        //??????????????????
        if(!StringUtils.isEmpty(endTimeParam)){
            Date date = checkTimeFormat(endTimeParam);
            criteria.andIdentifyTimeLessThanOrEqualTo(date);
        }
        List<AgIdentifyMancarResult> result = identifyMancarResultMapper.selectByExample(example);

        if(result != null &&  result.size() > 0){
            List<List<String>> excelLists = new ArrayList<>();
            SimpleDateFormat sf = new SimpleDateFormat("yyyy???MM???dd??? HH:mm:ss");

            //????????????
            List<String> excelRowHeaders = new ArrayList<>();
            excelRowHeaders.add("??????");
            excelRowHeaders.add("??????");
            excelRowHeaders.add("?????????");
            excelLists.add(excelRowHeaders);
            //????????????
            for(AgIdentifyMancarResult re : result){
                List<String> excelRows = new ArrayList<>();
                excelRows.add(sf.format(re.getIdentifyTime()));
                excelRows.add(re.getNumPeople().toString());
                excelRows.add(re.getNumCar().toString());
                excelLists.add(excelRows);
            }
            String fileName = mancarSource.getName() + ".xls";
            fileName = FileUtil.encodeFileName(fileName, request);
            ExcelUtil.exportExcel(response, excelLists, "???????????????????????????",fileName, 15);
        }
    }
}
