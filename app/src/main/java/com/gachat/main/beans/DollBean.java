package com.gachat.main.beans;

import java.io.Serializable;
import java.util.List;

/**
 * 抓娃娃首页
 */
public class DollBean implements Serializable {
    private List<DollBannerBean.BannerBean> mBannerBeanList;
    private List<DollListBean> mListBeanList;

    public DollBean() {
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"mBannerBeanList\":")
                .append(mBannerBeanList);
        sb.append(",\"mListBeanList\":")
                .append(mListBeanList);
        sb.append('}');
        return sb.toString();
    }

    public List<DollBannerBean.BannerBean> getBannerBeanList() {
        return mBannerBeanList;
    }

    public void setBannerBeanList(List<DollBannerBean.BannerBean> bannerBeanList) {
        mBannerBeanList = bannerBeanList;
    }

    public List<DollListBean> getListBeanList() {
        return mListBeanList;
    }

    public void setListBeanList(List<DollListBean> listBeanList) {
        mListBeanList = listBeanList;
    }
}