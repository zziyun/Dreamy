package com.cookandroid.dream;

public class DiaryVO {
    /*dno     INTEGER PRIMARY KEY AUTOINCREMENT,
    ddate   TEXT,
    dtitle TEXT,
    dcontent text,
    idpasswd TEXT,
    phone TEXT,
    id TEXT,
    FOREIGN KEY(id) REFERENCES user(id)*/
    private String ddate;
    private String dmood;
    private String dcontent;
    private String id;
    private String no;

    public DiaryVO() {
        this.ddate = ddate;
        this.dmood = dmood;
        this.dcontent = dcontent;
        this.id = id;
        this.no = no;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getDdate() {
        return ddate;
    }

    public void setDdate(String ddate) {
        this.ddate = ddate;
    }

    public String getDmood() {
        return dmood;
    }

    public void setDmood(String dmood) {
        this.dmood = dmood;
    }

    public String getDcontent() {
        return dcontent;
    }

    public void setDcontent(String dcontent) {
        this.dcontent = dcontent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
