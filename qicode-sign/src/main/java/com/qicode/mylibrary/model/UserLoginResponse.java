package com.qicode.mylibrary.model;

/**
 * Created by kakaxicm on 2015/9/4.
 */
public class UserLoginResponse extends BaseResponse {

    /**
     * result : {"user_id":3,"user_name":"","user_phone":"15901022568"}
     * status : {"code":"0","description":"success"}
     */
    private ResultEntity result;

    public void setResult(ResultEntity result) {
        this.result = result;
    }

    public ResultEntity getResult() {
        return result;
    }

    public static class ResultEntity {
        /**
         * user_id : 3
         * user_name :
         * user_phone : 15901022568
         */
        private int user_id;
        private String user_name;
        private String user_phone;

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public void setUser_phone(String user_phone) {
            this.user_phone = user_phone;
        }

        public int getUser_id() {
            return user_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public String getUser_phone() {
            return user_phone;
        }
    }
}
