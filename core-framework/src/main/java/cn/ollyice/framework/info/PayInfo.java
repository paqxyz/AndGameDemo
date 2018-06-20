package cn.ollyice.framework.info;

import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ollyice on 2018/6/19.
 */

public class PayInfo implements IHashMap{
    private final String[] EMPTY = new String[]{
            "payNotifyUrl","extension"
    };
    private String productId = "";//	充值商品ID，游戏内的商品ID
    private String productName = "";//	商品名称，比如100元宝，500钻石...
    private String productDesc = "";//	商品描述，比如 充值100元宝，赠送20元宝
    private String price = "";//	充值金额(单位：分)
    private String orderId = "";//  订单id
    private int buyNum = 1;//	购买数量，一般都是1
    private String coinNum = "";//	玩家当前身上剩余的游戏币
    private String serverID = "";//	玩家所在服务器的ID
    private String serverName = "";//	玩家所在服务器的名称
    private String roleID = "";//	玩家角色ID
    private String roleName = "";//	玩家角色名称
    private String roleLevel = "";//	玩家角色等级
    private String vip = "";//玩家vip等级
    private String payNotifyUrl = "";//	游戏服务器支付回调地址，渠道SDK支付成功，异步通知Server，Server根据该地址，通知游戏服务器发货
    private String extension = "";//	支付成功之后，原样返回给游戏服务器

    private Map<String,String> mHashMap = new HashMap<>();

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public String getPrice() {
        return price;
    }

    public String getOrderId() {
        return orderId;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public String getCoinNum() {
        return coinNum;
    }

    public String getServerID() {
        return serverID;
    }

    public String getServerName() {
        return serverName;
    }

    public String getRoleID() {
        return roleID;
    }

    public String getRoleName() {
        return roleName;
    }

    public String getRoleLevel() {
        return roleLevel;
    }

    public String getVip() {
        return vip;
    }

    public String getPayNotifyUrl() {
        return payNotifyUrl;
    }

    public String getExtension() {
        return extension;
    }

    public PayInfo setProductId(String productId) {
        this.productId = productId;
        mHashMap.put("productId",productId);
        return this;
    }

    public PayInfo setProductName(String productName) {
        this.productName = productName;
        mHashMap.put("productName",productName);
        return this;
    }

    public PayInfo setProductDesc(String productDesc) {
        this.productDesc = productDesc;
        mHashMap.put("productDesc",productDesc);
        return this;
    }

    public PayInfo setPrice(String price) {
        this.price = price;
        mHashMap.put("price",price);
        return this;
    }

    public PayInfo setOrderId(String orderId) {
        this.orderId = orderId;
        mHashMap.put("orderId",orderId);
        return this;
    }

    public PayInfo setBuyNum(int buyNum) {
        this.buyNum = buyNum;
        mHashMap.put("buyNum",buyNum + "");
        return this;
    }

    public PayInfo setCoinNum(String coinNum) {
        this.coinNum = coinNum;
        mHashMap.put("coinNum",coinNum);
        return this;
    }

    public PayInfo setServerID(String serverID) {
        this.serverID = serverID;
        mHashMap.put("serverID",serverID);
        return this;
    }

    public PayInfo setServerName(String serverName) {
        this.serverName = serverName;
        mHashMap.put("serverName",serverName);
        return this;
    }

    public PayInfo setRoleID(String roleID) {
        this.roleID = roleID;
        mHashMap.put("roleID",roleID);
        return this;
    }

    public PayInfo setRoleName(String roleName) {
        this.roleName = roleName;
        mHashMap.put("roleName",roleName);
        return this;
    }

    public PayInfo setRoleLevel(String roleLevel) {
        this.roleLevel = roleLevel;
        mHashMap.put("roleLevel",roleLevel);
        return this;
    }

    public PayInfo setVip(String vip) {
        this.vip = vip;
        mHashMap.put("vip",vip);
        return this;
    }

    public PayInfo setPayNotifyUrl(String payNotifyUrl) {
        this.payNotifyUrl = payNotifyUrl;
        if (!TextUtils.isEmpty(payNotifyUrl)) {
            mHashMap.put("payNotifyUrl", payNotifyUrl);
        }
        return this;
    }

    public PayInfo setExtension(String extension) {
        this.extension = extension;
        mHashMap.put("extension", extension);
        return this;
    }

    @Override
    public List<String> canEmptyKeys() {
        return Arrays.asList(EMPTY);
    }

    @Override
    public boolean success() {
        return HashMapUtils.isEmptyValue(hashMap(),canEmptyKeys());
    }

    @Override
    public Map<String, String> hashMap() {
        return mHashMap;
    }
}
