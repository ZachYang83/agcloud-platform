package com.augurit.agcloud.agcom.agsupport.sc.style.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.util.UploadUtil;
import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;
import com.augurit.agcloud.agcom.agsupport.domain.AgStyle;
import com.augurit.agcloud.agcom.agsupport.mapper.AgStyleMapper;
import com.augurit.agcloud.agcom.agsupport.sc.dir.service.IAgDir;
import com.augurit.agcloud.agcom.agsupport.sc.style.dao.AgStyleDao;
import com.augurit.agcloud.agcom.agsupport.sc.style.service.IAgStyle;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017-07-18.
 */
@Service
public class AgStyleImpl implements IAgStyle {

    @Autowired
    private AgStyleMapper agStyleMapper;

    @Autowired
    private IAgDir iAgDir;

    @Autowired
    private AgStyleDao agStyleDao;

    @Override
    public List<Map<String,Object>> findAllIcon(String path) throws Exception {
        List<Map<String,Object>> icons = new ArrayList<Map<String,Object>>();
        List<AgStyle> listUsedStyle = agStyleMapper.findAll();
        File file = new File(path);
            if (file.exists()) {
            File[] files = file.listFiles();
            for (File file2 : files) {
                if (!file2.isDirectory()) {
                    Map<String,Object> iconMap = new HashMap<String,Object>();
                    String fileName = file2.getName();
                    boolean isUsing = false;
                    for(AgStyle agStyle : listUsedStyle){
                        if(agStyle.getStyle()!=null && agStyle.getStyle().contains(fileName)){
                            isUsing = true;
                            break;
                        }
                    }
                    iconMap.put("icon", fileName);
                    iconMap.put("isUsing", isUsing);
                    icons.add(iconMap);
                }
            }
        }
        return icons;
    }

    @Override
    public PageInfo<AgStyle> findStyleListPage(AgStyle agStyle, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AgStyle> list = agStyleMapper.findAgStyleList(agStyle);
        return new PageInfo<AgStyle>(list);
    }

    @Override
    public List<AgStyle> findStyleList(AgStyle agStyle) throws Exception {
        return agStyleMapper.findAgStyleList(agStyle);
    }

    @Override
    public AgStyle findStyleById(String id) throws Exception {
        return agStyleMapper.findAgStyleById(id);
    }

    @Override
    public List<AgStyle> findDefaultStyleList() throws Exception {
        return agStyleMapper.findDefaultStyleList();
    }

    @Override
    public void saveStyle(AgStyle agStyle) throws Exception {
        agStyleMapper.saveAgStyle(agStyle);
    }

    @Override
    public void updateStyle(AgStyle agStyle) throws Exception {
        agStyleMapper.updateAgStyle(agStyle);
    }

    @Override
    public void deleteStyle(String[] ids) throws Exception {
        agStyleMapper.deleteAgStyle(ids);
    }

    @Override
    public Map getStyleConfMap(String dirLayerId) throws Exception {
        AgLayer agLayer = iAgDir.findLayerByDirLayerId(dirLayerId);
        return getStyleConfMap(agLayer);
    }

    @Override
    public Map getStyleConfMap(AgLayer agLayer) {

        Map styleConfMap = new HashMap();
        try {
            JSONObject dataJson = JSONObject.fromObject(agLayer.getData());
            if (dataJson.get("styleConf") != null) {
                styleConfMap = dataJson.getJSONObject("styleConf");
                Map styleIdMap = (Map) styleConfMap.get("styleIds");
                Map styleMap = new HashMap();
                if (styleConfMap != null) {
                    for (Object keyObj : styleIdMap.keySet()) {
                        String key = keyObj.toString().replace("_id", "");
                        String styleId = styleIdMap.get(keyObj).toString();
                        AgStyle agStyle = agStyleMapper.findAgStyleById(styleId);
                        if (agStyle != null && agStyle.getStyle() != null) {
                            styleMap.put(key, agStyle.getStyle());
                            if (agStyle.getPointType() != null)
                                styleMap.put(key + "_POINTTYPE", agStyle.getPointType());
                            styleMap.put("layerID", agLayer.getId());
                        }
                    }
                }

                styleConfMap.put("styles", styleMap);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return styleConfMap;
    }

    @Override
    public List<Map> getStyleConfList(String layerIds) throws Exception {
        List<Map> mapList = new ArrayList<Map>();
        if (StringUtils.isNotEmpty(layerIds)) {
            String[] ids = layerIds.split(",");
            List<AgLayer> list = iAgDir.findLayerListByLayerIds(ids);
            for (AgLayer agLayer : list) {
                Map temp = getStyleConfMap(agLayer);
                temp.put("featureType", agLayer.getFeatureType());
                if (temp.size() > 0) mapList.add(temp);
            }
        }
        return mapList;
    }


    @Override
    public Map loadStyleConf(MultipartFile multipartFile, String dirLayerId, String iconRootPath) throws Exception {
        Map result = new HashMap();
        List icons = new ArrayList();
        //获取图层原始样式配置信息
        AgLayer agLayer = iAgDir.findLayerByDirLayerId(dirLayerId);
        JSONObject data = JSONObject.fromObject(agLayer.getData());
        //获取sld数据
        String xml = UploadUtil.getAttachmentString(multipartFile.getInputStream(), 2048);
        XMLSerializer xmlSerializer = new XMLSerializer();
        JSONObject json = (JSONObject) xmlSerializer.read(xml);
        //获取sld中当前图层的样式配置数据（根据图层名称）
        String namedLayerStr = json.getString("NamedLayer");
        JSONObject featureTypeStyleObj = null;
        if (namedLayerStr.startsWith("[")) {
            JSONArray array = JSONArray.fromObject(namedLayerStr);
            for (int i = 0; i < array.size(); i++) {
                if (agLayer.getLayerTable().equals(array.getJSONObject(i).getString("Name"))) {
                    featureTypeStyleObj = array.getJSONObject(i).getJSONObject("UserStyle").getJSONObject("FeatureTypeStyle");
                    break;
                }
            }
        } else {
            JSONObject object = JSONObject.fromObject(namedLayerStr);
            if (agLayer.getLayerTable().equals(object.getString("Name"))) {
                featureTypeStyleObj = object.getJSONObject("UserStyle").getJSONObject("FeatureTypeStyle");
            }
        }
        //解析sld样式配置 组装成系统样式配置
        if (featureTypeStyleObj != null) {
            iconRootPath = iconRootPath + agLayer.getLayerTable() + "/";
            JSONObject styleConf = new JSONObject();
            JSONObject styleIds = new JSONObject();
            JSONObject styleNames = new JSONObject();
            //设置默认值
            styleConf.put("styleType", 0);
            //默认样式继承原始配置
            if (data.get("styleConf") != null && data.getJSONObject("styleConf").get("styleIds") != null) {
                JSONObject styleConfObj = data.getJSONObject("styleConf");
                styleIds.put("style_id_0", styleConfObj.getJSONObject("styleIds").getString("style_id_0"));
                styleNames.put("style_name_0", styleConfObj.getJSONObject("styleNames").getString("style_name_0"));
            }
            String ruleStr = featureTypeStyleObj.getString("Rule");
            if (ruleStr.startsWith("[")) {
                JSONObject fields = new JSONObject();
                JSONObject relations = new JSONObject();
                JSONObject values = new JSONObject();
                JSONObject minZooms = new JSONObject();
                JSONObject maxZooms = new JSONObject();
                JSONArray ruleArray = JSONArray.fromObject(ruleStr);
                for (int i = 0; i < ruleArray.size(); i++) {
                    JSONObject ruleObject = ruleArray.getJSONObject(i);
                    AgStyle agStyle = new AgStyle();
                    agStyle.setId(UUID.randomUUID().toString());
                    agStyle.setName(agLayer.getLayerTable() + "_" + i);
                    agStyle.setType(agLayer.getFeatureType());
                    agStyle.setIsSystem("0");
                    JSONObject object = formatAndSaveStyle(agStyle, ruleObject, iconRootPath);
                    if (object.get("icon") != null) {
                        icons.add(object.getString("icon"));
                    }
                    styleIds.put("style_id_" + (i + 1), agStyle.getId());
                    styleNames.put("style_name_" + (i + 1), agStyle.getName());
                    minZooms.put("minZoom_" + (i + 1), 0);
                    maxZooms.put("maxZoom_" + (i + 1), 20);
                    JSONObject filter = ruleObject.getJSONObject("ogc:Filter");
                    if (filter.get("ogc:PropertyIsEqualTo") != null) {
                        JSONObject ogc = filter.getJSONObject("ogc:PropertyIsEqualTo");
                        fields.put("field_" + (i + 1), ogc.getString("ogc:PropertyName"));
                        relations.put("relation_" + (i + 1), 1);
                        values.put("value_" + (i + 1), ogc.getString("ogc:Literal"));
                    } else {
                        // TODO: 2017-09-20  其他判断关系
                    }
                }
                styleConf.put("fields", fields);
                styleConf.put("relations", relations);
                styleConf.put("values", values);
                styleConf.put("minZooms", minZooms);
                styleConf.put("maxZooms", maxZooms);
            } else {
                JSONObject ruleObject = JSONObject.fromObject(ruleStr);
                AgStyle agStyle = new AgStyle();
                agStyle.setId(UUID.randomUUID().toString());
                agStyle.setName(agLayer.getLayerTable());
                agStyle.setType(agLayer.getFeatureType());
                agStyle.setIsSystem("0");
                JSONObject object = formatAndSaveStyle(agStyle, ruleObject, iconRootPath);
                if (object.get("icon") != null) {
                    icons.add(object.getString("icon"));
                }
                styleIds.put("style_id_0", agStyle.getId());
                styleNames.put("style_name_0", agStyle.getName());
            }
            styleConf.put("styleIds", styleIds);
            styleConf.put("styleNames", styleNames);
            data.put("styleConf", styleConf.toString());

        }
        agLayer.setData(data.toString());
        result.put("agLayer", agLayer);
        result.put("icons", icons);
        return result;
    }

    @Override
    public List<Map> listTableColumnData(String dataSourceId, String tableName, String column) throws Exception {
        return agStyleDao.listTableColumnData(dataSourceId, tableName, column);
    }

    private JSONObject formatAndSaveStyle(AgStyle agStyle, JSONObject ruleObject, String iconRootPath) throws Exception {
        JSONObject result = new JSONObject();
        JSONArray styleArray = new JSONArray();
        String symbolizer = "";
        if ("point".equals(agStyle.getType())) {
            symbolizer = ruleObject.getString("PointSymbolizer");
        } else if ("polyline".equals(agStyle.getType())) {
            symbolizer = ruleObject.getString("LineSymbolizer");
        } else if ("polygon".equals(agStyle.getType())) {
            symbolizer = ruleObject.getString("PolygonSymbolizer");
        }
        if (symbolizer.startsWith("[")) {
            JSONArray symbolizerArray = JSONArray.fromObject(symbolizer);
            for (int i = 0; i < symbolizerArray.size(); i++) {
                styleArray.add(formatSymbolizer(JSONObject.fromObject(symbolizerArray.getJSONObject(i))));
            }
        } else {
            styleArray.add(formatSymbolizer(JSONObject.fromObject(symbolizer)));
        }
        if (styleArray.size() > 0) {
            String style = styleArray.toString();
            if ("point".equals(agStyle.getType()) && symbolizer.indexOf("ExternalGraphic") > 0) {
                JSONObject iconObject = styleArray.getJSONObject(0).getJSONObject("icon");
                String iconUrl = iconObject.getString("iconUrl").replace("http://@httpwebsiteurl/", iconRootPath);
                result.put("icon", iconUrl.replace(iconRootPath, ""));
                iconObject.put("iconUrl", iconUrl);
                style = styleArray.getString(0);
                agStyle.setPointType("0");
            } else if ("point".equals(agStyle.getType())) {
                agStyle.setPointType("2");
            }
            agStyle.setStyle(style);
        }
        result.put("agStyle", agStyle);
        agStyleMapper.saveAgStyle(agStyle);
        return result;
    }

    private JSONObject formatSymbolizer(JSONObject symbolizer) {
        JSONObject style = new JSONObject();
        if (symbolizer.get("Graphic") != null) {
            JSONObject graphic = symbolizer.getJSONObject("Graphic");
            if (graphic.get("Mark") != null) {
                JSONObject mark = graphic.getJSONObject("Mark");
                if ("circle".equals(mark.getString("WellKnownName"))) {
                    style.put("drawMarkerType", 0);
                } else if ("square".equals(mark.getString("WellKnownName"))) {
                    style.put("drawMarkerType", 1);
                }
                style.put("radius", graphic.getString("Size"));
                JSONArray cssParameters = mark.getJSONObject("Fill").getJSONArray("CssParameter");
                for (int j = 0; j < cssParameters.size(); j++) {
                    JSONObject cssParameter = cssParameters.getJSONObject(j);
                    if ("fill".equals(cssParameter.getString("@name"))) {
                        style.put("fillColor", cssParameter.getString("#text"));
                        style.put("color", cssParameter.getString("#text"));
                        style.put("weight", 0);
                        style.put("dashArray", "0,0");
                    } else if ("fill-opacity".equals(cssParameter.getString("@name"))) {
                        style.put("fillOpacity", cssParameter.getString("#text"));
                        style.put("opacity", cssParameter.getString("#text"));
                    }
                }
            } else if (graphic.get("ExternalGraphic") != null) {
                JSONObject icon = new JSONObject();
                JSONObject onlineResource = graphic.getJSONObject("ExternalGraphic").getJSONObject("OnlineResource");
                icon.put("iconUrl", onlineResource.getString("@xlink:href"));
                icon.put("iconSize", Arrays.asList(32, 32).toArray());
                icon.put("iconAnchor", Arrays.asList(16, 16).toArray());
                icon.put("popupAnchor", Arrays.asList(0, -16).toArray());
                icon.put("tooltipAnchor", Arrays.asList(16, -16).toArray());
                style.put("icon", icon);
            }
        }
        if (symbolizer.get("Stroke") != null) {
            JSONArray cssParameters = symbolizer.getJSONObject("Stroke").getJSONArray("CssParameter");
            for (int j = 0; j < cssParameters.size(); j++) {
                JSONObject cssParameter = cssParameters.getJSONObject(j);
                if ("stroke".equals(cssParameter.getString("@name"))) {
                    style.put("color", cssParameter.getString("#text"));
                } else if ("stroke-width".equals(cssParameter.getString("@name"))) {
                    style.put("weight", cssParameter.getString("#text"));
                } else if ("stroke-opacity".equals(cssParameter.getString("@name"))) {
                    style.put("opacity", cssParameter.getString("#text"));
                } else if ("stroke-dasharray".equals(cssParameter.getString("@name"))) {
                    style.put("dashArray", cssParameter.getString("#text").replace(" ", ","));
                }
            }
        }
        if (symbolizer.get("Fill") != null) {
            JSONArray cssParameters = symbolizer.getJSONObject("Fill").getJSONArray("CssParameter");
            for (int j = 0; j < cssParameters.size(); j++) {
                JSONObject cssParameter = cssParameters.getJSONObject(j);
                if ("fill".equals(cssParameter.getString("@name"))) {
                    style.put("fillColor", cssParameter.getString("#text"));
                    if (symbolizer.get("Stroke") == null) {
                        style.put("color", cssParameter.getString("#text"));
                        style.put("weight", 0);
                        style.put("dashArray", "0,0");
                    }
                } else if ("fill-opacity".equals(cssParameter.getString("@name"))) {
                    style.put("fillOpacity", cssParameter.getString("#text"));
                    if (symbolizer.get("Stroke") == null) {
                        style.put("opacity", cssParameter.getString("#text"));
                    }
                }
            }
        }
        return style;
    }

    @Override
    public List<String> getStyleIdsByLayerId(String layerId) throws Exception {
        return agStyleMapper.findStyleIdsByLayerId(layerId);
    }

    @Override
    public void updateLayerStylesRetionship(String layerId, List<String> newStyleIds, List<String> olderStyles) throws Exception {

        List<Map> adds = new ArrayList<>();
        List<String> removes = new ArrayList<>();

        if (olderStyles.size() == 0) { //newStyleIds全部新增
            for (String s : newStyleIds) {
                Map<String, String> item = new HashMap<>();
                item.put("id", UUID.randomUUID().toString());
                item.put("layerId", layerId);
                item.put("styleId", s);
                adds.add(item);
            }
        } else {
            for (String s : newStyleIds) {
                if (!olderStyles.contains(s)) {
                    Map<String, String> item = new HashMap<>();
                    item.put("id", UUID.randomUUID().toString());
                    item.put("layerId", layerId);
                    item.put("styleId", s);
                    adds.add(item);
                }
            }
            for (String str : olderStyles) {
                if (!newStyleIds.contains(str)) {
                    removes.add(str);
                }
            }
        }

        if(removes.size() > 0) {
            agStyleMapper.deleteLayerStyle(layerId, removes);
        }
        if(adds.size() > 0) {
            agStyleMapper.saveLayerStyle(layerId, adds);
        }

    }





}