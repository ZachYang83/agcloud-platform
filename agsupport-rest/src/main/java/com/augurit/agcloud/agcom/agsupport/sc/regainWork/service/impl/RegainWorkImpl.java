package com.augurit.agcloud.agcom.agsupport.sc.regainWork.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.agcom.agsupport.sc.dir.controller.AgDirController;
import com.augurit.agcloud.agcom.agsupport.sc.regainWork.service.IRegainWork;
import com.augurit.agcloud.agcom.agsupport.sc.regainWork.service.Project;
import com.augurit.agcloud.agcom.agsupport.util.JdbcSqlServerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RegainWorkImpl implements IRegainWork {
    private static Logger logger = LoggerFactory.getLogger(RegainWorkImpl.class);
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    private static String connectUrlFor202="jdbc:sqlserver://10.194.68.202:1433;DatabaseName=GZCC_ZHJG";
    private static String connectUser = "cimsys_user" ;
    private static String connectPassword="cimsys_user,2020";

    //获取指定日期及该日期前复工项目列表
    public JSONObject getProjectBasicInfo(String gcbm) throws Exception{
        StringBuilder sb = new StringBuilder(500);
        sb.append("SELECT *");
        sb.append(" FROM datacenter.dbo.SP_V_SGXK_GS_GCXX") ;
        sb.append(" WHERE SGXKGCBM ='"+gcbm+"'");
        JSONArray jsonArray = getDetailBySql(sb.toString());
        return jsonArray.getJSONObject(0);
    }

    @Override
    public Map<String, List<Project>> getRegainedSZProject() throws Exception {
        Map<String, List<Project>> mapResult=null;
        String regainedSZProjectString = stringRedisTemplate.opsForValue().get("RegainedSZProject");
        if(regainedSZProjectString!=null&&!regainedSZProjectString.isEmpty()){
            mapResult = (Map<String, List<Project>>)JSON.parse(regainedSZProjectString);
        }
        else {
            StringBuilder sb = new StringBuilder(500);
            sb.append("SELECT z.XZB,z.YZB,isnull(x.name,'其他') as SZQX,isnull((CASE WHEN e.szqx is null then x.name ELSE e.szqx END),'其他') as SZQX1,isnull(x.JSLB,'房建') as JSLB,d.CODE,d.NAME,1 as STATUS,d.*");
            sb.append(" FROM  DataCenter.dbo.ZA_V_LZ_SJPAGCXX d left join [10.194.68.212].[M_GC].[dbo].[Y_GCSP_GCZB] z on d.code=z.GCBM");
            sb.append(" LEFT JOIN (select a.jslb,c.name,a.GCBH from [dbo].[BJ_GCXX] a  join [dbo].[BJ_XMXX] b on a.XMBH=b.XMBH  LEFT JOIN v_dict_item c on b.lxsd=c.code and c.lx='LXSD')x on d.code=x.GCBH");
            sb.append(" LEFT JOIN jdxt_bj_gcbljg f on   x.GCBH=f.GcdxbmT and isnull(GcStatus,'')<>'已完工未作安全评价' and isnull(GcStatus,'')<>'已作竣工安全评价'  and  bjlx=0");
            sb.append(" LEFT JOIN [dbo].[jdxt_jdjg] e on f.[Bljg]=e.[bljg] ");
            sb.append(" WHERE sfscfgba='是' and CHARINDEX('市政',ISNULL(x.JSLB,''))>0");
            List<Project> listProject = getProjectBySql(sb.toString());
            mapResult = listProject.stream().collect(Collectors.groupingBy(Project::getSzqx1));
        }
        return mapResult;

    }

    @Override
    public Map<String, List<Project>> getRegainedFJProject() throws Exception {
        Map<String, List<Project>> mapResult=null;
        String regainedFJProjectString = stringRedisTemplate.opsForValue().get("RegainedFJProject");
        if(regainedFJProjectString!=null&&!regainedFJProjectString.isEmpty()){
            mapResult = (Map<String, List<Project>>)JSON.parse(regainedFJProjectString);
        }
        else {
            StringBuilder sb = new StringBuilder(500);
            sb.append("SELECT z.XZB,z.YZB,isnull(x.name,'其他') as SZQX,isnull((CASE WHEN e.szqx is null then x.name ELSE e.szqx END),'其他') as SZQX1,isnull(x.JSLB,'房建') as JSLB,d.CODE,d.NAME,1 as STATUS,d.*");
            sb.append(" FROM  DataCenter.dbo.ZA_V_LZ_SJPAGCXX d left join [10.194.68.212].[M_GC].[dbo].[Y_GCSP_GCZB] z on d.code=z.GCBM");
            sb.append(" LEFT JOIN (select a.jslb,c.name,a.GCBH from [dbo].[BJ_GCXX] a  join [dbo].[BJ_XMXX] b on a.XMBH=b.XMBH  LEFT JOIN v_dict_item c on b.lxsd=c.code and c.lx='LXSD')x on d.code=x.GCBH");
            sb.append(" LEFT JOIN jdxt_bj_gcbljg f on   x.GCBH=f.GcdxbmT and isnull(GcStatus,'')<>'已完工未作安全评价' and isnull(GcStatus,'')<>'已作竣工安全评价'  and  bjlx=0");
            sb.append(" LEFT JOIN [dbo].[jdxt_jdjg] e on f.[Bljg]=e.[bljg] ");
            sb.append(" WHERE sfscfgba='是' and CHARINDEX('市政',ISNULL(x.JSLB,''))=0");
            List<Project> listProject = getProjectBySql(sb.toString());
            mapResult = listProject.stream().collect(Collectors.groupingBy(Project::getSzqx1));
        }
        return mapResult;
    }

    public List<Project> getRegainedProject() throws Exception {
        List<Project> listProject=null;
        String regainedProjectString = stringRedisTemplate.opsForValue().get("RegainedProject");
        if(regainedProjectString!=null&&!regainedProjectString.isEmpty()){
            listProject = (List<Project>)JSON.parse(regainedProjectString);
        }
        else {
            StringBuilder sb = new StringBuilder(500);
            sb.append("SELECT 0 as XZB,0 as YZB,isnull(x.name,'其他') as SZQX,isnull((CASE WHEN e.szqx is null then x.name ELSE e.szqx END),'其他') as SZQX1,isnull(x.JSLB,'房建') as JSLB,d.CODE,d.NAME,1 as STATUS,d.*");
            sb.append(" FROM  DataCenter.dbo.ZA_V_LZ_SJPAGCXX d");
            sb.append(" LEFT JOIN (select a.jslb,c.name,a.GCBH from [dbo].[BJ_GCXX] a  join [dbo].[BJ_XMXX] b on a.XMBH=b.XMBH  LEFT JOIN v_dict_item c on b.lxsd=c.code and c.lx='LXSD')x on d.code=x.GCBH");
            sb.append(" LEFT JOIN jdxt_bj_gcbljg f on   x.GCBH=f.GcdxbmT and isnull(GcStatus,'')<>'已完工未作安全评价' and isnull(GcStatus,'')<>'已作竣工安全评价'  and  bjlx=0");
            sb.append(" LEFT JOIN [dbo].[jdxt_jdjg] e on f.[Bljg]=e.[bljg] ");
            sb.append(" WHERE sfscfgba='是'");
            listProject = getProjectBySql(sb.toString());
        }
        return listProject;
    }

    @Override
    public Map<String, List<Project>> getPlanRegainSZProject(String regainDate) throws Exception {
        Map<String, List<Project>> mapResult=null;
        String planRegainSZProjectString = stringRedisTemplate.opsForValue().get("PlanRegainSZProject");
        if(planRegainSZProjectString!=null&&!planRegainSZProjectString.isEmpty()){
            mapResult = (Map<String, List<Project>>)JSON.parse(planRegainSZProjectString);
        }
        else {
            StringBuilder sb = new StringBuilder(500);
            sb.append("SELECT z.XZB,z.YZB,isnull(x.name,'其他') as SZQX,isnull((CASE WHEN e.szqx is null then x.name ELSE e.szqx END),'其他') as SZQX1,isnull(x.JSLB,'房建') as JSLB,d.CODE,d.NAME,1 as STATUS,d.*");
            sb.append(" FROM  DataCenter.dbo.ZA_V_LZ_SJPAGCXX d left join [10.194.68.212].[M_GC].[dbo].[Y_GCSP_GCZB] z on d.code=z.GCBM");
            sb.append(" LEFT JOIN (select a.jslb,c.name,a.GCBH from [dbo].[BJ_GCXX] a  join [dbo].[BJ_XMXX] b on a.XMBH=b.XMBH  LEFT JOIN v_dict_item c on b.lxsd=c.code and c.lx='LXSD')x on d.code=x.GCBH");
            sb.append(" LEFT JOIN jdxt_bj_gcbljg f on   x.GCBH=f.GcdxbmT and isnull(GcStatus,'')<>'已完工未作安全评价' and isnull(GcStatus,'')<>'已作竣工安全评价'  and  bjlx=0");
            sb.append(" LEFT JOIN [dbo].[jdxt_jdjg] e on f.[Bljg]=e.[bljg] ");
            sb.append(" WHERE sfscfgba='否' and DATEDIFF(day,'" + regainDate + "', nkgrq ) >0 and CHARINDEX('市政',ISNULL(x.JSLB,''))>0");
            List<Project> listProject = getProjectBySql(sb.toString());
            mapResult = listProject.stream().collect(Collectors.groupingBy(Project::getSzqx1));
        }
        return mapResult;
    }

    @Override
    public Map<String, List<Project>> getPlanRegainFJProject(String regainDate) throws Exception {
        Map<String, List<Project>> mapResult=null;
        String planRegainFJProjectString = stringRedisTemplate.opsForValue().get("PlanRegainFJProject");
        if(planRegainFJProjectString!=null&&!planRegainFJProjectString.isEmpty()){
            mapResult = (Map<String, List<Project>>)JSON.parse(planRegainFJProjectString);
        }
        else {
            StringBuilder sb = new StringBuilder(500);
            sb.append("SELECT z.XZB,z.YZB,isnull(x.name,'其他') as SZQX,isnull((CASE WHEN e.szqx is null then x.name ELSE e.szqx END),'其他') as SZQX1,isnull(x.JSLB,'房建') as JSLB,d.CODE,d.NAME,1 as STATUS,d.*");
            sb.append(" FROM  DataCenter.dbo.ZA_V_LZ_SJPAGCXX d left join [10.194.68.212].[M_GC].[dbo].[Y_GCSP_GCZB] z on d.code=z.GCBM");
            sb.append(" LEFT JOIN (select a.jslb,c.name,a.GCBH from [dbo].[BJ_GCXX] a  join [dbo].[BJ_XMXX] b on a.XMBH=b.XMBH  LEFT JOIN v_dict_item c on b.lxsd=c.code and c.lx='LXSD')x on d.code=x.GCBH");
            sb.append(" LEFT JOIN jdxt_bj_gcbljg f on   x.GCBH=f.GcdxbmT and isnull(GcStatus,'')<>'已完工未作安全评价' and isnull(GcStatus,'')<>'已作竣工安全评价'  and  bjlx=0");
            sb.append(" LEFT JOIN [dbo].[jdxt_jdjg] e on f.[Bljg]=e.[bljg] ");
            sb.append(" WHERE sfscfgba='否' and DATEDIFF(day,'" + regainDate + "', nkgrq ) >0 and CHARINDEX('市政',ISNULL(x.JSLB,''))=0");
            List<Project> listProject = getProjectBySql(sb.toString());
            mapResult = listProject.stream().collect(Collectors.groupingBy(Project::getSzqx1));
        }
        return mapResult;
    }

    public List<Project> getPlanRegainProject(String regainDate) throws Exception{
        List<Project> listProject=null;
        String planRegainProjectString = stringRedisTemplate.opsForValue().get("PlanRegainProject");
        if(planRegainProjectString!=null&&!planRegainProjectString.isEmpty()){
            listProject = (List<Project>)JSON.parse(planRegainProjectString);
        }
        else {
            StringBuilder sb = new StringBuilder(500);
            sb.append("SELECT 0 as XZB,0 as YZB,isnull(x.name,'其他') as SZQX,isnull((CASE WHEN e.szqx is null then x.name ELSE e.szqx END),'其他') as SZQX1,isnull(x.JSLB,'房建') as JSLB,d.CODE,d.NAME,1 as STATUS,d.*");
            sb.append(" FROM  DataCenter.dbo.ZA_V_LZ_SJPAGCXX d");
            sb.append(" LEFT JOIN (select a.jslb,c.name,a.GCBH from [dbo].[BJ_GCXX] a  join [dbo].[BJ_XMXX] b on a.XMBH=b.XMBH  LEFT JOIN v_dict_item c on b.lxsd=c.code and c.lx='LXSD')x on d.code=x.GCBH");
            sb.append(" LEFT JOIN jdxt_bj_gcbljg f on   x.GCBH=f.GcdxbmT and isnull(GcStatus,'')<>'已完工未作安全评价' and isnull(GcStatus,'')<>'已作竣工安全评价'  and  bjlx=0");
            sb.append(" LEFT JOIN [dbo].[jdxt_jdjg] e on f.[Bljg]=e.[bljg] ");
            sb.append(" WHERE sfscfgba='否' and DATEDIFF(day,'" + regainDate + "', nkgrq ) >0");
            listProject = getProjectBySql(sb.toString());
        }
        return listProject;
    }

    @Override
    public Map<String, List<Project>> getNoPlanRegainSZProject() throws Exception {
        Map<String, List<Project>> mapResult=null;
        String noPlanRegainSZProjectString = stringRedisTemplate.opsForValue().get("NoPlanRegainSZProject");
        if(noPlanRegainSZProjectString!=null&&!noPlanRegainSZProjectString.isEmpty()){
            mapResult = (Map<String, List<Project>>)JSON.parse(noPlanRegainSZProjectString);
        }
        else {
            StringBuilder sb = new StringBuilder(500);
            sb.append("select isnull(c.name,'其他') as SZQX,isnull((CASE WHEN e.szqx is null then c.name ELSE e.szqx END),'其他') as SZQX1,isnull(a.JSLB,'房建') as JSLB,d.GcdxbmT as CODE,d.GCMC as NAME,5 as STATUS");
            sb.append(" FROM [dbo].[BJ_GCXX] a  join [dbo].[BJ_XMXX] b on a.XMBH=b.XMBH join jdxt_bj_gcbljg d on d.GcdxbmT=a.GCBH left join v_dict_item c on b.lxsd=c.code and c.lx='LXSD' left join [dbo].[jdxt_jdjg] e on d.[Bljg]=e.[bljg]");
            sb.append(" WHERE isnull(GcStatus,'')<>'已完工未作安全评价' and isnull(GcStatus,'')<>'已作竣工安全评价'  and  d.bjlx=0 and CHARINDEX('市政',ISNULL(a.JSLB,''))>0");
            sb.append(" AND d.GcdxbmT not in (select d.code  FROM  DataCenter.dbo.ZA_V_LZ_SJPAGCXX d LEFT JOIN (select a.jslb,c.name,a.GCBH from [dbo].[BJ_GCXX] a ");
            sb.append(" join [dbo].[BJ_XMXX] b on a.XMBH=b.XMBH  LEFT JOIN v_dict_item c on b.lxsd=c.code and c.lx='LXSD')x on d.code=x.GCBH");
            sb.append(" LEFT JOIN jdxt_bj_gcbljg f on   x.GCBH=f.GcdxbmT and isnull(GcStatus,'')<>'已完工未作安全评价' and isnull(GcStatus,'')<>'已作竣工安全评价'  and  bjlx=0");
            sb.append(" LEFT JOIN [dbo].[jdxt_jdjg] e on f.[Bljg]=e.[bljg]");
            sb.append(" WHERE sfscfgba='是' and d.code is not null)");
            sb.append(" AND d.GcdxbmT not in (select d.code  FROM  DataCenter.dbo.ZA_V_LZ_SJPAGCXX d LEFT JOIN (select a.jslb,c.name,a.GCBH from [dbo].[BJ_GCXX] a ");
            sb.append(" join [dbo].[BJ_XMXX] b on a.XMBH=b.XMBH  LEFT JOIN v_dict_item c on b.lxsd=c.code and c.lx='LXSD')x on d.code=x.GCBH");
            sb.append(" LEFT JOIN jdxt_bj_gcbljg f on   x.GCBH=f.GcdxbmT and isnull(GcStatus,'')<>'已完工未作安全评价' and isnull(GcStatus,'')<>'已作竣工安全评价'  and  bjlx=0");
            sb.append(" LEFT JOIN [dbo].[jdxt_jdjg] e on f.[Bljg]=e.[bljg]");
            sb.append(" WHERE sfscfgba='否' and DATEDIFF(day,getdate(), nkgrq ) >0 and d.code is not null)");
            List<Project> listProject = getProjectBySql(sb.toString());
            mapResult = listProject.stream().collect(Collectors.groupingBy(Project::getSzqx1));
        }
        return mapResult;
    }

    @Override
    public Map<String, List<Project>> getNoPlanRegainFJProject() throws Exception {
        Map<String, List<Project>> mapResult=null;
        String noPlanRegainFJProjectString = stringRedisTemplate.opsForValue().get("NoPlanRegainFJProject");
        if(noPlanRegainFJProjectString!=null&&!noPlanRegainFJProjectString.isEmpty()){
            mapResult = (Map<String, List<Project>>)JSON.parse(noPlanRegainFJProjectString);
        }
        else {
            StringBuilder sb = new StringBuilder(500);
            sb.append("select isnull(c.name,'其他') as SZQX,isnull((CASE WHEN e.szqx is null then c.name ELSE e.szqx END),'其他') as SZQX1,isnull(a.JSLB,'房建') as JSLB,d.GcdxbmT as CODE,d.GCMC as NAME,5 as STATUS");
            sb.append(" FROM [dbo].[BJ_GCXX] a  join [dbo].[BJ_XMXX] b on a.XMBH=b.XMBH join jdxt_bj_gcbljg d on d.GcdxbmT=a.GCBH left join v_dict_item c on b.lxsd=c.code and c.lx='LXSD' left join [dbo].[jdxt_jdjg] e on d.[Bljg]=e.[bljg]");
            sb.append(" WHERE isnull(GcStatus,'')<>'已完工未作安全评价' and isnull(GcStatus,'')<>'已作竣工安全评价'  and  d.bjlx=0 and CHARINDEX('市政',ISNULL(a.JSLB,''))=0");
            sb.append(" AND d.GcdxbmT not in (select d.code  FROM  DataCenter.dbo.ZA_V_LZ_SJPAGCXX d LEFT JOIN (select a.jslb,c.name,a.GCBH from [dbo].[BJ_GCXX] a ");
            sb.append(" join [dbo].[BJ_XMXX] b on a.XMBH=b.XMBH  LEFT JOIN v_dict_item c on b.lxsd=c.code and c.lx='LXSD')x on d.code=x.GCBH");
            sb.append(" LEFT JOIN jdxt_bj_gcbljg f on   x.GCBH=f.GcdxbmT and isnull(GcStatus,'')<>'已完工未作安全评价' and isnull(GcStatus,'')<>'已作竣工安全评价'  and  bjlx=0");
            sb.append(" LEFT JOIN [dbo].[jdxt_jdjg] e on f.[Bljg]=e.[bljg]");
            sb.append(" WHERE sfscfgba='是' and d.code is not null)");
            sb.append(" AND d.GcdxbmT not in (select d.code  FROM  DataCenter.dbo.ZA_V_LZ_SJPAGCXX d LEFT JOIN (select a.jslb,c.name,a.GCBH from [dbo].[BJ_GCXX] a ");
            sb.append(" join [dbo].[BJ_XMXX] b on a.XMBH=b.XMBH  LEFT JOIN v_dict_item c on b.lxsd=c.code and c.lx='LXSD')x on d.code=x.GCBH");
            sb.append(" LEFT JOIN jdxt_bj_gcbljg f on   x.GCBH=f.GcdxbmT and isnull(GcStatus,'')<>'已完工未作安全评价' and isnull(GcStatus,'')<>'已作竣工安全评价'  and  bjlx=0");
            sb.append(" LEFT JOIN [dbo].[jdxt_jdjg] e on f.[Bljg]=e.[bljg]");
            sb.append(" WHERE sfscfgba='否' and DATEDIFF(day,getdate(), nkgrq ) >0 and d.code is not null)");
            List<Project> listProject = getProjectBySql(sb.toString());
            mapResult = listProject.stream().collect(Collectors.groupingBy(Project::getSzqx1));
        }
        return mapResult;
    }

    public List<Project> getNoPlanRegainProject() throws Exception {
        List<Project> listProject=null;
        String noPlanRegainProjectString = stringRedisTemplate.opsForValue().get("NoPlanRegainProject");
        if(noPlanRegainProjectString!=null&&!noPlanRegainProjectString.isEmpty()){
            listProject = (List<Project>)JSON.parse(noPlanRegainProjectString);
        }
        else {
            StringBuilder sb = new StringBuilder(500);
            sb.append("select isnull(c.name,'其他') as SZQX,isnull((CASE WHEN e.szqx is null then c.name ELSE e.szqx END),'其他') as SZQX1,isnull(a.JSLB,'房建') as JSLB,d.GcdxbmT as CODE,d.GCMC as NAME,5 as STATUS");
            sb.append(" FROM [dbo].[BJ_GCXX] a  join [dbo].[BJ_XMXX] b on a.XMBH=b.XMBH join jdxt_bj_gcbljg d on d.GcdxbmT=a.GCBH left join v_dict_item c on b.lxsd=c.code and c.lx='LXSD' left join [dbo].[jdxt_jdjg] e on d.[Bljg]=e.[bljg]");
            sb.append(" WHERE isnull(GcStatus,'')<>'已完工未作安全评价' and isnull(GcStatus,'')<>'已作竣工安全评价'  and  d.bjlx=0 ");
            sb.append(" AND d.GcdxbmT not in (select d.code  FROM  DataCenter.dbo.ZA_V_LZ_SJPAGCXX d LEFT JOIN (select a.jslb,c.name,a.GCBH from [dbo].[BJ_GCXX] a ");
            sb.append(" join [dbo].[BJ_XMXX] b on a.XMBH=b.XMBH  LEFT JOIN v_dict_item c on b.lxsd=c.code and c.lx='LXSD')x on d.code=x.GCBH");
            sb.append(" LEFT JOIN jdxt_bj_gcbljg f on   x.GCBH=f.GcdxbmT and isnull(GcStatus,'')<>'已完工未作安全评价' and isnull(GcStatus,'')<>'已作竣工安全评价'  and  bjlx=0");
            sb.append(" LEFT JOIN [dbo].[jdxt_jdjg] e on f.[Bljg]=e.[bljg]");
            sb.append(" WHERE sfscfgba='是' and d.code is not null)");
            sb.append(" AND d.GcdxbmT not in (select d.code  FROM  DataCenter.dbo.ZA_V_LZ_SJPAGCXX d LEFT JOIN (select a.jslb,c.name,a.GCBH from [dbo].[BJ_GCXX] a ");
            sb.append(" join [dbo].[BJ_XMXX] b on a.XMBH=b.XMBH  LEFT JOIN v_dict_item c on b.lxsd=c.code and c.lx='LXSD')x on d.code=x.GCBH");
            sb.append(" LEFT JOIN jdxt_bj_gcbljg f on   x.GCBH=f.GcdxbmT and isnull(GcStatus,'')<>'已完工未作安全评价' and isnull(GcStatus,'')<>'已作竣工安全评价'  and  bjlx=0");
            sb.append(" LEFT JOIN [dbo].[jdxt_jdjg] e on f.[Bljg]=e.[bljg]");
            sb.append(" WHERE sfscfgba='否' and DATEDIFF(day,getdate(), nkgrq ) >0 and d.code is not null)");
            listProject = getProjectBySql(sb.toString());
        }
        return listProject;
    }

    private List<Project> getProjectBySql(String sql) throws Exception{
        Connection conn=null;
        Statement st=null;
        ResultSet rs=null;
        List<Project> listProject = new ArrayList<>();
        Map<String, List<Project>> map;
        try {

            //建立连接
            conn = JdbcSqlServerUtil.getConnection(connectUrlFor202,connectUser,connectPassword);
            //创建statment，跟数据库打交道的对象
            st = conn.createStatement();
            //执行查询，得到结果集
            rs = st.executeQuery(sql);
            while(rs.next()) {
                Project project = new Project();
                project.setGcbm(rs.getString("CODE"));
                project.setGcmc(rs.getString("NAME"));
                project.setJslb(rs.getString("JSLB"));
                project.setSzqx(rs.getString("SZQX"));
                project.setSzqx1(rs.getString("SZQX1"));
                int status = rs.getInt("STATUS");
                if(status!=5){//status=5是查询未有复工计划的项目，这些项目不需要提供坐标
                    project.setXzb(rs.getFloat("XZB"));
                    project.setYzb(rs.getFloat("YZB"));
                    project.setXmzt(rs.getString("XMZT"));//项目状态
                    project.setNkgrq(rs.getString("NKGRQ"));//拟开工日期
                    project.setYqfzr(rs.getString("YQFZR"));//疫情负责人
                    project.setYqfzrsjhm(rs.getString("YQFZRSJHM"));//疫情负责人手机号码
                    project.setGdzrys(rs.getString("GDZRYS"));//工地总人数
                    project.setXcglrys(rs.getString("XCGLRYS"));//现场管理人员数
                    project.setYxzyrys(rs.getString("YXZYRYS"));//一线作业人员数
                    project.setYfgzrs(rs.getString("YFGZRS"));//已返岗总人数
                    project.setYqzblrs(rs.getString("YQZBLRS"));//已确诊病例人数
                    project.setYjcqzysblrs(rs.getString("YJCQZYSBLRS"));//有接触确诊、疑似病例人数
                    String yfghbjhwzjrs = rs.getString("YFGHBJHWZJRS");//已返岗湖北籍或温州籍人数
                    String ylxyyxchbhwzfycyrs = rs.getString("YLXYYXCHBHWZFYCYRS");//已联系有意向从湖北或温州返粤从业人数
                    String ysblrs = rs.getString("YSBLRS");//疑似病例人数
                    int intYfghbjhwzjrs = 0;
                    int intYlxyyxchbhwzfycyrs = 0;
                    int intYsblrs = 0;
                    if(yfghbjhwzjrs!=null && !yfghbjhwzjrs.isEmpty()){
                        intYfghbjhwzjrs = Integer.parseInt(yfghbjhwzjrs);
                    }
                    if(ylxyyxchbhwzfycyrs!=null && !ylxyyxchbhwzfycyrs.isEmpty()){
                        intYlxyyxchbhwzfycyrs = Integer.parseInt(ylxyyxchbhwzfycyrs);
                    }
                    if(ysblrs!=null && !ysblrs.isEmpty()){
                        intYsblrs = Integer.parseInt(ysblrs);
                    }
                    project.setYfghbjhwzjrs(yfghbjhwzjrs);
                    project.setYlxyyxchbhwzfycyrs(ylxyyxchbhwzfycyrs);
                    project.setYsblrs(ysblrs);
                    if(intYfghbjhwzjrs>0||intYlxyyxchbhwzfycyrs>0||intYsblrs>0){
                        status+=1;
                    }
                }
                project.setStatus(status);
                listProject.add(project);
            }

        } finally {
            JdbcSqlServerUtil.release(conn, st, rs);
        }
        return listProject;
    }




    //根据项目编号获取项目人员详细信息
    public JSONArray getWorkerInfo(String gcbm) throws Exception {
        StringBuilder sb = new StringBuilder(500);
        sb.append("SELECT b.*");
        sb.append(" FROM  datacenter.dbo.[ZA_V_LZ_SJPAGCXX] a JOIN datacenter.dbo.[ZA_V_LZ_SJPARYXX] b on a.projid=b.projid");
        sb.append(" WHERE a.code ='"+gcbm+"'");
        return getDetailBySql(sb.toString());
    }

    //根据项目编号获取建设单位详细信息
    public JSONObject getProjectJSDW(String gcbm) throws Exception {
        StringBuilder sb = new StringBuilder(500);
        sb.append("SELECT *");
        sb.append(" FROM  datacenter.dbo.[SP_V_SGXK_GS_JSDW]");
        sb.append(" WHERE GCBM ='"+gcbm+"'");
        JSONArray jsonArray = getDetailBySql(sb.toString());
        return jsonArray.getJSONObject(0);
    }

    //根据项目编号获取监理单位详细信息
    public JSONObject getProjectJLDW(String gcbm) throws Exception {
        StringBuilder sb = new StringBuilder(500);
        sb.append("SELECT *");
        sb.append(" FROM  datacenter.dbo.[SP_V_SGXK_GS_JLDW]");
        sb.append(" WHERE GCBM ='"+gcbm+"'");
        JSONArray jsonArray = getDetailBySql(sb.toString());
        return jsonArray.getJSONObject(0);
    }

    @Override
    public JSONObject getProjectSGDW(String gcbm) throws Exception {
        StringBuilder sb = new StringBuilder(500);
        sb.append("SELECT *");
        sb.append(" FROM  datacenter.dbo.[SP_V_SGXK_GS_SGDW]");
        sb.append(" WHERE GCBM ='"+gcbm+"'");
        JSONArray jsonArray = getDetailBySql(sb.toString());
        return jsonArray.getJSONObject(0);
    }

    @Override
    public JSONObject getStatisticsOfCity() throws Exception {
        StringBuilder sb = new StringBuilder(500);
        sb.append("SELECT SUM(GCZS) as GCZS");
        sb.append(",SUM(YFGZS) as YFGZS");
        sb.append(",SUM(YFGZS)*100/SUM(GCZS) as FGL");
        sb.append(",SUM(GDZRS) as GDZRS");
        sb.append(",SUM(YSBZRS) as YSBZRS");
        sb.append(",SUM(YFGZRS) as YFGZRS");
        sb.append(",SUM(YLXYYXCHBHWZFYCYRS) as YLXYYXCHBHWZFYCYRS");
        sb.append(",SUM(YQZBLRS) as YQZBLRS");
        sb.append(",SUM(YSBLRS) as YSBLRS");
        sb.append(",SUM(YJCQZYSBLRS) as YJCQZYSBLRS");
        sb.append(",SUM(YFGHBJHWZJRS) as YFGHBJHWZJRS");
        sb.append(" FROM  YQDT_XMTJ");
        sb.append(" WHERE LB='部门'");
        JSONArray jsonArray = getDetailBySql(sb.toString());
        return jsonArray.getJSONObject(0);
    }

    @Override
    public JSONArray getStatisticsByBM() throws Exception {
        StringBuilder sb = new StringBuilder(500);
        sb.append("SELECT *");
        sb.append(" FROM  YQDT_XMTJ");
        sb.append(" WHERE LB='部门'");
        JSONArray jsonArray = getDetailBySql(sb.toString());
        return jsonArray;
    }

    @Override
    public JSONArray getStatisticsByDQ() throws Exception {
        StringBuilder sb = new StringBuilder(500);
        sb.append("SELECT *");
        sb.append(" FROM  YQDT_XMTJ");
        sb.append(" WHERE LB='地区'");
        JSONArray jsonArray = getDetailBySql(sb.toString());
        return jsonArray;
    }

    @Override
    public JSONArray getStatisticsByXMLX() throws Exception {
        StringBuilder sb = new StringBuilder(500);
        sb.append("SELECT *");
        sb.append(" FROM  YQDT_XMTJ");
        sb.append(" WHERE LB='项目类型'");
        JSONArray jsonArray = getDetailBySql(sb.toString());
        return jsonArray;
    }

    @Override
    public JSONArray getStatisticsOfZDXM() throws Exception {
        StringBuilder sb = new StringBuilder(500);
        sb.append("SELECT *");
        sb.append(" FROM  YQDT_ZDXMFGTJ");
        JSONArray jsonArray = getDetailBySql(sb.toString());
        return jsonArray;
    }

    @Override
    public JSONArray getZDXMQD() throws Exception {
        StringBuilder sb = new StringBuilder(500);
        sb.append("SELECT *");
        sb.append(" FROM  YQDT_ZDQY");
        JSONArray jsonArray = getDetailBySql(sb.toString());
        return jsonArray;
    }

    @Override
    public JSONArray getZDXMOfBF() throws Exception {
        StringBuilder sb = new StringBuilder(500);
        sb.append("SELECT *");
        sb.append(" FROM  FJ_ZDXM");
        JSONArray jsonArray = getDetailBySql(sb.toString());
        return jsonArray;
    }

    @Override
    public JSONArray getCSGXOfBF() throws Exception {
        StringBuilder sb = new StringBuilder(500);
        sb.append("SELECT *");
        sb.append(" FROM  FJ_CSGX");
        JSONArray jsonArray = getDetailBySql(sb.toString());
        return jsonArray;
    }

    @Override
    public JSONArray getSJZYOfBF() throws Exception {
        StringBuilder sb = new StringBuilder(500);
        sb.append("SELECT *");
        sb.append(" FROM  FJ_SJZY");
        JSONArray jsonArray = getDetailBySql(sb.toString());
        return jsonArray;
    }

    @Override
    public JSONArray getFDCKFOfBF() throws Exception {
        StringBuilder sb = new StringBuilder(500);
        sb.append("SELECT *");
        sb.append(" FROM  FJ_FDCKF");
        JSONArray jsonArray = getDetailBySql(sb.toString());
        return jsonArray;
    }

    @Override
    public JSONArray getStatisticsOfQS() throws Exception {
        StringBuilder sb = new StringBuilder(500);
        sb.append("SELECT RQ,BM,FGZS");
        sb.append(" FROM  YQDT_FGXMRTJ");
        sb.append(" ORDER BY RQ");
        JSONArray jsonArray = getDetailBySql(sb.toString());
        return jsonArray;
    }

    @Override
    public JSONArray getStatisticsOfCXQY() throws Exception {
        StringBuilder sb = new StringBuilder(500);
        sb.append("SELECT *");
        sb.append(" FROM  YQDT_CXQYXMTJ");
        JSONArray jsonArray = getDetailBySql(sb.toString());
        return jsonArray;
    }

    @Override
    public JSONArray getXMQDOfCXQY() throws Exception {
        StringBuilder sb = new StringBuilder(500);
        sb.append("SELECT *");
        sb.append(" FROM  YQDT_CXQYXMQD");
        JSONArray jsonArray = getDetailBySql(sb.toString());
        return jsonArray;
    }

    @Override
    public JSONArray getYQDT_XMQD() throws Exception {
        StringBuilder sb = new StringBuilder(500);
        sb.append("SELECT *,(CASE WHEN SFSCFGBA='是' AND (YFGHBJHWZJRS=0 AND YLXYYXCHBHWZFYCYRS=0 AND YSBLRS=0) THEN 1");
        sb.append(" WHEN SFSCFGBA='是' AND (YFGHBJHWZJRS>0 OR YLXYYXCHBHWZFYCYRS>0 OR YSBLRS>0) THEN 2 ");
        sb.append(" WHEN SFSCFGBA='否' AND DATEDIFF(day,getdate(), NKGRQ) >0  AND (YFGHBJHWZJRS=0 AND YLXYYXCHBHWZFYCYRS=0 AND YSBLRS=0) THEN 3");
        sb.append(" WHEN SFSCFGBA='否' AND DATEDIFF(day,getdate(), NKGRQ) >0 AND (YFGHBJHWZJRS>0 OR YLXYYXCHBHWZFYCYRS>0 OR YSBLRS>0) THEN 4");
        sb.append(" ELSE 5 END) FGZT");
        sb.append(" FROM  datacenter.dbo.[ZA_V_LZ_SJPAGCXX]");
        JSONArray jsonArray = getDetailBySql(sb.toString());
        return jsonArray;
    }

    //根据项目编号获取项目详细信息
    public JSONObject getProjectInfo(String gcbm) throws Exception {
        StringBuilder sb = new StringBuilder(500);
        sb.append("SELECT *");
        sb.append(" FROM  datacenter.dbo.[ZA_V_LZ_SJPAGCXX]");
        sb.append(" WHERE code ='"+gcbm+"'");
        JSONArray jsonArray = getDetailBySql(sb.toString());
        return jsonArray.getJSONObject(0);
    }

    //根据语句获取查询内容的详细信息
    private JSONArray getDetailBySql(String sql) throws Exception {
        Connection conn=null;
        Statement st=null;
        ResultSet rs=null;
        JSONArray jsonArray = new JSONArray();
        try {
            long connStart = System.currentTimeMillis();
            //建立连接
            conn = JdbcSqlServerUtil.getConnection(connectUrlFor202,connectUser,connectPassword);
            //创建statment，跟数据库打交道的对象
            st = conn.createStatement();
            //执行查询，得到结果集
            long selectStart = System.currentTimeMillis();
            rs = st.executeQuery(sql);
            long selectEnd = System.currentTimeMillis();
            logger.info("链接时长："+(selectStart-connStart)/1000);
            logger.info("查询时长："+(selectEnd-selectStart)/1000);
            ResultSetMetaData rsmd = rs.getMetaData();
            List<String> listColumnName = new ArrayList<>();

            for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
                String columnName = rsmd.getColumnName(i);
                listColumnName.add(columnName);
            }
            while(rs.next()) {
                JSONObject jsonObject = new JSONObject();
                for(String columnName : listColumnName){
                    jsonObject.put(columnName,rs.getString(columnName));
                }
                jsonArray.add(jsonObject);
            }
        }
        finally {
            JdbcSqlServerUtil.release(conn, st, rs);
        }
        return jsonArray;
    }

    @Override
    public void writeRedisCache(){
        try {
            System.out.println(new Date()+":疫情复工项目数据缓存写入开始！");
            System.out.println(new Date()+":疫情复工项目数据缓存写入结束！");
        }
        catch (Exception ex){
            System.out.println("疫情复工数据缓存失败！");
        }
    }
}
