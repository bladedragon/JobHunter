package team.legend.jobhunter.utils;

import java.math.BigDecimal;

public class PriceUtil {

    public static String getPrice(String origin_price, String discount) {
        int price_int = Integer.parseInt(origin_price);
        int discount_int = Integer.parseInt(discount);
        String result = String.valueOf(price_int - discount_int);
        return result;
    }

    //将价格从分换成元
    public static String changePriceNum(int price){
        BigDecimal amount = new BigDecimal(price);
        amount = amount.divide(new BigDecimal(100));
        return amount.toString();
    }
}
