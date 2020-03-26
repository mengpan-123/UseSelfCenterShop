package com.example.selfshopcenter.bean;

import java.util.List;

public class SearchOrderEntity {
    /**
     * code : success
     * appid : keengee
     * appname : NEWPRINT
     * msg : 操作成功
     * maxPage : 1
     * currentPage : 0
     * Reqguid : 610c7592-5f28-4bed-b9a7-b26adc32885f
     * data : {"totAmount":0.01,"disAmount":0,"realAmount":0.01,"payVal":0.01,"khid":"007","khsname":"测试企业合江店","totQty":1,"transId":"0079120200213165034","trans_xsTime":"2020-02-13 16:52:17","outTransId":"2020021322001463521418862096","pluMap":[{"itemId":1,"barcode":"6923807030251","goodsId":"1001","pluName":"测试54g波力卷（鸡蛋）","pluPrice":0.01,"pluQty":1,"pluAmount":0.01,"RealAmount":0.01,"pluDis":0,"nWeight":0}],"payMap":{"payType":"07","payVal":"0.01","payTypeName":"支付宝支付"}}
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
         * totAmount : 0.01
         * disAmount : 0
         * realAmount : 0.01
         * payVal : 0.01
         * khid : 007
         * khsname : 测试企业合江店
         * totQty : 1
         * transId : 0079120200213165034
         * trans_xsTime : 2020-02-13 16:52:17
         * outTransId : 2020021322001463521418862096
         * pluMap : [{"itemId":1,"barcode":"6923807030251","goodsId":"1001","pluName":"测试54g波力卷（鸡蛋）","pluPrice":0.01,"pluQty":1,"pluAmount":0.01,"RealAmount":0.01,"pluDis":0,"nWeight":0}]
         * payMap : {"payType":"07","payVal":"0.01","payTypeName":"支付宝支付"}
         */

        private double totAmount;
        private double disAmount;
        private double realAmount;
        private double payVal;
        private String khid;
        private String khsname;
        private int totQty;
        private String transId;
        private String trans_xsTime;
        private String outTransId;
        private PayMapBean payMap;
        private List<PluMapBean> pluMap;

        public double getTotAmount() {
            return totAmount;
        }

        public void setTotAmount(double totAmount) {
            this.totAmount = totAmount;
        }

        public double getDisAmount() {
            return disAmount;
        }

        public void setDisAmount(double disAmount) {
            this.disAmount = disAmount;
        }

        public double getRealAmount() {
            return realAmount;
        }

        public void setRealAmount(double realAmount) {
            this.realAmount = realAmount;
        }

        public double getPayVal() {
            return payVal;
        }

        public void setPayVal(double payVal) {
            this.payVal = payVal;
        }

        public String getKhid() {
            return khid;
        }

        public void setKhid(String khid) {
            this.khid = khid;
        }

        public String getKhsname() {
            return khsname;
        }

        public void setKhsname(String khsname) {
            this.khsname = khsname;
        }

        public int getTotQty() {
            return totQty;
        }

        public void setTotQty(int totQty) {
            this.totQty = totQty;
        }

        public String getTransId() {
            return transId;
        }

        public void setTransId(String transId) {
            this.transId = transId;
        }

        public String getTrans_xsTime() {
            return trans_xsTime;
        }

        public void setTrans_xsTime(String trans_xsTime) {
            this.trans_xsTime = trans_xsTime;
        }

        public String getOutTransId() {
            return outTransId;
        }

        public void setOutTransId(String outTransId) {
            this.outTransId = outTransId;
        }

        public PayMapBean getPayMap() {
            return payMap;
        }

        public void setPayMap(PayMapBean payMap) {
            this.payMap = payMap;
        }

        public List<PluMapBean> getPluMap() {
            return pluMap;
        }

        public void setPluMap(List<PluMapBean> pluMap) {
            this.pluMap = pluMap;
        }

        public static class PayMapBean {
            /**
             * payType : 07
             * payVal : 0.01
             * payTypeName : 支付宝支付
             */

            private String payType;
            private String payVal;
            private String payTypeName;

            public String getPayType() {
                return payType;
            }

            public void setPayType(String payType) {
                this.payType = payType;
            }

            public String getPayVal() {
                return payVal;
            }

            public void setPayVal(String payVal) {
                this.payVal = payVal;
            }

            public String getPayTypeName() {
                return payTypeName;
            }

            public void setPayTypeName(String payTypeName) {
                this.payTypeName = payTypeName;
            }
        }

        public static class PluMapBean {
            /**
             * itemId : 1
             * barcode : 6923807030251
             * goodsId : 1001
             * pluName : 测试54g波力卷（鸡蛋）
             * pluPrice : 0.01
             * pluQty : 1
             * pluAmount : 0.01
             * RealAmount : 0.01
             * pluDis : 0
             * nWeight : 0
             */

            private int itemId;
            private String barcode;
            private String goodsId;
            private String pluName;
            private double pluPrice;
            private int pluQty;
            private double pluAmount;
            private double RealAmount;
            private double pluDis;
            private double nWeight;

            public int getItemId() {
                return itemId;
            }

            public void setItemId(int itemId) {
                this.itemId = itemId;
            }

            public String getBarcode() {
                return barcode;
            }

            public void setBarcode(String barcode) {
                this.barcode = barcode;
            }

            public String getGoodsId() {
                return goodsId;
            }

            public void setGoodsId(String goodsId) {
                this.goodsId = goodsId;
            }

            public String getPluName() {
                return pluName;
            }

            public void setPluName(String pluName) {
                this.pluName = pluName;
            }

            public double getPluPrice() {
                return pluPrice;
            }

            public void setPluPrice(double pluPrice) {
                this.pluPrice = pluPrice;
            }

            public int getPluQty() {
                return pluQty;
            }

            public void setPluQty(int pluQty) {
                this.pluQty = pluQty;
            }

            public double getPluAmount() {
                return pluAmount;
            }

            public void setPluAmount(double pluAmount) {
                this.pluAmount = pluAmount;
            }

            public double getRealAmount() {
                return RealAmount;
            }

            public void setRealAmount(double RealAmount) {
                this.RealAmount = RealAmount;
            }

            public double getPluDis() {
                return pluDis;
            }

            public void setPluDis(double pluDis) {
                this.pluDis = pluDis;
            }

            public double getNWeight() {
                return nWeight;
            }

            public void setNWeight(double nWeight) {
                this.nWeight = nWeight;
            }
        }
    }


//    /**
//     * code : success
//     * appid : keengee
//     * appname : NEWPRINT
//     * msg : 操作成功
//     * maxPage : 1
//     * currentPage : 0
//     * Reqguid : 10892944-0cdf-49cd-acca-341c75f77d5c
//     * data : {"totAmount":2.05,"disAmount":0,"realAmount":2.05,"payVal":2.05,"khid":"007","khsname":"合江店","totQty":2,"transId":"0079320200116100209","outTransId":"2020011622001451291411537142","pluMap":[{"itemId":1,"barcode":"6954144504471","goodsId":"40887","pluName":"鲁晶精制盐","pluPrice":0.8,"pluQty":1,"pluAmount":0.8,"RealAmount":0.8,"pluDis":0,"nWeight":0},{"itemId":2,"barcode":"0000274020315","goodsId":"41007","pluName":"馒头卡花","pluPrice":1.25,"pluQty":1,"pluAmount":1.25,"RealAmount":1.25,"pluDis":0,"nWeight":0}],"payMap":{"payType":"ZF07","payVal":"2.05","payTypeName":"支付宝支付"}}
//     */
//
//    private String code;
//    private String appid;
//    private String appname;
//    private String msg;
//    private int maxPage;
//    private int currentPage;
//    private String Reqguid;
//    private DataBean data;
//
//    public String getCode() {
//        return code;
//    }
//
//    public void setCode(String code) {
//        this.code = code;
//    }
//
//    public String getAppid() {
//        return appid;
//    }
//
//    public void setAppid(String appid) {
//        this.appid = appid;
//    }
//
//    public String getAppname() {
//        return appname;
//    }
//
//    public void setAppname(String appname) {
//        this.appname = appname;
//    }
//
//    public String getMsg() {
//        return msg;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }
//
//    public int getMaxPage() {
//        return maxPage;
//    }
//
//    public void setMaxPage(int maxPage) {
//        this.maxPage = maxPage;
//    }
//
//    public int getCurrentPage() {
//        return currentPage;
//    }
//
//    public void setCurrentPage(int currentPage) {
//        this.currentPage = currentPage;
//    }
//
//    public String getReqguid() {
//        return Reqguid;
//    }
//
//    public void setReqguid(String Reqguid) {
//        this.Reqguid = Reqguid;
//    }
//
//    public DataBean getData() {
//        return data;
//    }
//
//    public void setData(DataBean data) {
//        this.data = data;
//    }
//
//    public static class DataBean {
//        /**
//         * totAmount : 2.05
//         * disAmount : 0
//         * realAmount : 2.05
//         * payVal : 2.05
//         * khid : 007
//         * khsname : 合江店
//         * totQty : 2
//         * trans_xsTime  2020-01-16 10:05:22
//         * transId : 0079320200116100209
//         * outTransId : 2020011622001451291411537142
//         * pluMap : [{"itemId":1,"barcode":"6954144504471","goodsId":"40887","pluName":"鲁晶精制盐","pluPrice":0.8,"pluQty":1,"pluAmount":0.8,"RealAmount":0.8,"pluDis":0,"nWeight":0},{"itemId":2,"barcode":"0000274020315","goodsId":"41007","pluName":"馒头卡花","pluPrice":1.25,"pluQty":1,"pluAmount":1.25,"RealAmount":1.25,"pluDis":0,"nWeight":0}]
//         * payMap : {"payType":"ZF07","payVal":"2.05","payTypeName":"支付宝支付"}
//         */
//
//        private double totAmount;
//        private int disAmount;
//        private double realAmount;
//        private double payVal;
//        private String khid;
//        private String khsname;
//        private int totQty;
//        private String transId;
//        private String xsTime;
//        private String outTransId;
//        private PayMapBean payMap;
//        private List<PluMapBean> pluMap;
//
//        public double getTotAmount() {
//            return totAmount;
//        }
//
//        public void setTotAmount(double totAmount) {
//            this.totAmount = totAmount;
//        }
//
//        public int getDisAmount() {
//            return disAmount;
//        }
//
//        public void setDisAmount(int disAmount) {
//            this.disAmount = disAmount;
//        }
//
//        public double getRealAmount() {
//            return realAmount;
//        }
//
//        public void setRealAmount(double realAmount) {
//            this.realAmount = realAmount;
//        }
//
//        public double getPayVal() {
//            return payVal;
//        }
//
//        public void setPayVal(double payVal) {
//            this.payVal = payVal;
//        }
//
//        public String getKhid() {
//            return khid;
//        }
//
//        public void setKhid(String khid) {
//            this.khid = khid;
//        }
//
//        public String getKhsname() {
//            return khsname;
//        }
//
//        public void setKhsname(String khsname) {
//            this.khsname = khsname;
//        }
//
//        public int getTotQty() {
//            return totQty;
//        }
//
//        public void setTotQty(int totQty) {
//            this.totQty = totQty;
//        }
//
//        public String getXsTime() {
//            return xsTime;
//        }
//
//        public void setXsTime(String xsTime) {
//            this.xsTime = xsTime;
//        }
//
//        public String getTransId() {
//            return transId;
//        }
//
//        public void setTransId(String transId) {
//            this.transId = transId;
//        }
//
//        public String getOutTransId() {
//            return outTransId;
//        }
//
//        public void setOutTransId(String outTransId) {
//            this.outTransId = outTransId;
//        }
//
//        public PayMapBean getPayMap() {
//            return payMap;
//        }
//
//        public void setPayMap(PayMapBean payMap) {
//            this.payMap = payMap;
//        }
//
//        public List<PluMapBean> getPluMap() {
//            return pluMap;
//        }
//
//        public void setPluMap(List<PluMapBean> pluMap) {
//            this.pluMap = pluMap;
//        }
//
//        public static class PayMapBean {
//            /**
//             * payType : ZF07
//             * payVal : 2.05
//             * payTypeName : 支付宝支付
//             */
//
//            private String payType;
//            private String payVal;
//            private String payTypeName;
//
//            public String getPayType() {
//                return payType;
//            }
//
//            public void setPayType(String payType) {
//                this.payType = payType;
//            }
//
//            public String getPayVal() {
//                return payVal;
//            }
//
//            public void setPayVal(String payVal) {
//                this.payVal = payVal;
//            }
//
//            public String getPayTypeName() {
//                return payTypeName;
//            }
//
//            public void setPayTypeName(String payTypeName) {
//                this.payTypeName = payTypeName;
//            }
//        }
//
//        public static class PluMapBean {
//            /**
//             * itemId : 1
//             * barcode : 6954144504471
//             * goodsId : 40887
//             * pluName : 鲁晶精制盐
//             * pluPrice : 0.8
//             * pluQty : 1
//             * pluAmount : 0.8
//             * RealAmount : 0.8
//             * pluDis : 0
//             * nWeight : 0
//             */
//
//            private int itemId;
//            private String barcode;
//            private String goodsId;
//            private String pluName;
//            private double pluPrice;
//            private int pluQty;
//            private double pluAmount;
//            private double RealAmount;
//            private int pluDis;
//            private int nWeight;
//
//            public int getItemId() {
//                return itemId;
//            }
//
//            public void setItemId(int itemId) {
//                this.itemId = itemId;
//            }
//
//            public String getBarcode() {
//                return barcode;
//            }
//
//            public void setBarcode(String barcode) {
//                this.barcode = barcode;
//            }
//
//            public String getGoodsId() {
//                return goodsId;
//            }
//
//            public void setGoodsId(String goodsId) {
//                this.goodsId = goodsId;
//            }
//
//            public String getPluName() {
//                return pluName;
//            }
//
//            public void setPluName(String pluName) {
//                this.pluName = pluName;
//            }
//
//            public double getPluPrice() {
//                return pluPrice;
//            }
//
//            public void setPluPrice(double pluPrice) {
//                this.pluPrice = pluPrice;
//            }
//
//            public int getPluQty() {
//                return pluQty;
//            }
//
//            public void setPluQty(int pluQty) {
//                this.pluQty = pluQty;
//            }
//
//            public double getPluAmount() {
//                return pluAmount;
//            }
//
//            public void setPluAmount(double pluAmount) {
//                this.pluAmount = pluAmount;
//            }
//
//            public double getRealAmount() {
//                return RealAmount;
//            }
//
//            public void setRealAmount(double RealAmount) {
//                this.RealAmount = RealAmount;
//            }
//
//            public int getPluDis() {
//                return pluDis;
//            }
//
//            public void setPluDis(int pluDis) {
//                this.pluDis = pluDis;
//            }
//
//            public int getNWeight() {
//                return nWeight;
//            }
//
//            public void setNWeight(int nWeight) {
//                this.nWeight = nWeight;
//            }
//        }
//    }
}
