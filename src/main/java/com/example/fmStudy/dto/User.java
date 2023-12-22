package com.example.fmStudy.dto;

/**
 * <pre>
 *
 * </pre>
 *
 * @author 王蕾<br>
 * <b>mail</b> wanglei@syncsoft.com.cn<br>
 * <b>date</b> 2023年12月20日<br>
 * @version 1.0.0
 *
 * <pre>
 * 修改记录
 *  版本号		修订日期		修改人		bug编号		修改内容
 *  1.0.0		2023/12/20	王蕾	    		新建
 * </pre>
 */
public class User {
    private String id;
    private String userName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
