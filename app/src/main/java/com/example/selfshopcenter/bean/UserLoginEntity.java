package com.example.selfshopcenter.bean;

import java.io.Serializable;

public class UserLoginEntity implements Serializable {

    /**
     * code : success
     * appid : keengee
     * appname : KHLOGIN
     * msg : 操作成功
     * maxPage : 1
     * currentPage : 0
     * Reqguid : d487870d-8445-4e1f-8485-b6d9fa7356cd
     * data : {"client_type":"ZY","khid":"KH1911190000001","sname":"1119商家门店测试","posid":"3"}
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
         {
         "client_type": "ZY",
         "khid": "KH1911190000001",
         "sname": "1119商家门店测试",
         "zfbappid": "2015020500030465",
         "wxappid": "1",
         "wxshid": "1345898901",
         "posid": "2"
         }
         */

        private String client_type;
        private String khid;
        private String sname;
        private String posid;
        private String zfbappid;
        private String wxappid;
        private String wxshid;

        public String getClient_type() {
            return client_type;
        }

        public void setClient_type(String client_type) {
            this.client_type = client_type;
        }

        public String getKhid() {
            return khid;
        }

        public void setKhid(String khid) {
            this.khid = khid;
        }

        public String getSname() {
            return sname;
        }

        public void setSname(String sname) {
            this.sname = sname;
        }

        public String getPosid() {
            return posid;
        }

        public void setPosid(String posid) {
            this.posid = posid;
        }

        public String getZfbappid() {
            return zfbappid;
        }

        public void setZfbappid(String zfbappid) {
            this.zfbappid = zfbappid;
        }

        public String getWxshid() {
            return wxshid;
        }

        public void setWxshid(String posid) {
            this.wxshid = wxshid;
        }


        public String getWxappid() {
            return wxappid;
        }

        public void setWxappid(String wxappid) {
            this.wxappid = wxappid;
        }
    }
}
