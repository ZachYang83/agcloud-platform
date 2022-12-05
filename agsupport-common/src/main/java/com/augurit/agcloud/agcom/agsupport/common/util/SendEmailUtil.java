package com.augurit.agcloud.agcom.agsupport.common.util;

import com.sun.mail.util.MailSSLSocketFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @Author:
 * @Description:
 * @Date:created in :14:01 2019/3/12
 * @Modified By:
 */
@Component
public class SendEmailUtil {
    private static String host;
    private static String userName;
    private static String password;
    private static String port;

    @Value("${mail.smtp.host}")
    private void setHost(String host){
        SendEmailUtil.host = host;
    }
    @Value("${mail.username}")
    private void setUserName(String userName){
        SendEmailUtil.userName = userName;
    }
    @Value("${mail.authorization.code}")
    private void setPassword(String password){
        SendEmailUtil.password = password;
    }
    @Value("${mail.smtp.port}")
    private void setPort(String port){
        SendEmailUtil.port = port;
    }


    public static void sendEmail(String emailTitle, String ReceiveAddrr, String emailContent){
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", host); // 设置邮件服务器
            MailSSLSocketFactory msf = new MailSSLSocketFactory();//SSL认证，腾讯邮箱是基于SSL加密的，所以需要开启才可以使用
            msf.setTrustAllHosts(true);
            props.put("mail.smtp.ssl.enable", "true");//设置是否使用ssl安全连接
            props.put("mail.smtp.ssl.socketFactory", msf);
            props.put("mail.smtp.port", port);
            props.put("mail.smtp.auth", "true");// 发送服务器需要身份验证

            // 构建授权信息，用于进行SMTP进行身份验证
            Authenticator authenticator = new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(userName, password);
                }
            };
            Session session = Session.getInstance(props, authenticator);
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(userName));//设置发件
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(ReceiveAddrr));//设置收件
            msg.setSubject(emailTitle);//设置邮件标题
            msg.setContent(emailContent, "text/html;charset=UTF-8");
            Transport transport = session.getTransport();
            Transport.send(msg);
            transport.close();
            System.out.println("邮件发送成功");
        }catch (Exception e){
            System.out.println("邮件发送失败"+e.getMessage());
        }
    }
}
