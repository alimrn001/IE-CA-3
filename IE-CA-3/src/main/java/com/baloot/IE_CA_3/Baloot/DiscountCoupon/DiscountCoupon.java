package com.baloot.IE_CA_3.Baloot.DiscountCoupon;

public class DiscountCoupon {

    String discountCode;

    int discount;


    public DiscountCoupon(String discountCode, int discount) {
        this.discountCode = discountCode;
        this.discount = discount;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public int getDiscount() {
        return discount;
    }

    public double getDiscountRatio() {
        return (double)discount/100;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

}
