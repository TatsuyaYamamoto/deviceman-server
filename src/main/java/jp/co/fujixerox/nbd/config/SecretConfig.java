package jp.co.fujixerox.nbd;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * secret.propertiesに対応する設定ファイル読み込みBean
 */
@Component
public class SecretConfig {
    @Value("${refresh_token}")
    public String REFRESH_TOKEN;
}