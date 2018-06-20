package cn.ollyice.framework.info;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ollyice on 2018/6/19.
 * 游戏角色信息
 */

public class ExtraInfo implements IHashMap{
    private static final int SUBMIT_EXTRA_ERROR = 0;
    public static final int SUBMIT_EXTRA_ENTER_GAME = 1;//进入游戏
    public static final int SUBMIT_EXTRA_LEVEL_UP = 2;//角色升级
    public static final int SUBMIT_EXTRA_ENTER_COPY = 3;//进入副本
    public static final int SUBMIT_EXTRA_LEAVE_COPY = 4;//离开副本
    public static final int SUBMIT_EXTRA_CREATE_ROLE = 5;//创建角色

    private int dataType = SUBMIT_EXTRA_ERROR;
    private String cpUid = "";//CP方UID
    private String roleID = "";//角色id
    private String roleName = "";//角色名称
    private String roleLevel = "";//角色等级
    private String serverID = "";//服务器Id
    private String serverName = "";//服务器名称
    private String moneyNum = "";//角色铜币数量 不是虚拟游戏货币数量
    private String vip = "0";//vip级别
    private String union = "暂无";//工会
    private long createRoleTime = 0;//角色创建时间

    private HashMap<String,String> mHashMap = new HashMap<>();

    public int getDataType() {
        return dataType;
    }

    public String getCpUid() {
        return cpUid;
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

    public String getServerID() {
        return serverID;
    }

    public String getServerName() {
        return serverName;
    }

    public String getMoneyNum() {
        return moneyNum;
    }

    public String getVip() {
        return vip;
    }

    public String getUnion() {
        return union;
    }

    public long getCreateRoleTime() {
        return createRoleTime;
    }

    public ExtraInfo setDataType(int dataType) {
        this.dataType = dataType;
        mHashMap.put("dataType",dataType + "");
        return this;
    }

    public ExtraInfo setCpUid(String cpUid) {
        this.cpUid = cpUid;
        mHashMap.put("cpUid",cpUid);
        return this;
    }

    public ExtraInfo setRoleID(String roleID) {
        this.roleID = roleID;
        mHashMap.put("roleID",roleID);
        return this;
    }

    public ExtraInfo setRoleName(String roleName) {
        this.roleName = roleName;
        mHashMap.put("roleName",roleName);
        return this;
    }

    public ExtraInfo setRoleLevel(String roleLevel) {
        this.roleLevel = roleLevel;
        mHashMap.put("roleLevel",roleLevel);
        return this;
    }

    public ExtraInfo setServerID(String serverID) {
        this.serverID = serverID;
        mHashMap.put("serverID",serverID);
        return this;
    }

    public ExtraInfo setServerName(String serverName) {
        this.serverName = serverName;
        mHashMap.put("serverName",serverName);
        return this;
    }

    public ExtraInfo setMoneyNum(String moneyNum) {
        this.moneyNum = moneyNum;
        mHashMap.put("moneyNum",moneyNum);
        return this;
    }

    public ExtraInfo setVip(String vip) {
        this.vip = vip;
        mHashMap.put("vip",vip);
        return this;
    }

    public ExtraInfo setUnion(String union) {
        this.union = union;
        mHashMap.put("union",union);
        return this;
    }

    public ExtraInfo setCreateRoleTime(long createRoleTime) {
        this.createRoleTime = createRoleTime;
        mHashMap.put("createRoleTime",createRoleTime + "");
        return this;
    }

    @Override
    public List<String> canEmptyKeys() {
        return null;
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
