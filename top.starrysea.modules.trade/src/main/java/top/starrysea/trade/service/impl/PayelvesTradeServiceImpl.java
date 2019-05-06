package top.starrysea.trade.service.impl;

import static top.starrysea.common.ResultKey.STRING;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import top.starrysea.common.Common;
import top.starrysea.common.ServiceResult;
import top.starrysea.trade.PayelvesPayNotify;
import top.starrysea.trade.PayelvesPayRequest;
import top.starrysea.trade.service.ITradeService;
import static top.starrysea.common.Const.CHARSET;

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
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	/***
	 * 组装支付url
	 */
	public ServiceResult createPaymentRequestRouteService(PayelvesPayRequest payRequestBo) {
		StringBuilder paramBuilder = new StringBuilder();
		try {
			// appKey
			paramBuilder.append("appKey=" + URLEncoder.encode(appKey, CHARSET) + "&");
			// backParam
			String backParam = URLEncoder.encode(payRequestBo.getBackPara(), CHARSET);
			paramBuilder.append("backPara=" + backParam + "&");
			// 商品名称
			paramBuilder.append("body=" + URLEncoder.encode(payRequestBo.getBody(), CHARSET) + "&");
			// 通道
			paramBuilder.append("channel=" + URLEncoder.encode(payRequestBo.getChannel().toString(), CHARSET) + "&");
			// 版本号
			paramBuilder.append("clientVersion=" + URLEncoder.encode(clientVersion, CHARSET) + "&");
			// 订单时间
			payRequestBo.setDateTime(Common.getNowTime());
			paramBuilder.append("dateTime=" + URLEncoder.encode(payRequestBo.getDateTime(), CHARSET) + "&");
			// 开发者ID
			paramBuilder.append("openId=" + URLEncoder.encode(openId, CHARSET) + "&");
			// 订单ID
			paramBuilder.append("orderId=" + payRequestBo.getOrderId() + "&");
			// 支付类型1：支付宝 2：微信
			paramBuilder.append("payType=" + payRequestBo.getPayType() + "&");
			// 价格
			paramBuilder.append(
					"price=" + URLEncoder.encode(String.valueOf(payRequestBo.getPrice().intValue()), CHARSET) + "&");
			// 商品名称
			paramBuilder.append("subject=" + URLEncoder.encode(payRequestBo.getSubject(), CHARSET) + "&");
			// 用户ID
			paramBuilder.append("userId=" + URLEncoder.encode(payRequestBo.getUserId(), CHARSET) + "&");
			// uuid
			paramBuilder.append("uuid=" + URLEncoder.encode(uuid, CHARSET) + "&");
			paramBuilder.append("sign=" + URLEncoder.encode(createSignForRequest(payRequestBo), CHARSET));
			// 网关
			paramBuilder.insert(0, gateway + "?");
			logger.info("生成的链接为:" + paramBuilder.toString());
		} catch (UnsupportedEncodingException e) {
			return ServiceResult.of(false);
		}
		return ServiceResult.of(true).setResult(STRING, paramBuilder.toString());
	}

	/***
	 * 签名
	 * 
	 * @param payRequestBo
	 * @return
	 */
	private String createSignForRequest(PayelvesPayRequest payRequestBo) {
		StringBuilder paramBuilder = new StringBuilder();
		// appKey
		paramBuilder.append("appKey=" + appKey + "&");
		// backParam
		paramBuilder.append("backPara=" + payRequestBo.getBackPara() + "&");
		// 商品名称
		paramBuilder.append("body=" + payRequestBo.getBody() + "&");
		// 通道
		paramBuilder.append("channel=" + payRequestBo.getChannel() + "&");
		// 版本号
		paramBuilder.append("clientVersion=" + clientVersion + "&");
		// 订单时间
		paramBuilder.append("dateTime=" + payRequestBo.getDateTime() + "&");
		// 开发者ID
		paramBuilder.append("openId=" + openId + "&");
		// 订单ID
		paramBuilder.append("orderId=" + payRequestBo.getOrderId() + "&");
		// 支付类型1：支付宝 2：微信
		paramBuilder.append("payType=" + payRequestBo.getPayType() + "&");
		// 价格
		paramBuilder.append("price=" + payRequestBo.getPrice().intValue() + "&");
		// 商品名称
		paramBuilder.append("subject=" + payRequestBo.getSubject() + "&");
		// 用户ID
		paramBuilder.append("userId=" + payRequestBo.getUserId() + "&");
		// uuid
		paramBuilder.append("uuid=" + uuid);
		paramBuilder.append(token);
		logger.info("需要签名的参数为：" + paramBuilder.toString());
		String sign = Common.md5(paramBuilder.toString());
		logger.info("签名为：" + sign);
		return sign;
	}

	@Override
	public ServiceResult validateNotifyParamService(PayelvesPayNotify payNotify) {
		StringBuilder paramBuilder = new StringBuilder();
		paramBuilder.append("amount=" + payNotify.getAmount() + "&");
		paramBuilder.append("appKey=" + payNotify.getAppKey() + "&");
		paramBuilder.append("backPara=" + payNotify.getBackPara() + "&");
		paramBuilder.append("dateTime=" + payNotify.getDateTime() + "&");
		paramBuilder.append("openId=" + payNotify.getOpenId() + "&");
		paramBuilder.append("orderId=" + payNotify.getOrderId() + "&");
		paramBuilder.append("outTradeNo=" + payNotify.getOutTradeNo() + "&");
		paramBuilder.append("payType=" + payNotify.getPayType() + "&");
		paramBuilder.append("payUserId=" + payNotify.getPayUserId() + "&");
		paramBuilder.append("status=" + payNotify.getStatus() + "&");
		paramBuilder.append("version=" + payNotify.getVersion() + token);
		logger.info("正在对回调信息" + Common.toJson(payNotify) + "验证签名");
		logger.info("签名原文为：" + paramBuilder);
		logger.info("得到签名为：" + Common.md5(paramBuilder.toString()) + ",与原签名" + payNotify.getSign() + "进行对比");
		return payNotify.getSign().equals(Common.md5(paramBuilder.toString())) ? ServiceResult.SUCCESS_SERVICE_RESULT
				: ServiceResult.FAIL_SERVICE_RESULT;
	}

}
