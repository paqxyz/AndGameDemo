package cn.ollyice.framework.info;

import java.util.List;
import java.util.Map;

/**
 * Created by ollyice on 2018/6/19.
 */

public interface IHashMap {
    //可空字段key值
    List<String> canEmptyKeys();
    //是否可以发送给服务器
    boolean success();
    //转换成hashMap
    Map<String,String> hashMap();
}
