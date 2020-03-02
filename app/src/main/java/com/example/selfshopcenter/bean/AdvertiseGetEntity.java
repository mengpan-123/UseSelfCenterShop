package com.example.selfshopcenter.bean;

public class AdvertiseGetEntity {
    /**
     * code : success
     * appid : keengee
     * appname : GETADVERTISE
     * msg : 操作成功
     * maxPage : 1
     * currentPage : 0
     * Reqguid : 2d6996ac-9fd6-4225-ab01-9ba5c2bf5699
     * data : {"path":"http://wgpay.zczyofficial.com/Document/Temp/2020-02-27e3187e89-e91e-49e5-9763-7efd8822ae81.mp4"}
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
         * path : http://wgpay.zczyofficial.com/Document/Temp/2020-02-27e3187e89-e91e-49e5-9763-7efd8822ae81.mp4
         */

        private String path;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
}
