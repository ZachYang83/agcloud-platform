package com.augurit.agcloud.agcom.agsupport.sc.address.util;

import com.augurit.agcloud.agcom.agsupport.sc.address.config.AgAddressConfig;
import com.common.util.Common;

import java.util.HashMap;
import java.util.Map;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017-10-10.
 */
public class AddressMap<K, V> extends HashMap<K, V> implements Comparable<AddressMap<K, V>> {

    public AddressMap() {

    }

    public AddressMap(Map<K, V> map) {
        for (K key : map.keySet()) {
            this.put(key, map.get(key));
        }
    }

    @Override
    public int compareTo(AddressMap o) {
        int oLd = Common.checkInt(o.get(AgAddressConfig.SEMB_COL), AgAddressConfig.MAX_SEMB);
        int ld = Common.checkInt(this.get(AgAddressConfig.SEMB_COL), AgAddressConfig.MAX_SEMB);
        return ld - oLd;
    }
}
