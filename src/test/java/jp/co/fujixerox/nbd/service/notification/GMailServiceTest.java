package jp.co.fujixerox.nbd.service.notification;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.gmail.model.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GMailServiceTest {
    @Autowired
    private GMailService gMailService;

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
            gMailService.send(message);
        } catch (GoogleJsonResponseException e) {
            e.printStackTrace();
            fail();
        }
    }
}