package com.iot.projects4iotmobile.Model;

public class DashboardData {

    private String ph;
    private String temp;
    private String hum;
    private String ppm;
    private String levelWater;
   // private String statusPumpWater;

    public DashboardData(String ph, String temp, String hum, String ppm, String levelWater) {
    this.ph = ph;
    this.temp = temp;
    this.hum = hum;
    this.ppm = ppm;
    this.levelWater = levelWater;
  //  this.statusPumpWater = status;

    }

     public String getPh() {
        return this.ph;
    }

    public String getTemp() {
        return temp;
    }

    public String getHum() {
        return hum;
    }

    public String getPpm() {
        return ppm;
    }

    public String getLevelWater() {
        return levelWater;
    }

    public void setPh(String ph) {
        this.ph = ph;
    }

    public void setHum(String hum) {
        this.hum = hum;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public void setPpm(String ppm) {
        this.ppm = ppm;
    }

    public void setLevelWater(String levelWater) {
        this.levelWater = levelWater;
    }

   /* public String getStatusPumpWater(){
        return this.statusPumpWater;
    }

    public void setStatusPumpWater(String statusPumpWater) {
        this.statusPumpWater = statusPumpWater;
    }*/

}
