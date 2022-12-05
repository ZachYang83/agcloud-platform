package com.augurit.agcloud.agcom.agsupport.sc.monitor.thread;
import com.augurit.agcloud.agcom.agsupport.common.util.SendEmailUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @Author:
 * @Description:
 * @Date:created in :15:22 2019/3/13
 * @Modified By:
 */

@Service
public class AsyncTaskService {

    @Async
    public void sendEmailTask(String emailTitle, String ReceiveAddrr, String emailContent) {
        System.out.println("执行异步任务发送邮件到："+ReceiveAddrr);
        SendEmailUtil.sendEmail(emailTitle,ReceiveAddrr,emailContent);
    }
}
