package com.gachat.main.beans;

import java.io.Serializable;
import java.util.List;

public class DollBannerBean implements Serializable {

    private List<BannerBean> banners;


    public DollBannerBean() {
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"banners\":")
                .append(banners);
        sb.append('}');
        return sb.toString();
    }

    public List<BannerBean> getBanners() {
        return banners;
    }

    public void setBanners(List<BannerBean> banners) {
        this.banners = banners;
    }

    public static class BannerBean{
        /**
         - banner_id: 轮播图uid≈
         - create_time: 创建时间
         — index: 序号
         - desc: 描述≈
         - image_url: 图片链接
         */

        private int banner_id;
        private String create_time;
        private int index;
        private String desc;
        private String image_url;

        public BannerBean() {
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("{");
            sb.append("\"banner_id\":")
                    .append(banner_id);
            sb.append(",\"create_time\":\"")
                    .append(create_time).append('\"');
            sb.append(",\"index\":")
                    .append(index);
            sb.append(",\"desc\":\"")
                    .append(desc).append('\"');
            sb.append(",\"image_url\":\"")
                    .append(image_url).append('\"');
            sb.append('}');
            return sb.toString();
        }

        public int getBanner_id() {
            return banner_id;
        }

        public void setBanner_id(int banner_id) {
            this.banner_id = banner_id;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
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
