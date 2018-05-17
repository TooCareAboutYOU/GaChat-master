package com.gachat.main.beans;

import android.view.SurfaceView;

/**
 * 抓娃娃视频
 */
public class RemoteVideoBean {
    private String streamId;
    private SurfaceView mSurfaceView;

    public RemoteVideoBean() {

    }

    public RemoteVideoBean(String streamId, SurfaceView surfaceView) {
        this.streamId = streamId;
        mSurfaceView = surfaceView;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"streamId\":\"")
                .append(streamId).append('\"');
        sb.append(",\"mSurfaceView\":")
                .append(mSurfaceView);
        sb.append('}');
        return sb.toString();
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public SurfaceView getSurfaceView() {
        return mSurfaceView;
    }

    public void setSurfaceView(SurfaceView surfaceView) {
        mSurfaceView = surfaceView;
    }
}
