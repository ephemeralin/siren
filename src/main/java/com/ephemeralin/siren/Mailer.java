package com.ephemeralin.siren;

import com.ephemeralin.siren.util.PropertiesStorage;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * The type Mailer.
 */
@lombok.extern.log4j.Log4j2
public class Mailer {
    private PropertiesStorage props;

    /**
     * Instantiates a new Mailer.
     */
    public Mailer() {
    }

    /**
     * Send email.
     *
     * @param subject the subject
     * @param body    the body
     * @param props   the props
     */
    public void sendEmail(String subject, String body, PropertiesStorage props) {
        log.info("Alert email sending start");
        Properties sessionProps = new Properties();
        sessionProps.put("mail.smtp.host", props.getProperty("mail.smtp.host"));
        sessionProps.put("mail.smtp.socketFactory.port", props.getProperty("mail.smtp.port"));
        sessionProps.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        sessionProps.put("mail.smtp.auth", props.getProperty("mail.smtp.auth"));
        sessionProps.put("mail.smtp.port", props.getProperty("mail.smtp.port"));
        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(props.getProperty("mail.login"),
                        props.getProperty("mail.password"));
            }
        };
        Session session = Session.getDefaultInstance(sessionProps, auth);
        try {
            MimeMessage msg = new MimeMessage(session);
            //set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress(props.getProperty("msg.from.address"),
                    props.getProperty("msg.from.personal")));
            msg.setReplyTo(InternetAddress.parse(props.getProperty("msg.from.address"), false));
            msg.setSubject(subject, "UTF-8");
            msg.setText(body, "UTF-8");
            msg.setSentDate(new Date());
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(props.getProperty("msg.to.address"), false));
            log.info("Message is ready");
            Transport.send(msg);
            log.info("Alert email sent successfully");
        } catch (Exception e) {
            log.error("Alert sending error", e);
        }
    }

}

