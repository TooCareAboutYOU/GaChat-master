package com.gachat.main.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zs on 2018/4/26.
 */
public class NewsBean implements Serializable {

    private int count;
    private List<GiftsBean> gifts;

    public NewsBean() {
    }

    public static class GiftsBean implements Serializable{
        private int gift_id;
        private String create_time;
        private FromBean user_from;
        private ToBean user_to;
        private FromAssert user_from_asset;

        public GiftsBean() {
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("{");
            sb.append("\"gift_id\":")
                    .append(gift_id);
            sb.append(",\"create_time\":\"")
                    .append(create_time).append('\"');
            sb.append(",\"user_from\":")
                    .append(user_from);
            sb.append(",\"user_to\":")
                    .append(user_to);
            sb.append(",\"user_from_asset\":")
                    .append(user_from_asset);
            sb.append('}');
            return sb.toString();
        }

        public static class FromBean implements Serializable{
            private int uid;
            private String username;

            public FromBean() {
            }

            @Override
            public String toString() {
                final StringBuilder sb = new StringBuilder("{");
                sb.append("\"uid\":")
                        .append(uid);
                sb.append(",\"username\":\"")
                        .append(username).append('\"');
                sb.append('}');
                return sb.toString();
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }
        }

        public static class ToBean implements Serializable{
            private int uid;
            private String username;

            public ToBean() {
            }

            @Override
            public String toString() {
                final StringBuilder sb = new StringBuilder("{");
                sb.append("\"uid\":")
                        .append(uid);
                sb.append(",\"username\":\"")
                        .append(username).append('\"');
                sb.append('}');
                return sb.toString();
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }
        }

        public static class FromAssert implements Serializable{
            private String image_url;
            private String name;

            public FromAssert() {
            }

            @Override
            public String toString() {
                final StringBuilder sb = new StringBuilder("{");
                sb.append("\"image_url\":\"")
                        .append(image_url).append('\"');
                sb.append(",\"name\":\"")
                        .append(name).append('\"');
                sb.append('}');
                return sb.toString();
            }

            public String getImage_url() {
                return image_url;
            }

            public void setImage_url(String image_url) {
                this.image_url = image_url;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }


        public int getGift_id() {
            return gift_id;
        }

        public void setGift_id(int gift_id) {
            this.gift_id = gift_id;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public FromBean getUser_from() {
            return user_from;
        }

        public void setUser_from(FromBean user_from) {
            this.user_from = user_from;
        }

        public ToBean getUser_to() {
            return user_to;
        }

        public void setUser_to(ToBean user_to) {
            this.user_to = user_to;
        }

        public FromAssert getUser_from_asset() {
            return user_from_asset;
        }

        public void setUser_from_asset(FromAssert user_from_asset) {
            this.user_from_asset = user_from_asset;
        }
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<GiftsBean> getGifts() {
        return gifts;
    }

    public void setGifts(List<GiftsBean> gifts) {
        this.gifts = gifts;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"count\":")
                .append(count);
        sb.append(",\"gifts\":")
                .append(gifts);
        sb.append('}');
        return sb.toString();
    }
}
