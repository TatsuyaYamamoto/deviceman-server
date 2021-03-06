package jp.co.fujixerox.nbd.service.notification;

import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import jp.co.fujixerox.nbd.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GMailService {

    @Autowired
    private ApplicationProperties properties;

    private static final String PROXY_HOST = "proxy.fujixerox.co.jp";
    private static final int PROXY_PORT = 8080;

    private static final String APPLICATION_NAME = "Torica Mail Serivce";
    private static final String AUTHENTICATED_USER = "me";

    private static final String CLIENT_SECRET_RESOURCE_NAME = "/torica_gmail_client.json";

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport.Builder()
            .setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_HOST, PROXY_PORT)))
            .build();

    /**
     * GmailAPIを実行するサービスインスタンスを取得する
     *
     * @return
     * @throws IOException
     */
    public Gmail getGmailService() throws IOException {

        GoogleClientSecrets clientSecrets;

        try(InputStream in = GMailService.class.getResourceAsStream(CLIENT_SECRET_RESOURCE_NAME);
            InputStreamReader isr = new InputStreamReader(in)){
            clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, isr);
        }
        GoogleCredential credential = new GoogleCredential.Builder()
                .setJsonFactory(JSON_FACTORY)
                .setTransport(HTTP_TRANSPORT)
                .setClientSecrets(clientSecrets)
                .build()
                .setRefreshToken(properties.getMessage().getRefreshToken());

        return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     * メールを送信する
     *
     * @param message
     * @return
     * @throws MessagingException
     * @throws IOException
     */
    public Message send(Message message) throws MessagingException, IOException {
        Message response = getGmailService()
                .users()
                .messages()
                .send(AUTHENTICATED_USER, message)
                .execute();
        return response;
    }
}