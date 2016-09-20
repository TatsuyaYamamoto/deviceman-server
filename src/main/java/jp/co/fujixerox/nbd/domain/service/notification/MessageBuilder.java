package jp.co.fujixerox.nbd.domain.service.notification;

import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.services.gmail.model.Message;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

public class MessageBuilder {
    private String from;
    private String to;
    private String subject;
    private String bodyText;

    public MessageBuilder(String from, String to){
        this.from = from;
        this.to = to;
    }
    public MessageBuilder subject(String subject){
        this.subject = subject;
        return this;
    }
    public MessageBuilder bodyText(String bodyText){
        this.bodyText = bodyText;
        return this;
    }

    public Message build(){
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        try{
            email.setFrom(new InternetAddress(this.from));
            email.addRecipient(
                    javax.mail.Message.RecipientType.TO,
                    new InternetAddress(this.to));
            email.setSubject(this.subject);
            email.setText(this.bodyText);


            email.writeTo(buffer);

        }catch(IOException | MessagingException e){

        }

        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);

        return message;
    }
}