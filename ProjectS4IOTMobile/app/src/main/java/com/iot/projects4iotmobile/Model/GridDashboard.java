package com.iot.projects4iotmobile.Model;

public class GridDashboard {
    private String data_name;
    private int imgid;

    public GridDashboard(String data_name, int imgid) {
        this.data_name = data_name;
        this.imgid = imgid;
    }

    public String getData_name() {
        return data_name;
    }

    public void setData_name(String data_name) {
        this.data_name = data_name;
    }

    public int getImgid() {
        return imgid;
    }

    public void setImgid(int imgid) {
        this.imgid = imgid;
    }

}
