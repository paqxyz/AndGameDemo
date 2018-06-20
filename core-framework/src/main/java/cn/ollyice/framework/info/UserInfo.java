package cn.ollyice.framework.info;

/**
 * Created by ollyice on 2018/6/19.
 */

public class UserInfo {
    private String userId;// 用户Id 唯一标示
    private String userName;// 用户名
    private String userToken;// 用户凭证

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserToken() {
        return userToken;
    }

    public UserInfo setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public UserInfo setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public UserInfo setUserToken(String userToken) {
        this.userToken = userToken;
        return this;
    }
}
