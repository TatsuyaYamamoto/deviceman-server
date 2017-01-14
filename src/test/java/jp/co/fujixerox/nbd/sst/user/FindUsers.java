package jp.co.fujixerox.nbd.sst.user;

import jp.co.fujixerox.nbd.sst.SstTestBase;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Enclosed.class)
public class FindUsers {
    private static final String TARGET_PATH = "/users/";

    public static class Success extends SstTestBase {
        @Test
        public void sample() throws Exception {
            String expectJson = loadText("classpath:" + "sst/expect/get_user_info_v1/success/sample.json");

            MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
            parameters.add("id", "member");

            MultiValueMap<String, String> headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);

            ResponseEntity<String> response = getRestTemplate()
                    .exchange(
                            TARGET_PATH,
                            HttpMethod.GET,
                            new HttpEntity<>(parameters, headers),
                            String.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            JSONAssert.assertEquals(expectJson, response.getBody(), false);
        }
    }

    public static class BadRequestValue extends SstTestBase {
    }
}
