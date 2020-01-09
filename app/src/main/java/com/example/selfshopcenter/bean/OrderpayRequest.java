package com.example.selfshopcenter.bean;

import java.util.List;

public class OrderpayRequest {

    /**
     * appid : zczytest
     * apiname : ORDERPAY
     * req_operator : zp
     * data : {"khid":"KH1911220000001","posid":"1","xstype":"1","payWay":"WXSmall","paytype":"2","paycode":"289374016359850569","appid":"2015020500030465","wxshid":"1345898901","prepayId":"K210QTD0012020010219394988888","pluMap":[{"goodsId":"1090100006","barcode":"6906907113246","pluQty":"2","pluPrice":"48","realPrice":70,"pluAmount":70,"pluDis":70},{"goodsId":"10100029","barcode":"6906907113246","pluQty":"1","pluPrice":"48","realPrice":70,"pluAmount":70,"pluDis":70}],"payMap":[{"paySn":"","payTypeId":2,"payVal":0.01}]}
     */

    private String appid;
    private String apiname;
    private String req_operator;
    private DataBean data;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getApiname() {
        return apiname;
    }

    public void setApiname(String apiname) {
        this.apiname = apiname;
    }

    public String getReq_operator() {
        return req_operator;
    }

    public void setReq_operator(String req_operator) {
        this.req_operator = req_operator;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * khid : KH1911220000001
         * posid : 1
         * xstype : 1
         * payWay : WXSmall
         * paytype : 2
         * paycode : 289374016359850569
         * appid : 2015020500030465
         * wxshid : 1345898901
         * prepayId : K210QTD0012020010219394988888
         * pluMap : [{"goodsId":"1090100006","barcode":"6906907113246","pluQty":"2","pluPrice":"48","realPrice":70,"pluAmount":70,"pluDis":70},{"goodsId":"10100029","barcode":"6906907113246","pluQty":"1","pluPrice":"48","realPrice":70,"pluAmount":70,"pluDis":70}]
         * payMap : [{"paySn":"","payTypeId":2,"payVal":0.01}]
         */

        private String khid;
        private String posid;
        private String xstype;
        private String payWay;
        private String paytype;
        private String paycode;
        private String appid;
        private String wxshid;
        private String prepayId;
        private List<PluMapBean> pluMap;
        private List<PayMapBean> payMap;

        public String getKhid() {
            return khid;
        }

        public void setKhid(String khid) {
            this.khid = khid;
        }

        public String getPosid() {
            return posid;
        }

        public void setPosid(String posid) {
            this.posid = posid;
        }

        public String getXstype() {
            return xstype;
        }

        public void setXstype(String xstype) {
            this.xstype = xstype;
        }

        public String getPayWay() {
            return payWay;
        }

        public void setPayWay(String payWay) {
            this.payWay = payWay;
        }

        public String getPaytype() {
            return paytype;
        }

        public void setPaytype(String paytype) {
            this.paytype = paytype;
        }

        public String getPaycode() {
            return paycode;
        }

        public void setPaycode(String paycode) {
            this.paycode = paycode;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getWxshid() {
            return wxshid;
        }

        public void setWxshid(String wxshid) {
            this.wxshid = wxshid;
        }

        public String getPrepayId() {
            return prepayId;
        }

        public void setPrepayId(String prepayId) {
            this.prepayId = prepayId;
        }

        public List<PluMapBean> getPluMap() {
            return pluMap;
        }

        public void setPluMap(List<PluMapBean> pluMap) {
            this.pluMap = pluMap;
        }

        public List<PayMapBean> getPayMap() {
            return payMap;
        }

        public void setPayMap(List<PayMapBean> payMap) {
            this.payMap = payMap;
        }

        public static class PluMapBean {
            /**
             * goodsId : 1090100006
             * barcode : 6906907113246
             * pluQty : 2
             * pluPrice : 48
             * realPrice : 70
             * pluAmount : 70
             * pluDis : 70
             */

            private String goodsId;
            private String barcode;
            private int pluQty;
            private String pluPrice;
            private double realPrice;
            private double pluAmount;
            private double pluDis;

            public String getGoodsId() {
                return goodsId;
            }

            public void setGoodsId(String goodsId) {
                this.goodsId = goodsId;
            }

            public String getBarcode() {
                return barcode;
            }

            public void setBarcode(String barcode) {
                this.barcode = barcode;
            }

            public int getPluQty() {
                return pluQty;
            }

            public void setPluQty(int  pluQty) { this.pluQty = pluQty; }

            public String getPluPrice() {
                return pluPrice;
            }

            public void setPluPrice(String pluPrice) {
                this.pluPrice = pluPrice;
            }

            public double getRealPrice() {
                return realPrice;
            }

            public void setRealPrice(double realPrice) {
                this.realPrice = realPrice;
            }

            public double getPluAmount() {
                return pluAmount;
            }

            public void setPluAmount(double pluAmount) {
                this.pluAmount = pluAmount;
            }

            public double getPluDis() {
                return pluDis;
            }

            public void setPluDis(double pluDis) {
                this.pluDis = pluDis;
            }
        }

        public static class PayMapBean {
            /**
             * paySn :
             * payTypeId : 2
             * payVal : 0.01
             */

            private String paySn;
            private String payTypeId;
            private double payVal;

            public String getPaySn() {
                return paySn;
            }

            public void setPaySn(String paySn) {
                this.paySn = paySn;
            }

            public String getPayTypeId() {
                return payTypeId;
            }

            public void setPayTypeId(String  payTypeId) {
                this.payTypeId = payTypeId;
            }

            public double getPayVal() {
                return payVal;
            }

            public void setPayVal(double payVal) {
                this.payVal = payVal;
            }
        }
    }

}
