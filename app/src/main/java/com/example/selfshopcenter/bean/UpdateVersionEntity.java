package com.example.selfshopcenter.bean;

public class UpdateVersionEntity {

    /**
     * code : success
     * appid : keengee
     * appname : UPDATEVERSION
     * msg : 操作成功
     * maxPage : 1
     * currentPage : 0
     * Reqguid : 7a30516e-6077-499d-80d5-57f80fe8b716
     * data : {"KHid":"11","UpdateTime":"2020-01-13 00:00:00","V_Updatepath":"http://www.ikengee.com.cn/testexample/ZCZYUSE.apk","V_VERSION":"1"}
     */

    private String code;
    private String appid;
    private String appname;
    private String msg;
    private int maxPage;
    private int currentPage;
    private String Reqguid;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * KHid : 11
         * UpdateTime : 2020-01-13 00:00:00
         * V_Updatepath : http://www.ikengee.com.cn/testexample/ZCZYUSE.apk
         * V_VERSION : 1
         */

        private String KHid;
        private String UpdateTime;
        private String V_Updatepath;
        private int V_VERSION;

        public String getKHid() {
            return KHid;
        }

        public void setKHid(String KHid) {
            this.KHid = KHid;
        }

        public String getUpdateTime() {
            return UpdateTime;
        }

        public void setUpdateTime(String UpdateTime) {
            this.UpdateTime = UpdateTime;
        }

        public String getV_Updatepath() {
            return V_Updatepath;
        }

        public void setV_Updatepath(String V_Updatepath) {
            this.V_Updatepath = V_Updatepath;
        }

        public int getV_VERSION() {
            return V_VERSION;
        }

        public void setV_VERSION(int V_VERSION) {
            this.V_VERSION = V_VERSION;
        }
    }
}
