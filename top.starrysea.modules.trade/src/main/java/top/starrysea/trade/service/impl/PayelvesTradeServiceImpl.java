package top.starrysea.trade.service.impl;

import static top.starrysea.common.ResultKey.STRING;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import top.starrysea.common.Common;
import top.starrysea.common.ServiceResult;
import top.starrysea.trade.PayelvesPayRequest;
import top.starrysea.trade.service.ITradeService;

@Service("payelvesTradeService")
public class PayelvesTradeServiceImpl implements ITradeService {
	@Value("${payelves.openId}")
	private String openId;
	@Value("${payelves.token}")
	private String token;
	@Value("${payelves.uuid}")
	private String uuid;
	@Value("${payelves.clientVersion}")
	private String clientVersion;
	@Value("${payelves.gateway}")
	private String gateway;
	@Value("${payelves.appKey}")
	private String appKey;

	@Override
	/***
	 * 组装支付url
	 */
	public ServiceResult createPaymentRequestRouteService(PayelvesPayRequest payRequestBo) {
		StringBuffer paramBuffer = new StringBuffer();
		try {
			// appKey
			paramBuffer.append("appKey=" + appKey + "&");
			// backParam
			String backParam = URLEncoder.encode(payRequestBo.getBackPara(), "UTF-8");
			paramBuffer.append("backPara=" + backParam + "&");
			// 商品名称
			paramBuffer.append("body=" + URLEncoder.encode(payRequestBo.getBody(), "UTF-8") + "&");
			// 通道
			paramBuffer.append("channel=" + payRequestBo.getChannel() + "&");
			// 版本号
			paramBuffer.append("clientVersion=" + clientVersion + "&");
			// 订单时间
			paramBuffer.append("dateTime=" + Common.getNowTime() + "&");
			// 开发者ID
			paramBuffer.append("openId=" + openId + "&");
			// 订单ID
			paramBuffer.append("orderId=" + payRequestBo.getOrderId() + "&");
			// 支付类型1：支付宝 2：微信
			paramBuffer.append("payType=" + payRequestBo.getPayType() + "&");
			// 价格
			paramBuffer.append("price=" + payRequestBo.getPrice().intValue() + "&");
			// 商品名称
			paramBuffer.append("subject=" + URLEncoder.encode(payRequestBo.getSubject(), "UTF-8") + "&");
			// 用户ID
			paramBuffer.append("userId=" + payRequestBo.getUserId() + "&");
			// uuid
			paramBuffer.append("uuid=" + uuid + "&");
			paramBuffer.append("sign=" + createSignForRequest(payRequestBo));
			// 网关
			paramBuffer.insert(0, gateway + "?");
		} catch (UnsupportedEncodingException e) {
			return ServiceResult.of(false);
		}
		return ServiceResult.of(true).setResult(STRING, paramBuffer.toString());
	}

	/***
	 * 签名
	 * 
	 * @param payRequestBo
	 * @return
	 */
	private String createSignForRequest(PayelvesPayRequest payRequestBo) {
		StringBuffer paramBuffer = new StringBuffer();
		// appKey
		paramBuffer.append("appKey=" + appKey + "&");
		// backParam
		paramBuffer.append("backPara=" + payRequestBo.getBackPara() + "&");
		// 商品名称
		paramBuffer.append("body=" + payRequestBo.getBody() + "&");
		// 通道
		paramBuffer.append("channel=" + payRequestBo.getChannel() + "&");
		// 版本号
		paramBuffer.append("clientVersion=" + clientVersion + "&");
		// 订单时间
		paramBuffer.append("dateTime=" + Common.getNowTime() + "&");
		// 开发者ID
		paramBuffer.append("openId=" + openId + "&");
		// 订单ID
		paramBuffer.append("orderId=" + payRequestBo.getOrderId() + "&");
		// 支付类型1：支付宝 2：微信
		paramBuffer.append("payType=" + payRequestBo.getPayType() + "&");
		// 价格
		paramBuffer.append("price=" + payRequestBo.getPrice().intValue() + "&");
		// 商品名称
		paramBuffer.append("subject=" + payRequestBo.getSubject() + "&");
		// 用户ID
		paramBuffer.append("userId=" + payRequestBo.getUserId() + "&");
		// uuid
		paramBuffer.append("uuid=" + uuid);
		paramBuffer.append(token);
		return Common.md5(paramBuffer.toString());
	}
}
