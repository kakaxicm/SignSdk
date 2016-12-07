package com.qicode.mylibrary.model;

import java.util.List;

/**
 * Created by star on 15/10/24.
 */
public class ChargeResponse extends BaseResponse {

    private ResultEntity result;

    public void setResult(ResultEntity result) {
        this.result = result;
    }

    public ResultEntity getResult() {
        return result;
    }

    public static class ResultEntity {

        private ChargeEntity charge;

        public void setCharge(ChargeEntity charge) {
            this.charge = charge;
        }

        public ChargeEntity getCharge() {
            return charge;
        }

        public static class ChargeEntity {
            private String order_no;
            private ExtraEntity extra;
            private String app;
            private boolean livemode;
            private String currency;
            private Object time_settle;
            private int time_expire;
            private String id;
            private String subject;
            private Object failure_msg;
            private String channel;
            private MetadataEntity metadata;
            private String body;

            private CredentialEntity credential;
            private String client_ip;
            private Object description;
            private int amount_refunded;
            private boolean refunded;
            private String object;
            private boolean paid;
            private int amount_settle;
            private Object time_paid;
            private Object failure_code;

            private RefundsEntity refunds;
            private int created;
            private Object transaction_no;
            private int amount;

            public void setOrder_no(String order_no) {
                this.order_no = order_no;
            }

            public void setExtra(ExtraEntity extra) {
                this.extra = extra;
            }

            public void setApp(String app) {
                this.app = app;
            }

            public void setLivemode(boolean livemode) {
                this.livemode = livemode;
            }

            public void setCurrency(String currency) {
                this.currency = currency;
            }

            public void setTime_settle(Object time_settle) {
                this.time_settle = time_settle;
            }

            public void setTime_expire(int time_expire) {
                this.time_expire = time_expire;
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setSubject(String subject) {
                this.subject = subject;
            }

            public void setFailure_msg(Object failure_msg) {
                this.failure_msg = failure_msg;
            }

            public void setChannel(String channel) {
                this.channel = channel;
            }

            public void setMetadata(MetadataEntity metadata) {
                this.metadata = metadata;
            }

            public void setBody(String body) {
                this.body = body;
            }

            public void setCredential(CredentialEntity credential) {
                this.credential = credential;
            }

            public void setClient_ip(String client_ip) {
                this.client_ip = client_ip;
            }

            public void setDescription(Object description) {
                this.description = description;
            }

            public void setAmount_refunded(int amount_refunded) {
                this.amount_refunded = amount_refunded;
            }

            public void setRefunded(boolean refunded) {
                this.refunded = refunded;
            }

            public void setObject(String object) {
                this.object = object;
            }

            public void setPaid(boolean paid) {
                this.paid = paid;
            }

            public void setAmount_settle(int amount_settle) {
                this.amount_settle = amount_settle;
            }

            public void setTime_paid(Object time_paid) {
                this.time_paid = time_paid;
            }

            public void setFailure_code(Object failure_code) {
                this.failure_code = failure_code;
            }

            public void setRefunds(RefundsEntity refunds) {
                this.refunds = refunds;
            }

            public void setCreated(int created) {
                this.created = created;
            }

            public void setTransaction_no(Object transaction_no) {
                this.transaction_no = transaction_no;
            }

            public void setAmount(int amount) {
                this.amount = amount;
            }

            public String getOrder_no() {
                return order_no;
            }

            public ExtraEntity getExtra() {
                return extra;
            }

            public String getApp() {
                return app;
            }

            public boolean isLivemode() {
                return livemode;
            }

            public String getCurrency() {
                return currency;
            }

            public Object getTime_settle() {
                return time_settle;
            }

            public int getTime_expire() {
                return time_expire;
            }

            public String getId() {
                return id;
            }

            public String getSubject() {
                return subject;
            }

            public Object getFailure_msg() {
                return failure_msg;
            }

            public String getChannel() {
                return channel;
            }

            public MetadataEntity getMetadata() {
                return metadata;
            }

            public String getBody() {
                return body;
            }

            public CredentialEntity getCredential() {
                return credential;
            }

            public String getClient_ip() {
                return client_ip;
            }

            public Object getDescription() {
                return description;
            }

            public int getAmount_refunded() {
                return amount_refunded;
            }

            public boolean isRefunded() {
                return refunded;
            }

            public String getObject() {
                return object;
            }

            public boolean isPaid() {
                return paid;
            }

            public int getAmount_settle() {
                return amount_settle;
            }

            public Object getTime_paid() {
                return time_paid;
            }

            public Object getFailure_code() {
                return failure_code;
            }

            public RefundsEntity getRefunds() {
                return refunds;
            }

            public int getCreated() {
                return created;
            }

            public Object getTransaction_no() {
                return transaction_no;
            }

            public int getAmount() {
                return amount;
            }

            public static class ExtraEntity {
            }

            public static class MetadataEntity {
            }

            public static class CredentialEntity {
                private String object;

                private AlipayEntity alipay;

                private BfbEntity bfb;

                private WxEntity wx;

                public void setObject(String object) {
                    this.object = object;
                }

                public void setAlipay(AlipayEntity alipay) {
                    this.alipay = alipay;
                }

                public void setBfb(BfbEntity bfb) {
                    this.bfb = bfb;
                }

                public void setWx(WxEntity wx) {
                    this.wx = wx;
                }

                public String getObject() {
                    return object;
                }

                public AlipayEntity getAlipay() {
                    return alipay;
                }

                public BfbEntity getBfb() {
                    return bfb;
                }

                public WxEntity getWx() {
                    return wx;
                }

                public static class AlipayEntity {
                    private String orderInfo;

                    public void setOrderInfo(String orderInfo) {
                        this.orderInfo = orderInfo;
                    }

                    public String getOrderInfo() {
                        return orderInfo;
                    }
                }

                public static class BfbEntity {
                    private String pay_type;
                    private String goods_name;
                    private String order_create_time;
                    private int total_amount;
                    private String order_no;
                    private String extra;
                    private String input_charset;
                    private String sp_no;
                    private String service_code;
                    private String goods_desc;
                    private String sign;
                    private String currency;
                    private String version;
                    private String sign_method;
                    private String expire_time;
                    private String return_url;

                    public void setPay_type(String pay_type) {
                        this.pay_type = pay_type;
                    }

                    public void setGoods_name(String goods_name) {
                        this.goods_name = goods_name;
                    }

                    public void setOrder_create_time(String order_create_time) {
                        this.order_create_time = order_create_time;
                    }

                    public void setTotal_amount(int total_amount) {
                        this.total_amount = total_amount;
                    }

                    public void setOrder_no(String order_no) {
                        this.order_no = order_no;
                    }

                    public void setExtra(String extra) {
                        this.extra = extra;
                    }

                    public void setInput_charset(String input_charset) {
                        this.input_charset = input_charset;
                    }

                    public void setSp_no(String sp_no) {
                        this.sp_no = sp_no;
                    }

                    public void setService_code(String service_code) {
                        this.service_code = service_code;
                    }

                    public void setGoods_desc(String goods_desc) {
                        this.goods_desc = goods_desc;
                    }

                    public void setSign(String sign) {
                        this.sign = sign;
                    }

                    public void setCurrency(String currency) {
                        this.currency = currency;
                    }

                    public void setVersion(String version) {
                        this.version = version;
                    }

                    public void setSign_method(String sign_method) {
                        this.sign_method = sign_method;
                    }

                    public void setExpire_time(String expire_time) {
                        this.expire_time = expire_time;
                    }

                    public void setReturn_url(String return_url) {
                        this.return_url = return_url;
                    }

                    public String getPay_type() {
                        return pay_type;
                    }

                    public String getGoods_name() {
                        return goods_name;
                    }

                    public String getOrder_create_time() {
                        return order_create_time;
                    }

                    public int getTotal_amount() {
                        return total_amount;
                    }

                    public String getOrder_no() {
                        return order_no;
                    }

                    public String getExtra() {
                        return extra;
                    }

                    public String getInput_charset() {
                        return input_charset;
                    }

                    public String getSp_no() {
                        return sp_no;
                    }

                    public String getService_code() {
                        return service_code;
                    }

                    public String getGoods_desc() {
                        return goods_desc;
                    }

                    public String getSign() {
                        return sign;
                    }

                    public String getCurrency() {
                        return currency;
                    }

                    public String getVersion() {
                        return version;
                    }

                    public String getSign_method() {
                        return sign_method;
                    }

                    public String getExpire_time() {
                        return expire_time;
                    }

                    public String getReturn_url() {
                        return return_url;
                    }
                }

                public static class WxEntity {
                    private String packageValue;
                    private int timeStamp;
                    private String sign;
                    private String partnerId;
                    private String appId;
                    private String prepayId;
                    private String nonceStr;

                    public void setPackageValue(String packageValue) {
                        this.packageValue = packageValue;
                    }

                    public void setTimeStamp(int timeStamp) {
                        this.timeStamp = timeStamp;
                    }

                    public void setSign(String sign) {
                        this.sign = sign;
                    }

                    public void setPartnerId(String partnerId) {
                        this.partnerId = partnerId;
                    }

                    public void setAppId(String appId) {
                        this.appId = appId;
                    }

                    public void setPrepayId(String prepayId) {
                        this.prepayId = prepayId;
                    }

                    public void setNonceStr(String nonceStr) {
                        this.nonceStr = nonceStr;
                    }

                    public String getPackageValue() {
                        return packageValue;
                    }

                    public int getTimeStamp() {
                        return timeStamp;
                    }

                    public String getSign() {
                        return sign;
                    }

                    public String getPartnerId() {
                        return partnerId;
                    }

                    public String getAppId() {
                        return appId;
                    }

                    public String getPrepayId() {
                        return prepayId;
                    }

                    public String getNonceStr() {
                        return nonceStr;
                    }
                }
            }

            public static class RefundsEntity {
                private String url;
                private boolean has_more;
                private String object;
                private List<?> data;

                public void setUrl(String url) {
                    this.url = url;
                }

                public void setHas_more(boolean has_more) {
                    this.has_more = has_more;
                }

                public void setObject(String object) {
                    this.object = object;
                }

                public void setData(List<?> data) {
                    this.data = data;
                }

                public String getUrl() {
                    return url;
                }

                public boolean isHas_more() {
                    return has_more;
                }

                public String getObject() {
                    return object;
                }

                public List<?> getData() {
                    return data;
                }
            }
        }
    }
}
