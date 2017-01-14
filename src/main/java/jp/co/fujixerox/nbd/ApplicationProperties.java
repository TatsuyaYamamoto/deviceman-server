package jp.co.fujixerox.nbd;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "custom")
@Getter
@Setter
public class ApplicationProperties {
    private Message message;

    @Getter
    @Setter
    public static class Message {
        private boolean available;
        private String refreshToken;
    }
}
