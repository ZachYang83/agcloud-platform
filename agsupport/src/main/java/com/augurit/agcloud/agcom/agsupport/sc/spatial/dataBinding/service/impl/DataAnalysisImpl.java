package com.augurit.agcloud.agcom.agsupport.sc.spatial.dataBinding.service.impl;

import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;
import com.augurit.agcloud.agcom.agsupport.domain.AgLayerFieldConf;
import com.augurit.agcloud.agcom.agsupport.sc.dir.service.IAgDir;
import com.augurit.agcloud.agcom.agsupport.sc.field.service.IAgField;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.config.SpatialConfig;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.dataBinding.service.IDataAnalysis;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.dataBinding.service.IDataBinding;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.util.SpatialUtil;
import com.common.dbcp.DBHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by chendingxing on 2018-03-15.
 */
@Service
public class DataAnalysisImpl implements IDataAnalysis {

    @Autowired
    private IAgField iField;

    @Autowired
    private IAgDir iDir;

    @Autowired
    private IDataBinding iDataBinding;

    @Override
    public List<Map> bufferAnalysis(String userId, String dirLayerId1, String dirLayerId2, double distance) throws Exception {
        AgLayer layer1 = iDir.findLayerByDirLayerId(dirLayerId1);
        AgLayer layer2 = iDir.findLayerByDirLayerId(dirLayerId2);

        List<AgLayerFieldConf> layerFieldConfs = iField.getLayerFieldsByUserId(dirLayerId2, userId);

        //getLayerFieldsByUserId
        //同源才进行空间查询
        if(layer1.getDataSourceId().equals(layer2.getDataSourceId())){
            StringBuffer sql = new StringBuffer("SELECT db2gse.st_astext(t2.shape) ").append(SpatialConfig.WKT_COLUMN);
            for (AgLayerFieldConf agLayerField : layerFieldConfs) {
                //只查询显示的字段
                if ("1".equals(agLayerField.getViewInResult())) {
                    sql.append(",t2."+agLayerField.getFieldName());
                }
            }
            sql.append(" from ").append(layer1.getLayerTable() + " t1,").append(layer2.getLayerTable() + " t2 ");
            sql.append("WHERE db2gse.st_intersects (db2gse.st_buffer (t1.shape,"+ distance +"), t2.shape) = 1");
            List<Map> result = DBHelper.find(layer1.getDataSourceId(), sql.toString(), null);
            result = SpatialUtil.formatDataMapList(result);
            return result;
        }
        return null;
    }
}
