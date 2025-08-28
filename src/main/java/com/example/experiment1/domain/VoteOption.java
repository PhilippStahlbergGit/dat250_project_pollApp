package com.example.experiment1.domain;

public class VoteOption {
    private String caption;
    private int presentationOrder;

    public VoteOption() {}

    public String getCaption() {
        return this.caption;
    }
    public void setCaption( String caption ) {
        this.caption = caption;
    }

    public int getPresentationOrder() {
        return this.presentationOrder;
    }
    public void setPresentationOrder( int presentationOrder ) {
        this.presentationOrder = presentationOrder;
    }
}
