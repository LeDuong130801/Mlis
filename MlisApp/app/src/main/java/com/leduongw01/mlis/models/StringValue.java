package com.leduongw01.mlis.models;

public class StringValue {
    String non;
    String variantBody;

    public StringValue(String non, String variantBody) {
        this.non = non;
        this.variantBody = variantBody;
    }

    public String getNon() {
        return non;
    }

    public void setNon(String non) {
        this.non = non;
    }

    public String getVariantBody() {
        return variantBody;
    }

    public void setVariantBody(String variantBody) {
        this.variantBody = variantBody;
    }
}
