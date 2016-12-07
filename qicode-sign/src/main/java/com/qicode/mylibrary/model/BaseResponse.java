package com.qicode.mylibrary.model;

/**
 * Created by star on 15/10/14.
 */
public class BaseResponse {
    private StatusEntity status;

    public StatusEntity getStatus() {
        return status;
    }

    public void setStatus(StatusEntity status) {
        this.status = status;
    }

    public static class StatusEntity {
        private String code;
        private String description;
        private Object extra;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Object getExtra() {
            return extra;
        }

        public void setExtra(Object extra) {
            this.extra = extra;
        }
    }
}
