package com.qicode.mylibrary.model;

import java.util.List;

/**
 * Created by star on 15/10/1.
 */
public class SignListResponse extends BaseResponse {

    private ResultEntity result;

    public ResultEntity getResult() {
        return result;
    }

    public void setResult(ResultEntity result) {
        this.result = result;
    }

    public static class ResultEntity {
        private int total;

        private List<ExpertSignsEntity> expert_signs;

        private List<UserSignsEntity> user_signs;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<ExpertSignsEntity> getExpert_signs() {
            return expert_signs;
        }

        public void setExpert_signs(List<ExpertSignsEntity> expert_signs) {
            this.expert_signs = expert_signs;
        }

        public List<UserSignsEntity> getUser_signs() {
            return user_signs;
        }

        public void setUser_signs(List<UserSignsEntity> user_signs) {
            this.user_signs = user_signs;
        }

        public static class ExpertSignsEntity {
            private int expert_sign_id;
            private String sign_img_url;
            private String image_url;
            private String sign_name;
            private int price;
            private int is_design;
            private int is_video;
            private int delay_time;
            private int state;
            private String video_introduction;
            private String video_url;
            private String details_design_img1;
            private String details_design_img2;
            private String details_design_img3;
            private int sign_designer;
            private int preferential;
            private int design_price;
            private int video_price;
            private String ios_product_pay_id;
            private int ios_price;
            private String regular;
            private String sign_designer__designer_name;

            public int getExpert_sign_id() {
                return expert_sign_id;
            }

            public void setExpert_sign_id(int expert_sign_id) {
                this.expert_sign_id = expert_sign_id;
            }

            public String getSign_img_url() {
                return sign_img_url;
            }

            public void setSign_img_url(String sign_img_url) {
                this.sign_img_url = sign_img_url;
            }

            public String getImage_url() {
                return image_url;
            }

            public void setImage_url(String image_url) {
                this.image_url = image_url;
            }

            public String getSign_name() {
                return sign_name;
            }

            public void setSign_name(String sign_name) {
                this.sign_name = sign_name;
            }

            public int getPrice() {
                return price;
            }

            public void setPrice(int price) {
                this.price = price;
            }

            public int getIs_design() {
                return is_design;
            }

            public void setIs_design(int is_design) {
                this.is_design = is_design;
            }

            public int getIs_video() {
                return is_video;
            }

            public void setIs_video(int is_video) {
                this.is_video = is_video;
            }

            public int getDelay_time() {
                return delay_time;
            }

            public void setDelay_time(int delay_time) {
                this.delay_time = delay_time;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }

            public String getVideo_introduction() {
                return video_introduction;
            }

            public void setVideo_introduction(String video_introduction) {
                this.video_introduction = video_introduction;
            }

            public String getVideo_url() {
                return video_url;
            }

            public void setVideo_url(String video_url) {
                this.video_url = video_url;
            }

            public String getDetails_design_img1() {
                return details_design_img1;
            }

            public void setDetails_design_img1(String details_design_img1) {
                this.details_design_img1 = details_design_img1;
            }

            public String getDetails_design_img2() {
                return details_design_img2;
            }

            public void setDetails_design_img2(String details_design_img2) {
                this.details_design_img2 = details_design_img2;
            }

            public String getDetails_design_img3() {
                return details_design_img3;
            }

            public void setDetails_design_img3(String details_design_img3) {
                this.details_design_img3 = details_design_img3;
            }

            public int getSign_designer() {
                return sign_designer;
            }

            public void setSign_designer(int sign_designer) {
                this.sign_designer = sign_designer;
            }

            public int getPreferential() {
                return preferential;
            }

            public void setPreferential(int preferential) {
                this.preferential = preferential;
            }

            public int getDesign_price() {
                return design_price;
            }

            public void setDesign_price(int design_price) {
                this.design_price = design_price;
            }

            public int getVideo_price() {
                return video_price;
            }

            public void setVideo_price(int video_price) {
                this.video_price = video_price;
            }

            public String getIos_product_pay_id() {
                return ios_product_pay_id;
            }

            public void setIos_product_pay_id(String ios_product_pay_id) {
                this.ios_product_pay_id = ios_product_pay_id;
            }

            public int getIos_price() {
                return ios_price;
            }

            public void setIos_price(int ios_price) {
                this.ios_price = ios_price;
            }

            public String getRegular() {
                return regular;
            }

            public void setRegular(String regular) {
                this.regular = regular;
            }

            public String getSign_designer__designer_name() {
                return sign_designer__designer_name;
            }

            public void setSign_designer__designer_name(String sign_designer__designer_name) {
                this.sign_designer__designer_name = sign_designer__designer_name;
            }
        }

        public static class UserSignsEntity {
            private int user_sign_id;
            private int user;
            private int expert_sign;
            private String sign_user_name;
            private String addition_content;
            private int state;
            private String sign_image;
            private String sign_video;
            private String image_url;
            private String video_url;
            private int sign_status;
            private String create_time;
            private int create_time_stamp;
            private Object deadline_time;
            private int deadline_time_stamp;
            private String update_time;
            private int addition_video_status;
            private String addition_video_content;
            private String sign_source;
            private int sign_type;
            private int is_addition_video;
            private String expert_sign__sign_name;
            private String comment;

            public String getComment() {
                return comment;
            }

            public void setComment(String comment) {
                this.comment = comment;
            }
            public int getUser_sign_id() {
                return user_sign_id;
            }

            public void setUser_sign_id(int user_sign_id) {
                this.user_sign_id = user_sign_id;
            }

            public int getUser() {
                return user;
            }

            public void setUser(int user) {
                this.user = user;
            }

            public int getExpert_sign() {
                return expert_sign;
            }

            public void setExpert_sign(int expert_sign) {
                this.expert_sign = expert_sign;
            }

            public String getSign_user_name() {
                return sign_user_name;
            }

            public void setSign_user_name(String sign_user_name) {
                this.sign_user_name = sign_user_name;
            }

            public String getAddition_content() {
                return addition_content;
            }

            public void setAddition_content(String addition_content) {
                this.addition_content = addition_content;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }

            public String getSign_image() {
                return sign_image;
            }

            public void setSign_image(String sign_image) {
                this.sign_image = sign_image;
            }

            public String getSign_video() {
                return sign_video;
            }

            public void setSign_video(String sign_video) {
                this.sign_video = sign_video;
            }

            public String getImage_url() {
                return image_url;
            }

            public void setImage_url(String image_url) {
                this.image_url = image_url;
            }

            public String getVideo_url() {
                return video_url;
            }

            public void setVideo_url(String video_url) {
                this.video_url = video_url;
            }

            public int getSign_status() {
                return sign_status;
            }

            public void setSign_status(int sign_status) {
                this.sign_status = sign_status;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public int getCreate_time_stamp() {
                return create_time_stamp;
            }

            public void setCreate_time_stamp(int create_time_stamp) {
                this.create_time_stamp = create_time_stamp;
            }

            public Object getDeadline_time() {
                return deadline_time;
            }

            public void setDeadline_time(Object deadline_time) {
                this.deadline_time = deadline_time;
            }

            public int getDeadline_time_stamp() {
                return deadline_time_stamp;
            }

            public void setDeadline_time_stamp(int deadline_time_stamp) {
                this.deadline_time_stamp = deadline_time_stamp;
            }

            public String getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(String update_time) {
                this.update_time = update_time;
            }

            public int getAddition_video_status() {
                return addition_video_status;
            }

            public void setAddition_video_status(int addition_video_status) {
                this.addition_video_status = addition_video_status;
            }

            public String getAddition_video_content() {
                return addition_video_content;
            }

            public void setAddition_video_content(String addition_video_content) {
                this.addition_video_content = addition_video_content;
            }

            public String getSign_source() {
                return sign_source;
            }

            public void setSign_source(String sign_source) {
                this.sign_source = sign_source;
            }

            public int getSign_type() {
                return sign_type;
            }

            public void setSign_type(int sign_type) {
                this.sign_type = sign_type;
            }

            public int getIs_addition_video() {
                return is_addition_video;
            }

            public void setIs_addition_video(int is_addition_video) {
                this.is_addition_video = is_addition_video;
            }

            public String getExpert_sign__sign_name() {
                return expert_sign__sign_name;
            }

            public void setExpert_sign__sign_name(String expert_sign__sign_name) {
                this.expert_sign__sign_name = expert_sign__sign_name;
            }
        }
    }
}
