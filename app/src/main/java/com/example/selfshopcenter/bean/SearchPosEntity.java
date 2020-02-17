package com.example.selfshopcenter.bean;

public class SearchPosEntity {
    /**
     * code : success
     * appid : keengee
     * appname : DEVICECONTROL
     * msg : 操作成功
     * maxPage : 1
     * currentPage : 0
     * Reqguid : 8085ac47-2c55-4c16-a19f-d227ae652dc9
     * data : {"posstatus":"1"}
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
         * posstatus : 1
         */

        private String posstatus;

        public String getPosstatus() {
            return posstatus;
        }

        public void setPosstatus(String posstatus) {
            this.posstatus = posstatus;
        }
    }
}
