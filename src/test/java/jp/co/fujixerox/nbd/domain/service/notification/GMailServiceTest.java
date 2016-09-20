package jp.co.fujixerox.nbd.domain.service.notification;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.gmail.model.Message;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.fail;

public class GMailServiceTest {
    @Test
    public void test_send_指定したアドレスにメールが送られる() throws Exception {
        String expectedFrom = "torica.fx@gmail.com";
        String expectedTo = "torica.fx@gmail.com";
        String expectedSubject = "test" + new Date().toString();
        String expectedBody = "テストです。";

        Message message = new MessageBuilder(expectedFrom, expectedTo)
                .subject(expectedSubject)
                .bodyText(expectedBody)
                .build();

        try {
            new GMailService().send(message);
        } catch (GoogleJsonResponseException e) {
            fail();
        }
    }
}