package com.augurit.agcloud.agcom.agsupport.sc.escustomword.controller;

import com.augurit.agcloud.agcom.agsupport.domain.AgEsCustomWord;
import com.augurit.agcloud.agcom.agsupport.sc.escustomword.service.IAgEsCustomWord;
import com.augurit.agcloud.agcom.agsupport.util.ExcelUtil;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.common.util.HttpRequester;
import com.common.util.HttpRespons;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;


/**
 * @Author:Dreram
 * @Description:
 * @Date:created in :17:18 2018/12/21
 * @Modified By:
 */
@Api(value = "自定义词设置接口",description = "自定义词设置接口")
@RestController
@RequestMapping("/agsupport/escustomword")
public class AgEsCustomWordController {

    @Value("${address.url}")
    private String esAddress;

    @Autowired
    private IAgEsCustomWord iAgEsCustomWord;

    @ApiOperation(value = "分页获取自定义词",notes = "分页获取自定义词")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "agEsCustomWord",required = true, value = "自定义词对象", dataType = "AgEsCustomWord"),
            @ApiImplicitParam(name = "page",required = true, value = "分页参数:/agsupport/param/paramList?page=1&rows=10&name=", dataType = "Page")
    })
    @RequestMapping(value = "/customList",method = RequestMethod.POST)
    public ContentResultForm customList(AgEsCustomWord agEsCustomWord, Page page) throws Exception {
        PageInfo<AgEsCustomWord> pageInfo=iAgEsCustomWord.searchAgEsCustomWord(agEsCustomWord, page);
        return new ContentResultForm<EasyuiPageInfo>(true,PageHelper.toEasyuiPageInfo(pageInfo));
    }

    @ApiOperation(value = "保存自定义词",notes = "保存自定义词")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "agEsCustomWord" ,value = "自定义词对象",dataType = "AgEsCustomWord")
    })
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public ResultForm save(AgEsCustomWord agEsCustomWord)throws Exception{
        try {
            String str = agEsCustomWord.getCustomWord().replaceAll("，",",");//把中文逗号替换为英文逗号
            String[] customWords = str.split(",");
            String[] ids = agEsCustomWord.getId().split(",");
            StringBuilder sb = new StringBuilder();
            for (int i=0;i<customWords.length;i++){
                sb.append(customWords[i]+",");
                if ("undefined".equals(ids[i]) || "".equals(ids[i]) || ids[i]==null){
                    AgEsCustomWord cust = new AgEsCustomWord();
                    String word = customWords[i];
                    AgEsCustomWord customWord = iAgEsCustomWord.findCustom(word);
                    if (customWord!=null) continue;//已经存在了自定义词则不保存
                    cust.setId(UUID.randomUUID().toString());
                    cust.setChangeTime(new Date());
                    cust.setCustomWord(word);
                    iAgEsCustomWord.saveAgEsCustomWord(cust);
                }else {
                    //agEsCustomWord.setChangeTime(new Date());
                    agEsCustomWord.setId(ids[i]);
                    agEsCustomWord.setCustomWord(customWords[i]);
                    iAgEsCustomWord.updataAgEsCustomWord(agEsCustomWord);
                }
            }
            //同步
            if (sb.toString().length()>0){
                sendCustom();
            }
            return new ResultForm(true, "保存成功");
        }catch (Exception e){
            return new ResultForm(false, "保存失败");
        }
    }

    @ApiOperation(value = "根据id删除自定义词",notes = "根据id删除自定义词")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id" ,value = "自定义词id",dataType = "String")
    })
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public ResultForm delete(String id)throws Exception{
        try {
            iAgEsCustomWord.delById(id);
            sendCustom();
            return new ResultForm(true, "删除成功");
        }catch (Exception e){
            return new ResultForm(false, "删除失败");
        }
    }

    @ApiOperation(value = "批量删除自定义词",notes = "批量删除自定义词")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids" ,value = "自定义词id",dataType = "String")
    })
    @RequestMapping(value = "/batchDelById",method = RequestMethod.DELETE)
    public ResultForm batchDelById(String ids) {
        try {
            String[] idArr = ids.split(",");
            iAgEsCustomWord.batchDelById(idArr);
            sendCustom();
            return new ResultForm(true, "删除成功");
        }catch (Exception e){
            return new ResultForm(false, "删除失败");
        }
    }

    @ApiOperation(value = "根据Excel文件导入自定义词",notes = "根据Excel文件导入自定义词")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file" ,value = "Excel文件",dataType = "MultipartFile")
    })
    @RequestMapping(value = "/importExecl",method = RequestMethod.POST)
    public  ResultForm importExecl(@RequestParam MultipartFile file){
        try {
            JSONObject json = new JSONObject();
            List<Object[]> list = ExcelUtil.parseExcel(file.getOriginalFilename(),file.getInputStream());
            List<AgEsCustomWord> customWordsList = iAgEsCustomWord.findAll();
            boolean title = true;
            for (Object[] object:list){
                //第一行数组对象是 Excel文件的字段,不保存进数据库
                if (title){
                    if (object[0]!=null && !"customWord".equals(object[0].toString())){
                        return new ResultForm(false, "Excel模板表头字段有误，请使用正确模板");
                    }
                    title =false;
                }else {
                    for (int i=0;i<object.length;i++){
                        if (object[0]==null) continue;
                        String word = object[0].toString();
                        AgEsCustomWord synonymWord = iAgEsCustomWord.findByCustomWord(word);
                        if (synonymWord==null){
                            AgEsCustomWord custom = new AgEsCustomWord();
                            custom.setId(UUID.randomUUID().toString());
                            custom.setCustomWord(object[i].toString());
                            custom.setChangeTime(new Date());
                            iAgEsCustomWord.saveAgEsCustomWord(custom);
                        }
                    }
                }
            }
            sendCustom();//同步
            return new ResultForm(true, "导入成功");
        }catch (Exception e){
            return new ResultForm(false, "导入失败");
        }
    }

    /**
     * 发送自定义词
     */
    private void sendCustom() {
        try {

            List<AgEsCustomWord> list = iAgEsCustomWord.findAll();
            StringBuilder oldSb = new StringBuilder();
            String customs = "";
            for (AgEsCustomWord customWord:list){
                if (customWord.getCustomWord()==null) continue;
                oldSb.append(customWord.getCustomWord());
                oldSb.append(",");
            }
            if (oldSb.length() > 0) {
                String oldStr = oldSb.toString();
                customs = oldStr.substring(0,oldStr.length()-1);
            }
            //调用agaddress 的同步接口
            Map<String, String> param = new HashMap<>();
            param.put("customWords", customs);
            HttpRespons httpRespons = new HttpRequester().sendPost(esAddress + "/syncustom", param);
            if (httpRespons.getCode() == 200) {
                System.out.println("同步自定义词成功");
            }
        } catch (Exception e) {
            System.out.println("同步自定义词失败 " + e.getMessage());
        }
    }
}
