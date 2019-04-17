package top.starrysea.trade.service;

import top.starrysea.common.ServiceResult;
import top.starrysea.trade.PayelvesPayRequest;

public interface ITradeService {
	ServiceResult createPaymentRequestRouteService(PayelvesPayRequest payRequestBo);

}
