package com.nxtvision.capitalstar.data;

import java.util.Date;

public class PanelInfo {
    public String file,info,link;
    public Date date;

    public PanelInfo(String file, String info, String link, Date date) {
        this.file = file;
        this.info = info;
        this.link = link;
        this.date = date;
    }
}
