package com.augurit.agcloud.agcom.agsupport.sc.func.convert;

import com.augurit.agcloud.agcom.agsupport.domain.AgFunc;

import java.util.Map;

/**
 * @author zhangmingyang
 * @Description: 将agcloud的功能对象转换为agcom的
 * @date 2019-02-22 15:04
 */
public class AgFuncConverter {
    public AgFunc convertToAgFunc(Map map){
        Object funcId = map.get("funcId");
        Object funcName = map.get("funcName");
        Object funcCode = map.get("funcCode");
        Object funcType = map.get("funcType");
        Object parentFuncId = map.get("parentFuncId");
        Object funcIconCss = map.get("funcIconCss");
        Object funcSortNo = map.get("funcSortNo");
        Object smallImgPath = map.get("smallImgPath");
        Object middleImgPath = map.get("middleImgPath");
        Object bigImgPath = map.get("bigImgPath");
        Object hugeImgPath = map.get("hugeImgPath");
        Object funcDesc = map.get("funcDesc");
        Object widgetType = map.get("widgetType");
        Object funcInvokeUrl = map.get("funcInvokeUrl");
        Object pagePosition = map.get("pagePosition");
        AgFunc agFunc = new AgFunc();
        agFunc.setId(funcId.toString());
        if (funcName != null){
            agFunc.setName(funcName.toString());
        }
        if (funcCode != null){
            agFunc.setCode(funcCode.toString());
        }
        if (funcType != null){
            agFunc.setType(funcType.toString());
        }
        if (parentFuncId != null){
            agFunc.setParentFuncId(parentFuncId.toString());
        }
        if (funcIconCss != null){
            agFunc.setIconClass(funcIconCss.toString());
        }
        if (funcSortNo != null){
            agFunc.setOrderNm(Integer.valueOf(funcSortNo.toString()));
        }
        if (smallImgPath != null){
            agFunc.setSmallImgPath(smallImgPath.toString());
        }
        if (middleImgPath != null){
            agFunc.setMiddleImgPath(middleImgPath.toString());
        }
        if (bigImgPath != null){
            agFunc.setBigImgPath(bigImgPath.toString());
        }
        if (hugeImgPath != null){
            agFunc.setHugeImgPath(hugeImgPath.toString());
        }
        if (funcDesc != null){
            agFunc.setFuncDesc(funcDesc.toString());
        }
        if (widgetType != null){
            agFunc.setWidgetType(widgetType.toString());
        }
        if(funcInvokeUrl != null){
            agFunc.setFuncInvokeUrl(funcInvokeUrl.toString());
        }
        if(pagePosition != null){
            agFunc.setPagePosition(pagePosition.toString());
        }

        return agFunc;
    }
}
