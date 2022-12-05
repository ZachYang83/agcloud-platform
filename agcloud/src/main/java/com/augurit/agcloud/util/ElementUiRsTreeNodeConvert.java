package com.augurit.agcloud.util;


import com.augurit.agcloud.agx.common.domain.AgxRsTmn;
import com.augurit.agcloud.domain.OpuRsToolBar;
import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuRsAppInst;
import com.augurit.agcloud.opus.common.domain.OpuRsAppSoft;
import com.augurit.agcloud.opus.common.domain.OpuRsFunc;
import com.augurit.agcloud.opus.common.domain.OpuRsMac;
import com.augurit.agcloud.opus.common.domain.OpuRsMenu;
import com.augurit.agcloud.opus.common.domain.OpuRsNet;
import com.augurit.agcloud.opus.common.domain.OpuRsServ;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class ElementUiRsTreeNodeConvert {
    public ElementUiRsTreeNodeConvert() {
    }

    public static String getFullContextPath() {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        return basePath;
    }


    public static ElementUiRsTreeNode convertFunc(OpuRsToolBar func, boolean isNeedFullPath) {
        if (func != null) {
            if (isNeedFullPath) {
                String basePath = getFullContextPath();
                if (StringUtils.isNotBlank(func.getSmallImgPath())) {
                    if (func.getSmallImgPath().startsWith("/")) {
                        if (func.getSmallImgPath().indexOf(basePath) < 0) {
                            func.setSmallImgPath(basePath + func.getSmallImgPath());
                        }
                    } else if (func.getSmallImgPath().indexOf(basePath) < 0) {
                        func.setSmallImgPath(basePath + "/" + func.getSmallImgPath());
                    }
                }

                if (StringUtils.isNotBlank(func.getMiddleImgPath())) {
                    if (func.getMiddleImgPath().startsWith("/")) {
                        if (func.getMiddleImgPath().indexOf(basePath) < 0) {
                            func.setMiddleImgPath(basePath + func.getMiddleImgPath());
                        }
                    } else if (func.getMiddleImgPath().indexOf(basePath) < 0) {
                        func.setMiddleImgPath(basePath + "/" + func.getMiddleImgPath());
                    }
                }

                if (StringUtils.isNotBlank(func.getBigImgPath())) {
                    if (func.getBigImgPath().startsWith("/")) {
                        if (func.getBigImgPath().indexOf(basePath) < 0) {
                            func.setBigImgPath(basePath + func.getBigImgPath());
                        }
                    } else if (func.getBigImgPath().indexOf(basePath) < 0) {
                        func.setBigImgPath(basePath + "/" + func.getBigImgPath());
                    }
                }

                if (StringUtils.isNotBlank(func.getHugeImgPath())) {
                    if (func.getHugeImgPath().startsWith("/")) {
                        if (func.getHugeImgPath().indexOf(basePath) < 0) {
                            func.setHugeImgPath(basePath + func.getHugeImgPath());
                        }
                    } else if (func.getHugeImgPath().indexOf(basePath) < 0) {
                        func.setHugeImgPath(basePath + "/" + func.getHugeImgPath());
                    }
                }
            }

            ElementUiRsTreeNode funcNode = new ElementUiRsTreeNode();
            funcNode.setId(func.getFuncId());
            funcNode.setLabel(func.getFuncName());
            funcNode.setType(func.getOpusRsType().toString());
            funcNode.setData(func);
            return funcNode;
        } else {
            return null;
        }
    }

    public static List<ElementUiRsTreeNode> convertFuncs(List<OpuRsToolBar> funcs, boolean isNeedFullPath) {
        if (funcs != null && funcs.size() > 0) {
            List<ElementUiRsTreeNode> appNodeChildren = new ArrayList();
            Iterator var3 = funcs.iterator();

            while(var3.hasNext()) {
                OpuRsToolBar func = (OpuRsToolBar)var3.next();
                appNodeChildren.add(convertFunc(func, isNeedFullPath));
            }

            return appNodeChildren;
        } else {
            return null;
        }
    }
}
