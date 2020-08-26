package com.cookandroid.teamproject2;

public class ListItem {

    String dtitle, ddate;
    int did;
    public ListItem() {

    }

    public int getDid() {
        return did;
    }

    public void setDid(int did) {
        this.did = did;
    }




    public String getDtitle() {
        return dtitle;
    }

    public String getDdate() {
        return ddate;
    }

    public void setDtitle(String dtitle) {
        this.dtitle = dtitle;
    }

    public void setDdate(String ddate) {
        this.ddate = ddate;
    }

    ListItem(String dtitle, String ddate){
        this.dtitle=dtitle;
        this.ddate=ddate;
    }
}

