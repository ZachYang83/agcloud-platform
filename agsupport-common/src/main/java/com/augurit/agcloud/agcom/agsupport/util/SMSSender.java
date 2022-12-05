package com.augurit.agcloud.agcom.agsupport.util;

import com.qcloud.sms.SmsMultiSender;
import com.qcloud.sms.SmsMultiSenderResult;
import com.qcloud.sms.SmsSingleSender;
import com.qcloud.sms.SmsSingleSenderResult;
import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;

public class SMSSender {
	private static final Logger log = Logger.getLogger(SMSSender.class);

	private static final int APPID = 1400055485;
	private static final String APPKEY = "de9d72bb34707a47484a7f35b20281da";

	private static final SmsMultiSender multiSender = initSmsMultiSender();
	private static final SmsSingleSender singleSender = initSmsSingleSender();

	private static final SmsMultiSender initSmsMultiSender() {
		SmsMultiSender sender = null;
		try {
			sender = new SmsMultiSender(APPID, APPKEY);
		} catch (Exception e) {
			log.error("初始化短信群发发送器失败...", e);
		}

		return sender;
	}

	private static final SmsSingleSender initSmsSingleSender() {
		SmsSingleSender sender = null;
		try {
			sender = new SmsSingleSender(APPID, APPKEY);
		} catch (Exception e) {
			log.error("初始化短信发送器失败...", e);
		}

		return sender;
	}

	public static synchronized SmsMultiSenderResult sendSms(ArrayList<String> phones, String smsContent) {
		if(CollectionUtils.isEmpty(phones) || StringUtils.isEmpty(smsContent)) {
			return null;
		}

		SmsMultiSenderResult result = null;
		try {
			result = multiSender.send(0, "86", phones, smsContent, "", "");
		} catch(Exception e) {
			log.error("群发短信出现异常...", e);
		}

		return result;
	}

	public static synchronized SmsSingleSenderResult sendSms(String phone, String smsContent) {
		if(StringUtils.isEmpty(phone) || StringUtils.isEmpty(smsContent)) {
			return null;
		}

		SmsSingleSenderResult result = null;
		try {
			result = singleSender.send(0, "86", phone, smsContent, "", "");
		} catch(Exception e) {
			log.error("群发短信出现异常...", e);
		}

		return result;
	}
}
