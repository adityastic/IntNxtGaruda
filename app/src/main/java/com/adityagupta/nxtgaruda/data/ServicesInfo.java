package com.adityagupta.nxtgaruda.data;

import java.io.Serializable;

public class ServicesInfo implements Serializable {
    private String name, desc;
    private double monthly, quaterly, hyearly, yearly;

    public ServicesInfo(String name, String desc, double monthly, double quaterly, double hyearly, double yearly) {
        this.name = name;
        this.desc = desc;
        this.monthly = monthly;
        this.quaterly = quaterly;
        this.hyearly = hyearly;
        this.yearly = yearly;
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

    public double getMonthly() {
        return monthly;
    }

    public void setMonthly(double monthly) {
        this.monthly = monthly;
    }

    public double getQuaterly() {
        return quaterly;
    }

    public void setQuaterly(double quaterly) {
        this.quaterly = quaterly;
    }

    public double getHyearly() {
        return hyearly;
    }

    public void setHyearly(double hyearly) {
        this.hyearly = hyearly;
    }

    public double getYearly() {
        return yearly;
    }

    public void setYearly(double yearly) {
        this.yearly = yearly;
    }
}
