package com.adityagupta.nxtgaruda.data;

public class TabInfo {
    String title, json;

    public TabInfo(String title, String json) {
        this.title = title;
        this.json = json;
    }

    public String getTitle() {
        return title;
    }

    public String getJson() {
        return json;
    }
}
