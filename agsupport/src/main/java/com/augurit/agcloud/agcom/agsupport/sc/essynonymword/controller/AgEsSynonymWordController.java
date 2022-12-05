package com.augurit.agcloud.agcom.agsupport.sc.essynonymword.controller;

import com.augurit.agcloud.agcom.agsupport.domain.AgEsSynonymWord;
import com.augurit.agcloud.agcom.agsupport.sc.escustomword.service.IAgEsCustomWord;
import com.augurit.agcloud.agcom.agsupport.sc.essynonymword.service.IAgEsSynonymWord;
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
 * @Date:created in :17:19 2018/12/21
 * @Modified By:
 */
@Api(value = "同义词设置接口",description = "同义词设置接口")
@RestController
@RequestMapping("/agsupport/essynonymword")
public class AgEsSynonymWordController {

    @Value("${address.url}")
    private String esAddress;

    @Autowired
    private IAgEsSynonymWord iAgEsSynonymWord;

    @Autowired
    private IAgEsCustomWord iAgEsCustomWord;

    @ApiOperation(value = "分页获取同义词",notes = "分页获取同义词")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "agEsSynonymWord",required = true, value = "同义词对象", dataType = "AgEsSynonymWord"),
            @ApiImplicitParam(name = "page",required = true, value = "分页参数:/agsupport/param/paramList?page=1&rows=10&name=", dataType = "Page")
    })
    @RequestMapping(value = "/synonymList",method = RequestMethod.POST)
    public ContentResultForm customList(AgEsSynonymWord agEsSynonymWord, Page page) throws Exception {
        PageInfo<AgEsSynonymWord> pageInfo=iAgEsSynonymWord.searchAgEsSynonymWord(agEsSynonymWord, page);
        return new ContentResultForm<EasyuiPageInfo>(true,PageHelper.toEasyuiPageInfo(pageInfo));
    }

    @ApiOperation(value = "保存自定义词",notes = "保存自定义词")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "agEsSynonymWord" ,value = "同义词对象",dataType = "AgEsSynonymWord")
    })
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public ResultForm save(AgEsSynonymWord agEsSynonymWord)throws Exception{
        try {
            String[] synonymWords = agEsSynonymWord.getSynonymWord().replaceAll("；",";").split(";");//把中文;替换为英文;
            String[] words = agEsSynonymWord.getWord().replaceAll("，",",").split(",");
            String[] ids = agEsSynonymWord.getId().replaceAll("，",",").split(",");
            Map<String,String> idMap = new HashMap<>();
            StringBuilder sb = new StringBuilder();//记录保存或者更新的词和同义词用于同步
            for (int i=0;i<ids.length;i++){
                sb.append(words[i]+",");
                String[] tempSynoWordArr = synonymWords[i].replaceAll("，",",").split(",");
                //内容去重
                HashSet<Object> repeatSet = new HashSet<>();
                for (String str:tempSynoWordArr){
                    repeatSet.add(str.trim());
                }
                String setStr = repeatSet.toString();
                String tempSynoWord = setStr.substring(1,setStr.length()-1);
                sb.append(tempSynoWord);

                if ("undefined".equals(ids[i]) || "".equals(ids[i]) || ids[i]==null){
                    String word = words[i];
                    AgEsSynonymWord oldSynonymWord = iAgEsSynonymWord.findWord(word);
                    //如果新增的词已经存在，则判断新增的同义词是否已经存在，不存在则添加同义词
                    if (oldSynonymWord!=null) {
                        String[] oldSynonArr = oldSynonymWord.getSynonymWord().split(",");
                        //使用set去重
                        HashSet<Object> set = new HashSet<>();
                        for (String old:oldSynonArr){
                            set.add(old.trim());
                        }
                        for (String temp:tempSynoWordArr){
                            set.add(temp.trim());
                        }
                        String str = set.toString();
                        String saveSynonWord = str.substring(1,str.length()-1);
                        oldSynonymWord.setSynonymWord(saveSynonWord);
                        //oldSynonymWord.setChangeTime(new Date());
                        idMap.put(oldSynonymWord.getId(),"true");
                        iAgEsSynonymWord.updataAgEsSynonymWord(oldSynonymWord);
                        sb.append(saveSynonWord);
                    }else {
                        AgEsSynonymWord syon = new AgEsSynonymWord();
                        syon.setId(UUID.randomUUID().toString());
                        syon.setWord(words[i]);
                        syon.setSynonymWord(tempSynoWord);
                        syon.setChangeTime(new Date());
                        iAgEsSynonymWord.saveAgEsSynonymWord(syon);
                    }
                }else {
                    if ("true".equals(idMap.get(ids[i]))) continue;
                    agEsSynonymWord.setId(ids[i]);
                    agEsSynonymWord.setWord(words[i]);
                    agEsSynonymWord.setSynonymWord(tempSynoWord);
                    //agEsSynonymWord.setChangeTime(new Date());
                    iAgEsSynonymWord.updataAgEsSynonymWord(agEsSynonymWord);
                }
                sb.append(";");
            }
            if (sb.toString().length()>0){
                sendSynonWord();//同步词库
            }
            return new ResultForm(true, "保存成功");
        }catch (Exception e){
            return new ResultForm(false, "保存失败");
        }
    }

    @ApiOperation(value = "根据id删除同义词",notes = "根据id删除同义词")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id" ,value = "同义词id",dataType = "String")
    })
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public ResultForm delete(String id)throws Exception{
        try {
            iAgEsSynonymWord.delById(id);
            sendSynonWord();
            return new ResultForm(true, "删除成功");
        }catch (Exception e){
            return new ResultForm(false, "删除失败");
        }
    }

    @ApiOperation(value = "批量删除同义词",notes = "批量删除同义词")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids" ,value = "同义词id",dataType = "String")
    })
    @RequestMapping(value = "/batchDelById",method = RequestMethod.DELETE)
    public ResultForm batchDelById(String ids) {
        try {
            String[] idArr = ids.split(",");
            iAgEsSynonymWord.batchDelById(idArr);
            sendSynonWord();
            return new ResultForm(true, "删除成功");
        }catch (Exception e){
            return new ResultForm(false, "删除失败");
        }
    }

    @ApiOperation(value = "根据Excel文件导入同义词义词",notes = "根据Excel文件导入同义词义词")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file" ,value = "Excel文件",dataType = "MultipartFile")
    })
    @RequestMapping(value = "/importExecl",method = RequestMethod.POST)
    public  ResultForm importExecl(@RequestParam MultipartFile file){
        try {
            List<Object[]> list = ExcelUtil.parseExcel(file.getOriginalFilename(),file.getInputStream());
            boolean title = true;
            for (Object[] object:list){
                //第一行数组对象是 Excel文件的字段,不保存进数据库
                if (title){
                    if (object.length<2) {
                        return new ResultForm(false, "Excel模板缺少列");
                    }else if ((object[0]==null || object[1]==null) && (!"word".equals(object[0].toString()) && !"synonymWord".equals(object[1].toString()))){
                        return new ResultForm(false, "Excel模板表头字段有误，第一列应为：word，第二列应为：synonymWord");
                    }
                    title =false;
                }else {
                    for (int i=0;i<object.length;i++){
                        if (object[0]==null) continue;
                        String word_excel = object[0].toString();
                        String synonyWord_excel = object[1].toString().replaceAll("，",",");
                        AgEsSynonymWord synonymWord = iAgEsSynonymWord.findByWord(word_excel);
                        if (synonymWord==null){
                            AgEsSynonymWord synonym = new AgEsSynonymWord();
                            synonym.setId(UUID.randomUUID().toString());
                            synonym.setWord(word_excel);
                            synonym.setSynonymWord(synonyWord_excel);
                            synonym.setChangeTime(new Date());
                            iAgEsSynonymWord.saveAgEsSynonymWord(synonym);
                        }else {
                            String[] tempArr = synonyWord_excel.split(",");
                            String[] oldArr = synonymWord.getSynonymWord().split(",");
                            HashSet<Object> set = new HashSet<>();
                            for (String excel:tempArr){
                                set.add(excel.trim());
                            }
                            for (String old:oldArr){
                                set.add(old.trim());
                            }
                            String str = set.toString();
                            String upStr = str.substring(1,str.length()-1);
                            synonymWord.setSynonymWord(upStr);
                            synonymWord.setChangeTime(new Date());
                            iAgEsSynonymWord.updataAgEsSynonymWord(synonymWord);
                        }
                    }
                }
            }
            sendSynonWord();//同步词库
            return new ResultForm(true, "导入成功");
        }catch (Exception e){
            return new ResultForm(false, "导入失败");
        }
    }

    /**
     * 同步
     */
    private void sendSynonWord() {
        try {
            List<AgEsSynonymWord> list = iAgEsSynonymWord.findAll();
            StringBuilder sb = new StringBuilder();
            for (AgEsSynonymWord synonymWord:list){
                if (synonymWord.getSynonymWord()==null) continue;
                sb.append(synonymWord.getWord().trim());
                sb.append(",");
                sb.append(synonymWord.getSynonymWord().trim());
                sb.append(";");
            }
            //调用agaddress 的同步接口
            Map<String, String> param = new HashMap<>();
            param.put("synonymWords",sb.toString().replaceAll(" ",""));
            HttpRespons httpRespons = new HttpRequester().sendPost(esAddress+"/synsynonym", param);
            if (httpRespons.getCode() == 200) {
                System.out.println("同步同义词成功");
            }
        } catch (Exception e) {
            System.out.println("同步同义词失败 " + e.getMessage());
        }
    }
}
