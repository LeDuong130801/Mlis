package com.leduongw01.mlis.models;

public class StringValue {
    String id;
    String text1;
    String text2;

    public StringValue() {
    }

    public StringValue(String id, String text1, String text2) {
        this.id = id;
        this.text1 = text1;
        this.text2 = text2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }
}
