package com.example.selfshopcenter.bean;

public class AuthInfoEntity {

    /**
     * code : success
     * appid : keengee
     * appname : KHLOGIN
     * msg : 操作成功
     * maxPage : 1
     * currentPage : 0
     * Reqguid : d487870d-8445-4e1f-8485-b6d9fa7356cd
     * data : {"khid":"KH1911190000001"}
     */

    private String code;
    private String appid;
    private String appname;
    private String msg;
    private int maxPage;
    private int currentPage;
    private String Reqguid;
    private AuthInfoEntity.DataBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public String getReqguid() {
        return Reqguid;
    }

    public void setReqguid(String Reqguid) {
        this.Reqguid = Reqguid;
    }

    public AuthInfoEntity.DataBean getData() {
        return data;
    }

    public void setData(AuthInfoEntity.DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**

         * code : 200
         * msg : 111
         */


        private String code;
        private String msg;


        public String getCode() {
            return code;
        }

        public void setCode(String khid) {
            this.code = khid;
        }


        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.code = msg;
        }

    }

}
