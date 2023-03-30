package com.baloot.IE_CA_3.Baloot.DiscountCoupon;

import com.baloot.IE_CA_3.Baloot.Exceptions.DiscountCouponNotExistsException;
import com.baloot.IE_CA_3.Baloot.Exceptions.WrongDiscountPercentageException;

import java.util.HashMap;
import java.util.Map;

public class DiscountCouponsManager {

    private final Map<String, DiscountCoupon> balootDiscountCoupons = new HashMap<>();


    public boolean discountCouponExists(String discountCode) {
        return balootDiscountCoupons.containsKey(discountCode);
    }

    public void addDiscountCoupon(DiscountCoupon discountCoupon) throws Exception {
        if(discountCoupon.getDiscount() < 0 || discountCoupon.getDiscount() > 100)
            throw new WrongDiscountPercentageException();
        balootDiscountCoupons.put(discountCoupon.getDiscountCode(), discountCoupon);
    }

    public DiscountCoupon getDiscountCouponByCode(String discountCode) throws Exception {
        if(discountCouponExists(discountCode)) {
            return balootDiscountCoupons.get(discountCode);
        }
        throw new DiscountCouponNotExistsException();
    }

    public Map<String, DiscountCoupon> getBalootDiscountCoupons() {
        return balootDiscountCoupons;
    }

}
