package com.gachat.main.beans;

import java.io.Serializable;
import java.util.List;

/**
 - Created by zs on 2018/4/24.- room_id: 房间ID
 - index: 房间序号
 - name: 名称
 - price: 房间价格，抓一次消耗多少钻石
 - machine: 机器
 - image_url: 房间图片
 - type: 房间类型 待定
 - valid: 是否有效 待定
 - max_user_count: 房间最大容纳人数
 - room_sku: 房间物品信息
     - name: 物品名称
     - desc: 物品描述
     - image_url: 物品图片
 */
public class DollListBean implements Serializable {

    private int count;
    private List<RoomListBean> rooms;

    public DollListBean() {
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"count\":")
                .append(count);
        sb.append(",\"rooms\":")
                .append(rooms);
        sb.append('}');
        return sb.toString();
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<RoomListBean> getRooms() {
        return rooms;
    }

    public void setRooms(List<RoomListBean> rooms) {
        this.rooms = rooms;
    }

    public static class RoomListBean{
        private int room_id;
        private int index;
        private String name;
        private int status;
        private int type;
        private int valid;
        private int max_user_count;
        private String image_url;
        private int price;
        private int machine;
        private RoomSkuBean room_sku;

        public RoomListBean() {
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("{");
            sb.append("\"room_id\":")
                    .append(room_id);
            sb.append(",\"index\":")
                    .append(index);
            sb.append(",\"name\":\"")
                    .append(name).append('\"');
            sb.append(",\"status\":")
                    .append(status);
            sb.append(",\"type\":")
                    .append(type);
            sb.append(",\"valid\":")
                    .append(valid);
            sb.append(",\"max_user_count\":")
                    .append(max_user_count);
            sb.append(",\"image_url\":\"")
                    .append(image_url).append('\"');
            sb.append(",\"price\":")
                    .append(price);
            sb.append(",\"machine\":")
                    .append(machine);
            sb.append(",\"room_sku\":")
                    .append(room_sku);
            sb.append('}');
            return sb.toString();
        }

        public int getRoom_id() {
            return room_id;
        }

        public void setRoom_id(int room_id) {
            this.room_id = room_id;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getValid() {
            return valid;
        }

        public void setValid(int valid) {
            this.valid = valid;
        }

        public int getMax_user_count() {
            return max_user_count;
        }

        public void setMax_user_count(int max_user_count) {
            this.max_user_count = max_user_count;
        }

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getMachine() {
            return machine;
        }

        public void setMachine(int machine) {
            this.machine = machine;
        }

        public RoomSkuBean getRoom_sku() {
            return room_sku;
        }

        public void setRoom_sku(RoomSkuBean room_sku) {
            this.room_sku = room_sku;
        }

        public static class RoomSkuBean {
            private String name;
            private String desc;
            private String image_url;

            public RoomSkuBean() {
            }

            @Override
            public String toString() {
                final StringBuilder sb = new StringBuilder("{");
                sb.append("\"name\":\"")
                        .append(name).append('\"');
                sb.append(",\"desc\":\"")
                        .append(desc).append('\"');
                sb.append(",\"image_url\":\"")
                        .append(image_url).append('\"');
                sb.append('}');
                return sb.toString();
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getImage_url() {
                return image_url;
            }

            public void setImage_url(String image_url) {
                this.image_url = image_url;
            }
        }
    }

}
