package cn.ollyice.framework.info;

/**
 * Created by ollyice on 2018/6/19.
 */

public class OrderInfo {
    public static final int ORDER_STATE_USER_CANCEL = 0;
    public static final int ORDER_STATE_WAIT_SERVER_NOTIFY = 1;//部分渠道sdk需要支付完成后查询  如果查询中途出问题  返回等待服务器通知状态
    public static final int ORDER_STATE_PAY_SUCCESS = 2;
    public static final int ORDER_STATE_PAY_FAILED = 3;

    private int orderState = ORDER_STATE_USER_CANCEL;//订单状态
    private String  orderId = "";//订单Id
    private String orderPrice = "";//订单价格 分
    private String errorMessage = "";//错误信息

    public int getOrderState() {
        return orderState;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public OrderInfo setOrderState(int orderState) {
        this.orderState = orderState;
        return this;
    }

    public OrderInfo setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public OrderInfo setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
        return this;
    }

    public OrderInfo setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }
}
