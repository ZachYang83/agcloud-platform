package com.augurit.agcloud.agcom.agsupport.util;

/**
 * Created by Administrator on 2017/12/19.
 */
public class SMSContentTemplate {
    //注册申请
    public static String smsRegApplyOkTemplate = "尊敬的%s用户，您的“广州水务一体化平台”用户申请已审批通过，登录名为%s,请及时登录平台确认。";
    public static String smsRegApplyFailTemplate = "尊敬的%s用户，您的“广州水务一体化平台”用户申请审批不通过，不通过的理由：%s。";
    //单点登录申请 授权
    public static String smsSSOAuthorizeApplyOkTemplate = "尊敬的%s用户，您的“广州水务一体化平台”登录其他系统申请已审批通过，请及时登录平台确认。";
    public static String smsSSOAuthorizeApplyFailTemplate = "尊敬的%s用户，您的“广州水务一体化平台”登录其他系统申请申请审批不通过，不通过的理由：%s。";
    //验证码
   // public static String smsVerifyCodeTemplate = "尊敬的%s用户，广州水务一体化平台验证码是：%s";
    public static String smsVerifyCodeTemplate = "尊敬的%s用户，您正在重置您的“广州水务一体化平台”账号密码，验证码是:%s";
    // 注册申请用户
    public static String smsRegVerifyCodeTemplate = "验证码是:%s，请在“广州水务一体化平台”注册页面中输入以完成注册。";

    // 重置密码里 获取验证码信息
    public static String smsVerifyCodeInResetPasswordTemplate = "验证码是:%s，请在“广州水务一体化平台”重置密码页面中输入验证码。";

    // 登录页面 获取验证码信息
    public static String smsVerifyCodeInLoginPageTemplate = "验证码是:%s，请在“广州水务一体化平台”登录页面中输入验证码。";
}
