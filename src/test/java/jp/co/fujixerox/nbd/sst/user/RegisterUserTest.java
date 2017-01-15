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
public class RegisterUserTest {
    private static final String TARGET_PATH = "/torica/api/users/";

    private static class TestUser {
        private static String id = "fx00000";
        private static String name = "未踏ろく太";
        private static String address = "mitoh.rokuta@hogehoge.com";
    }

    private static class ConflictUser {
        private static String id = "fx54321";
        private static String name = "府画ふが子";
        private static String address = "fuga.fugako@hogehoge.com";
    }

    public static class Success extends SstTestBase {
        @Test
        public void ok() throws Exception {
            Map<String, Object> parameters = new HashedMap();
            parameters.put("id", TestUser.id);
            parameters.put("name", TestUser.name);
            parameters.put("address", TestUser.address);

            MultiValueMap<String, String> headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);

            ResponseEntity<String> response = getRestTemplate()
                    .exchange(
                            TARGET_PATH,
                            HttpMethod.POST,
                            new HttpEntity<>(
                                    new ObjectMapper().writer().writeValueAsString(parameters),
                                    headers),
                            String.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getHeaders().getLocation().toString()).contains(TestUser.id);

            UserEntity registered = getEntityManager().find(UserEntity.class, TestUser.id);
            assertThat(registered).isNotNull();
            assertThat(registered.getName()).isEqualTo(TestUser.name);
            assertThat(registered.getAddress()).isEqualTo(TestUser.address);
            assertThat(registered.getIsAdmin()).isEqualTo(false);
        }
    }

    public static class FailureDueTo extends SstTestBase {
        @Test
        public void notEnoughParam() throws Exception {
            Map<String, Object> parameters = new HashedMap();
            parameters.put("id", TestUser.id);
            parameters.put("name", TestUser.name);
            /* parameters.put("address", TestUser1.address);*/

            MultiValueMap<String, String> headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);

            ResponseEntity<String> response = getRestTemplate()
                    .exchange(
                            TARGET_PATH,
                            HttpMethod.POST,
                            new HttpEntity<>(
                                    new ObjectMapper().writer().writeValueAsString(parameters),
                                    headers),
                            String.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        public void invalidValue() throws Exception {
            Map<String, Object> parameters = new HashedMap();
            parameters.put("id", TestUser.id);
            parameters.put("name", TestUser.name);
            parameters.put("address", "this_is_not_email");

            MultiValueMap<String, String> headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);

            ResponseEntity<String> response = getRestTemplate()
                    .exchange(
                            TARGET_PATH,
                            HttpMethod.POST,
                            new HttpEntity<>(
                                    new ObjectMapper().writer().writeValueAsString(parameters),
                                    headers),
                            String.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        public void conflict() throws Exception {
            Map<String, Object> parameters = new HashedMap();
            parameters.put("id", ConflictUser.id);
            parameters.put("name", ConflictUser.name);
            parameters.put("address", ConflictUser.address);

            MultiValueMap<String, String> headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);

            ResponseEntity<String> response = getRestTemplate()
                    .exchange(
                            TARGET_PATH,
                            HttpMethod.POST,
                            new HttpEntity<>(
                                    new ObjectMapper().writer().writeValueAsString(parameters),
                                    headers),
                            String.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        }
    }
}
