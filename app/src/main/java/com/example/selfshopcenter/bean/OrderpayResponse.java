package com.example.selfshopcenter.bean;

public class OrderpayResponse {

    /**
     * code : success
     * appid : keengee
     * appname : KHLOGIN
     * msg : 操作成功
     * maxPage : 1
     * currentPage : 0
     * Reqguid : d487870d-8445-4e1f-8485-b6d9fa7356cd
     * data : {
     * "paycode":"200",
     * "out_trad_no":"11111111111",
     * "errmsg":""
     * }
     */

    private String code;
    private String appid;
    private String appname;
    private String msg;
    private int maxPage;
    private int currentPage;
    private String Reqguid;
    private OrderpayResponse.DataBean data;

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

         {
         * "paycode":"200",
         * "out_trad_no":"11111111111",
         * "errmsg":""
         * }
         */


        private String paycode;
        private String out_trad_no;
        private String errmsg;

        public String getPaycode() {
            return paycode;
        }

        public void setPaycode(String paycode) {
            this.paycode = paycode;
        }

        public String getOut_trad_no() {
            return out_trad_no;
        }

        public void setOut_trad_no(String out_trad_no) {
            this.out_trad_no = out_trad_no;
        }

        public String getErrmsg() {
            return errmsg;
        }

        public void setErrmsg(String paycode) {
            this.errmsg = errmsg;
        }

    }
}
