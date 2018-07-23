package com.adityagupta.nxtgaruda.data;

public class ServicesInfo {
    public String name,desc;
    public double monthly,quaterly,hyearly,yearly;

    public ServicesInfo(String name, String desc, double monthly, double quaterly, double hyearly, double yearly) {
        this.name = name;
        this.desc = desc;
        this.monthly = monthly;
        this.quaterly = quaterly;
        this.hyearly = hyearly;
        this.yearly = yearly;
    }
}
