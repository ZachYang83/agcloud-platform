package com.augurit.agcloud.agcom.agsupport.sc.address.service.impl;

import com.augurit.agcloud.agcom.agsupport.sc.address.dao.AgAddressDao;
import com.augurit.agcloud.agcom.agsupport.sc.address.service.IAgAddress;
import com.common.thread.Executer;
import com.common.thread.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Created by Augurit on 2017-07-06.
 */
@Service
public class AgAddressImpl implements IAgAddress {
    @Autowired
    private AgAddressDao agAddressDao;

    /**
     * 匹配地址
     * @param address
     * @return
     */
    @Override
    public List<Map> matchAddress(String address)  throws Exception {
//        AgAddressDao agAddressDao = new AgAddressDao();
        return agAddressDao.findAgDmdz(address);
    }

    @Override
    public Map<String,List<Map>> matchAddressList(List<String> addressList) throws Exception {
        Map<String,List<Map>> rsMap = new HashMap<>();
        if (addressList != null && addressList.size() > 0) {
            int rNum = Runtime.getRuntime().availableProcessors();
            int nThreads = rNum > 1 ? (rNum - 1) : rNum;
            int size = addressList.size();
            Executer exe = new Executer(nThreads);
            List<Future<Map<String,List<Map>> >> futures = new ArrayList<Future<Map<String,List<Map>> >>(nThreads);
            for (int i = 0; i < nThreads; i++) {
                final List<String> subList = addressList.subList(size / nThreads * i, (nThreads == (i + 1) ? size : size / nThreads * (i + 1)));
                Job<Map<String,List<Map>> > task = new Job<Map<String,List<Map>>>() {
                    public Map execute() throws Exception {
                        Map<String,List<Map>> subResult = null;
                        for (String address : subList) {
                            if(subResult==null){
                                subResult = new HashMap();
                            }
                            List<Map> addressMap = matchAddress(address);
                            subResult.put(address, addressMap);
                        }
                        return subResult;
                    }
                };
                futures.add(exe.fork(task));
            }
            for (Future<Map<String,List<Map>>> future : futures) {
                if (future.get() != null) {
                    Map<String,List<Map>> map= future.get();
                    rsMap.putAll(map);
                }
            }
            exe.close();
        }
        return rsMap;
    }

    public static void main(String[] args) throws Exception{
        AgAddressImpl agAddressImpl = new AgAddressImpl();
        String address = "香洲拱北九洲大道西1043号北岭社区警务室";
        String address1 = "广省珠海市香洲区拱北街道港一路38号19栋";
        String address2 = "广东省珠海市斗门区井岸镇滨江路427号";
        String address3 = "广东省珠海市斗门区井岸镇港霞东路30号1栋";
        List<String> addressList = new ArrayList<>();
//        addressList.add(address);
        addressList.add(address1);
        addressList.add(address2);
        addressList.add(address3);
        List<Map> rsList = agAddressImpl.matchAddress(address1);
        Map<String,List<Map>> resultList = agAddressImpl.matchAddressList(addressList);

        Map addressMap = new HashMap();
        addressMap.put("city","珠海市");
        addressMap.put("addname","广省珠海市香洲区拱北街道港一路38号19栋");
        String addressCol = "addname";
        Map addressMap2 = new HashMap();
        addressMap2.put("city","珠海市");
        addressMap2.put("addname","广东省珠海市斗门区井岸镇滨江路427号");
        List<Map> resultList2 = agAddressImpl.matchAddressMap(addressMap,addressCol);
        List<Map> agAddressMapList = new ArrayList<>();
        agAddressMapList.add(addressMap);
        agAddressMapList.add(addressMap2);
        Map<String,List<Map>> resultList3 = agAddressImpl.matchAddressMapList(agAddressMapList,addressCol);
        System.out.println("--end--");
    }

    @Override
    public List<Map> matchAddressMap(Map addressMap, String addressCol) throws Exception{
        if(addressMap!=null&&addressMap.get(addressCol)!=null){
            String address = addressMap.get(addressCol)+"";
            List<Map> rsList = matchAddress(address);
            return rsList;
        }
        return null;
    }

    @Override
    public Map<String,List<Map>> matchAddressMapList(List<Map> addressObjList, String addressCol) throws Exception{
        Map<String,List<Map>> rsMap = new HashMap<>();
        if(addressObjList!=null&&addressObjList.size()>0){
            int rNum = Runtime.getRuntime().availableProcessors();
            int nThreads = rNum > 1 ? (rNum - 1) : rNum;
            int size = addressObjList.size();
            Executer exe = new Executer(nThreads);
            List<Future<Map<String,List<Map>>>> futures = new ArrayList<Future<Map<String,List<Map>>>>(nThreads);
            for (int i = 0; i < nThreads; i++) {
                final List<Map> subList = addressObjList.subList(size / nThreads * i, (nThreads == (i + 1) ? size : size / nThreads * (i + 1)));
                Job<Map<String,List<Map>>> task = new Job<Map<String,List<Map>>>() {
                    public Map<String,List<Map>> execute() throws Exception {
                        Map<String,List<Map>> subResult = null;
                        for (Map addressMap : subList) {
                            if(subResult==null){
                                subResult = new HashMap();
                            }
                            String address = addressMap.get(addressCol)+"";
                            List<Map> addressMap2 = matchAddress(address);
                            subResult.put(address, addressMap2);
                        }
                        return subResult;
                    }
                };
                futures.add(exe.fork(task));
            }
            for (Future<Map<String,List<Map>>> future : futures) {
                if (future.get() != null) {
                    Map<String,List<Map>> map= future.get();
                    rsMap.putAll(map);
                }
            }
            exe.close();
            return rsMap;
        }
        return null;
    }


}
