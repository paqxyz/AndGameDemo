package cn.ollyice.framework.info;

import android.text.TextUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by ollyice on 2018/6/19.
 */

class HashMapUtils {
    static boolean isEmptyValue(Map<String,String> map, List<String> emptyList){
        for (String key : map.keySet()) {
            if (TextUtils.isEmpty(key)) {//如果key为空  直接返回时空值
                return true;
            }

            String value = map.get(key);
            if (TextUtils.isEmpty(value) && !TextUtils.isEmpty(key)) {
                if (emptyList != null && !emptyList.contains(key)) {
                    return true;
                }
            }
        }
        return false;
    }
}
