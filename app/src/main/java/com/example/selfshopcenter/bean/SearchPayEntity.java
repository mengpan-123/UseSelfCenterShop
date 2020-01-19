package com.example.selfshopcenter.bean;

public class SearchPayEntity {

    /**
     * code : fail
     * appid : keengee
     * appname : QUERYORDER
     * msg : -1
     * maxPage : 1
     * currentPage : 0
     * Reqguid : bbe99fa4-0284-4d74-a412-5baeba327f70
     * data : {"paycode":"-100","errmsg":"-1","out_trad_no":""}
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
         * paycode : -100
         * errmsg : -1
         * out_trad_no :
         */

        private String paycode;
        private String errmsg;
        private String out_trad_no;

        public String getPaycode() {
            return paycode;
        }

        public void setPaycode(String paycode) {
            this.paycode = paycode;
        }

        public String getErrmsg() {
            return errmsg;
        }

        public void setErrmsg(String errmsg) {
            this.errmsg = errmsg;
        }

        public String getOut_trad_no() {
            return out_trad_no;
        }

        public void setOut_trad_no(String out_trad_no) {
            this.out_trad_no = out_trad_no;
        }
    }

}
