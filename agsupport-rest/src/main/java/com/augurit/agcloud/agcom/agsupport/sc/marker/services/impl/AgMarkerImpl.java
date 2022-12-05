package com.augurit.agcloud.agcom.agsupport.sc.marker.services.impl;


import com.augurit.agcloud.agcom.agsupport.domain.AgMarkRemind;
import com.augurit.agcloud.agcom.agsupport.domain.AgMarker;
import com.augurit.agcloud.agcom.agsupport.domain.AgUserMarker;
import com.augurit.agcloud.agcom.agsupport.mapper.AgMarkerMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.AgUserMapper;
import com.augurit.agcloud.agcom.agsupport.sc.marker.services.IAgMarker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import sun.misc.BASE64Encoder;

import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * Created by yzq on 2019-10-20.
 */
@Service
public class AgMarkerImpl implements IAgMarker {
    @Autowired
    private AgMarkerMapper agMarkerMapper;
    @Autowired
    private AgUserMapper agUserMapper;
//    @Autowired
//    static BASE64Encoder encoder = new BASE64Encoder();
    @Override
    public List getMarkers(String userId) throws Exception {

        List<AgMarker>  agMarkerS = agMarkerMapper.getMarkers(userId);
//        if(agMarkerS.size()>0){
//            for (AgMarker agMarker :agMarkerS ){
//                File f = new File(agMarker.getImage_path().trim());
//                BufferedImage bi;
//                String prefix = tollutil.suffixName(agMarker.getImage_path());
//                try {
//                    bi = ImageIO.read(f);
//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    ImageIO.write(bi, prefix , baos);  //经测试转换的图片是格式这里就什么格式，否则会失真
//                    byte[] bytes = baos.toByteArray();
//                    agMarker.setImage_path(tollutil.imgsBase(prefix)+encoder.encodeBuffer(bytes).trim());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
        return agMarkerS;

    }
    public void shareMarker(String userId,String markId,String userName) throws Exception {
        if(userId!=null&&markId!=null){
            String[] userIds=userId.split(",");
            if(userIds.length>0){
                for(int i=0;i<userIds.length;i++){
                if(agMarkerMapper.getUserMarker(userIds[0],markId)==null){
                    AgUserMarker agUserMarker=new AgUserMarker();
                    agUserMarker.setId(UUID.randomUUID().toString());
                    agUserMarker.setMarkId(markId);
                    agUserMarker.setUserId(userIds[0]);
                    // 分享默认给0
                    agUserMarker.setViewState("0");
                    agUserMarker.setShareTime(new Date());
                    agUserMarker.setShareUser(userName);
                    agMarkerMapper.saveUserMarker(agUserMarker);
                }
                    return;
                }
                return;
            }
            return;
        }else {
            return;
        }



    }
    public void deleteMarker(String userId,String markId) throws Exception {
        if(userId!=null&&markId!=null){
           AgMarker agMarker=agMarkerMapper.getMarkerbyId(markId);
           if(agMarker!=null&&userId.equals(agMarker.getUserId())){
               agMarkerMapper.deleteMarker( markId);
               agMarkerMapper.deleteUserMarkerbymarkId(markId);
           }else {
               agMarkerMapper.deleteUserMarker(userId,markId);
           }
            return;
        }else {
            return;

        }


    }
    public void saveMarker(AgMarker agMarker,String userId) throws Exception {
        if(agMarker!=null&&userId!=null){
            AgUserMarker agUserMarker=new AgUserMarker();
            agUserMarker.setId(UUID.randomUUID().toString());
            agUserMarker.setMarkId(agMarker.getId());
            agUserMarker.setUserId(userId);
            agUserMarker.setShareTime(new Date());
            // 自己创建为1
            agUserMarker.setViewState("1");
            agMarkerMapper.saveMarker(agMarker);
            agMarkerMapper.saveUserMarker(agUserMarker);
        }else {
            return;
        }



    }
    public void updateMarker(AgMarker agMarker,String userId) throws Exception {
        if(agMarker!=null&&userId!=null)return;
        agMarkerMapper.updateMarker(agMarker);
    }

    @Override
    public void readMarker(String userId, String markId) throws Exception{
        AgUserMarker userMarker = agMarkerMapper.getUserMarker(userId, markId);
        // 已查看标注为1
        userMarker.setViewState("1");
        agMarkerMapper.updateUserMarker(userMarker);
    }

    @Override
    public AgUserMarker getUserMarker(String userId, String markId) throws Exception {
        return agMarkerMapper.getUserMarker(userId,markId);
    }

    @Override
    public List<AgUserMarker> getUserMarkerByUserAndView(String userId, String viewState) throws Exception {
        return agMarkerMapper.getUserMarkerByUserAndView(userId,viewState);
    }

    @Override
    public void saveMarkRemind(AgMarkRemind agMarkRemind) throws Exception {
        agMarkerMapper.saveMarkRemind(agMarkRemind);
    }

    @Override
    public void updateMarkRemind(AgMarkRemind agMarkRemind) throws Exception {
        agMarkerMapper.updateMarkRemind(agMarkRemind);
    }

    @Override
    public AgMarkRemind findMarkRemindByUserId(String userId) {
        return agMarkerMapper.findMarkRemindByUserId(userId);
    }
}
