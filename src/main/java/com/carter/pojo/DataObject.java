package com.carter.pojo;

public class DataObject {
    private int timeFlag;
    private int temperature;
    private int humidity;
    private int sf6;

    public DataObject(int timeFlag, int temperature, int humidity, int sf6) {
        this.timeFlag = timeFlag;
        this.temperature = temperature;
        this.humidity = humidity;
        this.sf6 = sf6;
    }

    public int getTimeFlag() {
        return timeFlag;
    }

    public void setTimeFlag(int timeFlag) {
        this.timeFlag = timeFlag;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getSf6() {
        return sf6;
    }

    public void setSf6(int sf6) {
        this.sf6 = sf6;
    }
}
