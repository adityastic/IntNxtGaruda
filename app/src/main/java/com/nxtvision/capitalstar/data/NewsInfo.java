package com.nxtvision.capitalstar.data;

import java.util.Date;

public class NewsInfo {
    public String title,desc,link;
    public Date date;

    public NewsInfo(String title, String desc, Date date,String link) {
        this.title = title;
        this.desc = desc;
        this.date = date;
        this.link = link;
    }
}
