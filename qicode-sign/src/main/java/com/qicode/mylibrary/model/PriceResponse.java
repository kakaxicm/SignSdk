package com.qicode.mylibrary.model;

/**
 * Created by chenming on 16/7/8.
 */
public class PriceResponse extends BaseResponse{

    /**
     * price : 3800.0
     */

    private ResultBean result;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        private double price;

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }
    }
}
