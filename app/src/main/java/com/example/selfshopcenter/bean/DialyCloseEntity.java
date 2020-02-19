package com.example.selfshopcenter.bean;

public class DialyCloseEntity {
    /**
     * code : success
     * appid : keengee
     * appname : DIALYCLOSE
     * msg : 操作成功
     * maxPage : 1
     * currentPage : 0
     * Reqguid : 8ea1e575-9502-4613-ae83-41346f20fdec
     * data : {"totaldisc":"0","totalnet":"0","totalcount":"0","wxnet":"0","zfbnet":"0"}
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
         * totaldisc : 0
         * totalnet : 0
         * totalcount : 0
         * wxnet : 0
         * zfbnet : 0
         */

        private String totaldisc;
        private String totalnet;
        private String totalcount;
        private String wxnet;
        private String zfbnet;

        public String getTotaldisc() {
            return totaldisc;
        }

        public void setTotaldisc(String totaldisc) {
            this.totaldisc = totaldisc;
        }

        public String getTotalnet() {
            return totalnet;
        }

        public void setTotalnet(String totalnet) {
            this.totalnet = totalnet;
        }

        public String getTotalcount() {
            return totalcount;
        }

        public void setTotalcount(String totalcount) {
            this.totalcount = totalcount;
        }

        public String getWxnet() {
            return wxnet;
        }

        public void setWxnet(String wxnet) {
            this.wxnet = wxnet;
        }

        public String getZfbnet() {
            return zfbnet;
        }

        public void setZfbnet(String zfbnet) {
            this.zfbnet = zfbnet;
        }
    }
}
