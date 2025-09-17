/**
 * CreateTime: 2024-08-29
 * Description: 邮箱配置工厂，通过外部化配置加载敏感参数
 * Version: 1.1
 * Author: holic512
 *
 * 优化说明：
 * 1. 邮箱服务器参数（主机、端口、用户名、密码）通过外部配置文件注入，避免硬编码敏感信息；
 * 2. 使用 @Value 注解从配置文件读取配置，实现分离配置文件；
 * 3. 在生产环境中，配置文件应被单独管理，并通过 CI/CD、环境变量或配置中心注入到应用中。
 */
package org.example.servicenotification.mail.sender;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
// @PropertySource(value = "classpath:mail-config.yml", factory = YamlPropertySourceFactory.class) // 加载 mail-config.yml
public class MailSenderFactory {

    // 从外部配置文件中加载邮件服务器主机名（如：smtp.example.com）
    @Value("${mail.server.host}")
    private String host;

    // 从外部配置文件中加载邮件服务器端口（如：587）
    @Value("${mail.server.port}")
    private int port;

    // 从外部配置文件中加载邮箱用户名
    @Value("${mail.server.username}")
    private String username;

    // 从外部配置文件中加载邮箱授权码或密码，建议使用安全存储方式管理
    @Value("${mail.server.password}")
    private String password;

    // 从外部配置文件中加载是否需要 SMTP 认证（true/false）
    @Value("${mail.smtp.auth:true}")
    private String smtpAuth;

    // 从外部配置文件中加载是否启用 SSL（true/false）
    @Value("${mail.smtp.ssl.enable:true}")
    private String sslEnable;

    // 从外部配置文件中加载是否启用 STARTTLS（true/false）
    @Value("${mail.smtp.starttls.enable:false}")
    private String starttlsEnable;

    /**
     * 用于启动前初始化 JavaMailSender 对象
     * 通过外部化配置加载敏感信息，确保代码中不包含敏感参数
     *
     * @return 配置好的 JavaMailSender 实例
     */
    @Bean
    public JavaMailSender createMailClient() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        // 设置邮件服务器主机名，外部配置注入
        mailSender.setHost(host);
        // 设置邮件服务器端口，外部配置注入
        mailSender.setPort(port);
        // 设置邮箱用户名，外部配置注入
        mailSender.setUsername(username);
        // 设置邮箱授权码或密码，外部配置注入
        mailSender.setPassword(password);
        // 设置默认编码格式
        mailSender.setDefaultEncoding("UTF-8");

        // 配置邮件发送相关属性
        Properties props = mailSender.getJavaMailProperties();
        // 设置使用的邮件传输协议
        props.put("mail.transport.protocol", "smtp");
        // 设置是否需要认证，外部配置注入
        props.put("mail.smtp.auth", smtpAuth);
        // 设置是否启用 SSL，外部配置注入
        props.put("mail.smtp.ssl.enable", sslEnable);
        // 设置是否启用 STARTTLS，外部配置注入
        props.put("mail.smtp.starttls.enable", starttlsEnable);

        return mailSender;
    }
}
