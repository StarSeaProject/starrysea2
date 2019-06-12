package top.starrysea.object.view.out;

public class OrderForAll {
	private String orderId;
	private String orderNum;
	private String orderName;
	private String orderStatus;
	private long orderTime;
	private int orderMoney;

	public OrderForAll(String orderId, String orderNum, String orderName, String orderStatus, long orderTime,
			int orderMoney) {
		super();
		this.orderId = orderId;
		this.orderNum = orderNum;
		this.orderName = orderName;
		this.orderStatus = orderStatus;
		this.orderTime = orderTime;
		this.orderMoney = orderMoney;
	}

	public int getOrderMoney() {
		return orderMoney;
	}

	public String getOrderId() {
		return orderId;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public String getOrderName() {
		return orderName;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public long getOrderTime() {
		return orderTime;
	}

}
