package com.example.selfshopcenter.bean;

import java.io.Serializable;
import java.util.List;

public class AddGoodsEntity implements Serializable {
    /**
     * code : success
     * appid : keengee
     * appname : GetGoodsInfo
     * msg : 操作成功
     * maxPage : 1
     * currentPage : 0
     * Reqguid : bc6e1f7c-6594-43af-9bd7-55ce93d540af
     * data : {"khid":"KH1911220000001","totAmount":"299","disAmount":"0","leaveNote":"","totalQty":2,"orderNumber":"KH1911220000001120200106145805","itemsList":[{"disrule":"暂无折扣","discmoney":"0","items":[{"barcode":"2001109013371","dprice":"11","price":"12","qyid":"2019112200001","sname":"草莓星座蛋糕10寸","spid":"1090100006","unit":"1","weight":"","disc":"0","net":"288","qty":"1"},{"barcode":"2591738625454","dprice":"11","price":"12","qyid":"2019112200001","sname":"香片","spid":"10100029","unit":"个","weight":"","disc":"0","net":"11","qty":"1"}]}]}
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
         * khid : KH1911220000001
         * totAmount : 299
         * disAmount : 0
         * leaveNote :
         * totalQty : 2
         * orderNumber : KH1911220000001120200106145805
         * itemsList : [{"disrule":"暂无折扣","discmoney":"0","items":[{"barcode":"2001109013371","dprice":"11","price":"12","qyid":"2019112200001","sname":"草莓星座蛋糕10寸","spid":"1090100006","unit":"1","weight":"","disc":"0","net":"288","qty":"1"},{"barcode":"2591738625454","dprice":"11","price":"12","qyid":"2019112200001","sname":"香片","spid":"10100029","unit":"个","weight":"","disc":"0","net":"11","qty":"1"}]}]
         */

        private String khid;
        private String totAmount;
        private String disAmount;
        private String leaveNote;
        private int totalQty;
        private String orderNumber;
        private List<ItemsListBean> itemsList;

        public String getKhid() {
            return khid;
        }

        public void setKhid(String khid) {
            this.khid = khid;
        }

        public String getTotAmount() {
            return totAmount;
        }

        public void setTotAmount(String totAmount) {
            this.totAmount = totAmount;
        }

        public String getDisAmount() {
            return disAmount;
        }

        public void setDisAmount(String disAmount) {
            this.disAmount = disAmount;
        }

        public String getLeaveNote() {
            return leaveNote;
        }

        public void setLeaveNote(String leaveNote) {
            this.leaveNote = leaveNote;
        }

        public int getTotalQty() {
            return totalQty;
        }

        public void setTotalQty(int totalQty) {
            this.totalQty = totalQty;
        }

        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        public List<ItemsListBean> getItemsList() {
            return itemsList;
        }

        public void setItemsList(List<ItemsListBean> itemsList) {
            this.itemsList = itemsList;
        }

        public static class ItemsListBean {
            /**
             * disrule : 暂无折扣
             * discmoney : 0
             * items : [{"barcode":"2001109013371","dprice":"11","price":"12","qyid":"2019112200001","sname":"草莓星座蛋糕10寸","spid":"1090100006","unit":"1","weight":"","disc":"0","net":"288","qty":"1"},{"barcode":"2591738625454","dprice":"11","price":"12","qyid":"2019112200001","sname":"香片","spid":"10100029","unit":"个","weight":"","disc":"0","net":"11","qty":"1"}]
             */

            private String disrule;
            private String discmoney;
            private List<ItemsBean> items;

            public String getDisrule() {
                return disrule;
            }

            public void setDisrule(String disrule) {
                this.disrule = disrule;
            }

            public String getDiscmoney() {
                return discmoney;
            }

            public void setDiscmoney(String discmoney) {
                this.discmoney = discmoney;
            }

            public List<ItemsBean> getItems() {
                return items;
            }

            public void setItems(List<ItemsBean> items) {
                this.items = items;
            }

            public static class ItemsBean {
                /**
                 * barcode : 2001109013371
                 * dprice : 11
                 * price : 12
                 * qyid : 2019112200001
                 * sname : 草莓星座蛋糕10寸
                 * spid : 1090100006
                 * unit : 1
                 * weight :
                 * disc : 0
                 * net : 288
                 * qty : 1
                 */

                private String barcode;
                private String dprice;
                private String price;
                private String qyid;
                private String sname;
                private String spid;
                private String unit;
                private String weight;
                private double disc;
                private String net;
                private int qty;

                public String getBarcode() {
                    return barcode;
                }

                public void setBarcode(String barcode) {
                    this.barcode = barcode;
                }

                public String getDprice() {
                    return dprice;
                }

                public void setDprice(String dprice) {
                    this.dprice = dprice;
                }

                public String getPrice() {
                    return price;
                }

                public void setPrice(String price) {
                    this.price = price;
                }

                public String getQyid() {
                    return qyid;
                }

                public void setQyid(String qyid) {
                    this.qyid = qyid;
                }

                public String getSname() {
                    return sname;
                }

                public void setSname(String sname) {
                    this.sname = sname;
                }

                public String getSpid() {
                    return spid;
                }

                public void setSpid(String spid) {
                    this.spid = spid;
                }

                public String getUnit() {
                    return unit;
                }

                public void setUnit(String unit) {
                    this.unit = unit;
                }

                public String getWeight() {
                    return weight;
                }

                public void setWeight(String weight) {
                    this.weight = weight;
                }

                public Double getDisc() {
                    return disc;
                }

                public void setDisc(Double disc) {
                    this.disc = disc;
                }

                public String getNet() {
                    return net;
                }

                public void setNet(String net) {
                    this.net = net;
                }

                public int getQty() {
                    return qty;
                }

                public void setQty(int qty) {
                    this.qty = qty;
                }
            }
        }
    }
}
