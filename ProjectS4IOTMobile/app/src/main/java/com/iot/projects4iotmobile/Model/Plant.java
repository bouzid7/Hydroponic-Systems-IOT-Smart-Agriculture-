package com.iot.projects4iotmobile.Model;

import java.io.Serializable;
import java.net.PortUnreachableException;
import java.util.ArrayList;

public class Plant  implements Serializable {
    private  String name;
    private String  info;
    private String image;
    private String isFavorite;

    public Plant(String name, String info, String image,String isFavorite) {
        this.name = name;
        this.info = info;
        this.image = image;
        this.isFavorite=isFavorite;
    }

    public String getImage() {
        return image;
    }

    public String getInfo() {
        return info;
    }

    public String getName() {
        return name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsFavorite(){
        return this.isFavorite;
    }

    public void setIsFavorite(String isFavorite) {
        this.isFavorite = isFavorite;
    }

}
