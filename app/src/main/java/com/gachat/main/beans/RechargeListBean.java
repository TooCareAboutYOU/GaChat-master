package com.gachat.main.beans;

import java.io.Serializable;
import java.util.List;

/**
 - charge_goods: 充值商品列表
     - charge_good_id: 商品ID
     - price: 价格，单位元
     - diamond: 钻石数
     - send_diamond： 赠送钻石数
 */
public class RechargeListBean implements Serializable {

    private List<GoodsInfoBean> charge_goods;

    public RechargeListBean() {
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"charge_goods\":")
                .append(charge_goods);
        sb.append('}');
        return sb.toString();
    }

    public List<GoodsInfoBean> getCharge_goods() {
        return charge_goods;
    }

    public void setCharge_goods(List<GoodsInfoBean> charge_goods) {
        this.charge_goods = charge_goods;
    }

    public static class GoodsInfoBean{
        private int charge_good_id;
        private double price;
        private int diamond;
        private int send_diamond;

        public GoodsInfoBean() {
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("{");
            sb.append("\"charge_good_id\":")
                    .append(charge_good_id);
            sb.append(",\"price\":")
                    .append(price);
            sb.append(",\"diamond\":")
                    .append(diamond);
            sb.append(",\"send_diamond\":")
                    .append(send_diamond);
            sb.append('}');
            return sb.toString();
        }

        public int getCharge_good_id() {
            return charge_good_id;
        }

        public void setCharge_good_id(int charge_good_id) {
            this.charge_good_id = charge_good_id;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getDiamond() {
            return diamond;
        }

        public void setDiamond(int diamond) {
            this.diamond = diamond;
        }

        public int getSend_diamond() {
            return send_diamond;
        }

        public void setSend_diamond(int send_diamond) {
            this.send_diamond = send_diamond;
        }
    }
}
