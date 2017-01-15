package jp.co.fujixerox.nbd.sst.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.fujixerox.nbd.persistence.entity.UserEntity;
import jp.co.fujixerox.nbd.sst.SstTestBase;
import org.apache.commons.collections.map.HashedMap;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Enclosed.class)
public class GetUser {
    private static final String TARGET_PATH = "/torica/api/users/";

    private static class TestUser {
        private static String id = "fx54321";
        private static String name = "府画ふが子";
        private static String address = "fuga.fugako@hogehoge.com";
    }

    public static class Success extends SstTestBase {
        @Test
        public void ok() throws Exception {
            ResponseEntity<String> response = getRestTemplate()
                    .exchange(
                            TARGET_PATH + TestUser.id,
                            HttpMethod.GET,
                            null,
                            String.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        }
    }
    public static class Failure extends SstTestBase {
        @Test
        public void ok() throws Exception {
            ResponseEntity<String> response = getRestTemplate()
                    .exchange(
                            TARGET_PATH + "notExistId",
                            HttpMethod.GET,
                            null,
                            String.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }
}
