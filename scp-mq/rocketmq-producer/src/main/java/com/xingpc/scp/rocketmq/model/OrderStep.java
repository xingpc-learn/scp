package com.xingpc.scp.rocketmq.model;

/**
 * @Author: XingPc
 * @Description: 订单步骤
 * @Date: 2020/2/19 11:37
 * @Version: 1.0
 */
public class OrderStep {

    private long orderId;
    private String desc;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "OrderStep{" +
                "orderId=" + orderId +
                ", desc='" + desc + '\'' +
                '}';
    }

}
