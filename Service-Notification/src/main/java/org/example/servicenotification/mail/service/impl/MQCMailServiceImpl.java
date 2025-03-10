/**
 * File Name: MQCMailServiceImpl.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-03-11
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicenotification.mail.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.example.servicenotification.mail.service.MQCMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MQCMailServiceImpl implements MQCMailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Autowired
    public MQCMailServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public boolean sendVerificationEmail(String email, String username, String purpose, String verificationCode) {
        try {

            // 创建邮件消息对象，使用 UTF-8 编码
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("slothteam@yeah.net");
            helper.setTo(email);
            helper.setSubject(verificationCode + " 是你的 SlothAsk 验证码");

            // 设置 Thymeleaf 上下文，传递需要在模板中使用的参数
            Context context = new Context();
            context.setVariable("username", username);
            context.setVariable("purpose", purpose);
            context.setVariable("verificationCode", verificationCode);

            // 加载模板
            String template = "mail/VerificationEmail.html";
            String htmlContent = templateEngine.process(template, context);

            // 设置邮件内容为 HTML 格式
            helper.setText(htmlContent, true);

            // 发送邮件
            mailSender.send(message);

            return true;
        } catch (MessagingException e) {
            // 此处可记录日志或进行其他错误处理
            e.printStackTrace();
            return false;
        }
    }
}
