package com.example.yelp;

public class Deal {
    String deal_id;
    String deal_title;
    String deal_url;
    String deal_start;

    public Deal(String... info) {
        deal_id = info[0];
        deal_title = info[1];
        deal_url = info[2];
        deal_start = info[3];
    }

    public void setDeal(String... info) {
        deal_id = info[0];
        deal_title = info[1];
        deal_url = info[2];
        deal_start = info[3];
    }

}
