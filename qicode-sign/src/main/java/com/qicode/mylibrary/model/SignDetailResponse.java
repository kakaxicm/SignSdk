package com.qicode.mylibrary.model;

import java.util.List;

/**
 * Created by star on 15/10/2.
 */
public class SignDetailResponse extends BaseResponse {

    private ResultEntity result;

    public ResultEntity getResult() {
        return result;
    }

    public void setResult(ResultEntity result) {
        this.result = result;
    }

    public static class ResultEntity {
        private String sign_name;
        private String portrait_url;
        private String video_url;
        private String video_introduction;
        private int ios_price;
        private String deadline_time;
        private String designer_portrait;
        private String designer_introduce;
        private String designer_name;
        private int price;
        private int design_price;
        private String regular;
        private String regular_desc;
        private int deadline_time_stamp;
        private String designer_identity;
        private int preferential;
        private String details_design_img2;
        private String details_design_img3;
        private String details_design_img1;
        private String sign_img_url;
        private int video_price;
        private String image_url;
        private int delay_time;
        private int expert_sign_id;
        private int is_video;

        private List<DetailsDesignImageEntity> details_design_image;

        public String getSign_name() {
            return sign_name;
        }

        public void setSign_name(String sign_name) {
            this.sign_name = sign_name;
        }

        public String getPortrait_url() {
            return portrait_url;
        }

        public void setPortrait_url(String portrait_url) {
            this.portrait_url = portrait_url;
        }

        public String getVideo_url() {
            return video_url;
        }

        public void setVideo_url(String video_url) {
            this.video_url = video_url;
        }

        public String getVideo_introduction() {
            return video_introduction;
        }

        public void setVideo_introduction(String video_introduction) {
            this.video_introduction = video_introduction;
        }

        public int getIos_price() {
            return ios_price;
        }

        public void setIos_price(int ios_price) {
            this.ios_price = ios_price;
        }

        public String getDeadline_time() {
            return deadline_time;
        }

        public void setDeadline_time(String deadline_time) {
            this.deadline_time = deadline_time;
        }

        public String getDesigner_portrait() {
            return designer_portrait;
        }

        public void setDesigner_portrait(String designer_portrait) {
            this.designer_portrait = designer_portrait;
        }

        public String getDesigner_introduce() {
            return designer_introduce;
        }

        public void setDesigner_introduce(String designer_introduce) {
            this.designer_introduce = designer_introduce;
        }

        public String getDesigner_name() {
            return designer_name;
        }

        public void setDesigner_name(String designer_name) {
            this.designer_name = designer_name;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getDesign_price() {
            return design_price;
        }

        public void setDesign_price(int design_price) {
            this.design_price = design_price;
        }

        public String getRegular() {
            return regular;
        }

        public void setRegular(String regular) {
            this.regular = regular;
        }

        public String getRegular_desc() {
            return regular_desc;
        }

        public void setRegular_desc(String regular_desc) {
            this.regular_desc = regular_desc;
        }

        public int getDeadline_time_stamp() {
            return deadline_time_stamp;
        }

        public void setDeadline_time_stamp(int deadline_time_stamp) {
            this.deadline_time_stamp = deadline_time_stamp;
        }

        public String getDesigner_identity() {
            return designer_identity;
        }

        public void setDesigner_identity(String designer_identity) {
            this.designer_identity = designer_identity;
        }

        public int getPreferential() {
            return preferential;
        }

        public void setPreferential(int preferential) {
            this.preferential = preferential;
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

        public String getDetails_design_img1() {
            return details_design_img1;
        }

        public void setDetails_design_img1(String details_design_img1) {
            this.details_design_img1 = details_design_img1;
        }

        public String getSign_img_url() {
            return sign_img_url;
        }

        public void setSign_img_url(String sign_img_url) {
            this.sign_img_url = sign_img_url;
        }

        public int getVideo_price() {
            return video_price;
        }

        public void setVideo_price(int video_price) {
            this.video_price = video_price;
        }

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }

        public int getDelay_time() {
            return delay_time;
        }

        public void setDelay_time(int delay_time) {
            this.delay_time = delay_time;
        }

        public int getExpert_sign_id() {
            return expert_sign_id;
        }

        public void setExpert_sign_id(int expert_sign_id) {
            this.expert_sign_id = expert_sign_id;
        }

        public int getIs_video() {
            return is_video;
        }

        public void setIs_video(int is_video) {
            this.is_video = is_video;
        }

        public List<DetailsDesignImageEntity> getDetails_design_image() {
            return details_design_image;
        }

        public void setDetails_design_image(List<DetailsDesignImageEntity> details_design_image) {
            this.details_design_image = details_design_image;
        }

        public static class DetailsDesignImageEntity {
            private int id;
            private int expert_sign;
            private String image_url;
            private String url;
            private int width;
            private int height;
            private int sort;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getExpert_sign() {
                return expert_sign;
            }

            public void setExpert_sign(int expert_sign) {
                this.expert_sign = expert_sign;
            }

            public String getImage_url() {
                return image_url;
            }

            public void setImage_url(String image_url) {
                this.image_url = image_url;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }
        }
    }
}
