package com.star.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component

public class MailUtil {
    @Autowired
    JavaMailSender javaMailSender;

    @Async
    public void sendMail(String msg,String mailAds) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
        mimeMessageHelper.setSubject("RAINBOWSEA");
        mimeMessageHelper.setFrom("2860698744@qq.com");
        if (mailAds != null) {
            mimeMessageHelper.setTo(mailAds);
        } else {
            mimeMessageHelper.setTo("2860698744@qq.com");
        }
        mimeMessageHelper.setText(msg,true);
        javaMailSender.send(mimeMessage);
    }
}
