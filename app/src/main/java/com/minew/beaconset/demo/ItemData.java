package com.minew.beaconset.demo;

public class ItemData {
    private int iv_profile; //이미지 뷰
    private String item_n;   //아이템 이름

    public ItemData(int iv_profile, String item_name) {
        this.iv_profile = iv_profile;
        this.item_n = item_name;
    }

    public int getIv_profile() {
        return iv_profile;
    }

    public void setIv_profile(int iv_profile) {
        this.iv_profile = iv_profile;
    }

    public String getItem_name() {
        return item_n;
    }

    public void setItem_name(String item_name) {
        this.item_n = item_name;
    }
}