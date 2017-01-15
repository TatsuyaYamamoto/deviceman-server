package jp.co.fujixerox.nbd.sst.user;

import jp.co.fujixerox.nbd.sst.SstTestBase;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@RunWith(Enclosed.class)
public class FindUsers {
    private static final String TARGET_PATH = "/torica/api/users/";

    public static class Success extends SstTestBase {
        @Test
        public void ok() throws Exception {
            ResponseEntity<String> response = getRestTemplate()
                    .exchange(
                            TARGET_PATH,
                            HttpMethod.GET,
                            null,
                            String.class);

            assertThat(response.getBody(), hasJsonPath("$.users"));
            assertThat(response.getBody(), hasJsonPath("$.users[*]", hasSize(2)));
        }

        @Test
        public void withQuery() throws Exception {
            ResponseEntity<String> response = getRestTemplate()
                    .exchange(
                            TARGET_PATH + "?query=hoge.hogeo@hogehoge.com",
                            HttpMethod.GET,
                            null,
                            String.class);

            assertThat(response.getBody(), hasJsonPath("$.users"));
            assertThat(response.getBody(), hasJsonPath("$.users[*]", hasSize(1)));
            assertThat(response.getBody(), hasJsonPath("$.users[0].id", is("fx12345")));
        }
    }
}
