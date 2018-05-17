package com.gachat.main.beans;

import java.io.Serializable;
import java.util.List;

/**
 * {  获取礼物列表
 */

public class GiftListBean implements Serializable {


    private int count;
    private List<AssetsBean> assets;


    public GiftListBean() {
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"count\":")
                .append(count);
        sb.append(",\"assets\":")
                .append(assets);
        sb.append('}');
        return sb.toString();
    }

    public static class AssetsBean{
        private int user_asset_id;   //用户物品记录 ID
        private String create_time;   //获得时间
        private String expire_time;  //过期时间
        private ItemBean item;
        private int status;         //   0-拥有-抓获，1-拥有-好友赠与，2-失去-赠与好友，3-失去-准备发货，4-失去-已发货，5-失去-已过期
        private ExpressBean express;
        private int user;           // 拥有用户
        private String source;     //娃娃来源


        public AssetsBean() {
        }



        public static class ItemBean{
            private String name;      //  物品名
            private String image_url;   //  物品图片URL

            public ItemBean() {
            }

            @Override
            public String toString() {
                final StringBuilder sb = new StringBuilder("{");
                sb.append("\"name\":\"")
                        .append(name).append('\"');
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

            public String getImage_url() {
                return image_url;
            }

            public void setImage_url(String image_url) {
                this.image_url = image_url;
            }
        }

        public static class ExpressBean{
            private String express_sn;  //  快递单号

            public ExpressBean() {
            }

            @Override
            public String toString() {
                final StringBuilder sb = new StringBuilder("{");
                sb.append("\"express_sn\":\"")
                        .append(express_sn).append('\"');
                sb.append('}');
                return sb.toString();
            }

            public String getExpress_sn() {
                return express_sn;
            }

            public void setExpress_sn(String express_sn) {
                this.express_sn = express_sn;
            }
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("{");
            sb.append("\"user_asset_id\":")
                    .append(user_asset_id);
            sb.append(",\"create_time\":\"")
                    .append(create_time).append('\"');
            sb.append(",\"expire_time\":\"")
                    .append(expire_time).append('\"');
            sb.append(",\"item\":")
                    .append(item);
            sb.append(",\"status\":")
                    .append(status);
            sb.append(",\"express\":")
                    .append(express);
            sb.append(",\"user\":")
                    .append(user);
            sb.append(",\"source\":\"")
                    .append(source).append('\"');
            sb.append('}');
            return sb.toString();
        }

        public int getUser_asset_id() {
            return user_asset_id;
        }

        public void setUser_asset_id(int user_asset_id) {
            this.user_asset_id = user_asset_id;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getExpire_time() {
            return expire_time;
        }

        public void setExpire_time(String expire_time) {
            this.expire_time = expire_time;
        }

        public ItemBean getItem() {
            return item;
        }

        public void setItem(ItemBean item) {
            this.item = item;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public ExpressBean getExpress() {
            return express;
        }

        public void setExpress(ExpressBean express) {
            this.express = express;
        }

        public int getUser() {
            return user;
        }

        public void setUser(int user) {
            this.user = user;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<AssetsBean> getAssets() {
        return assets;
    }

    public void setAssets(List<AssetsBean> assets) {
        this.assets = assets;
    }
}
