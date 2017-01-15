package jp.co.fujixerox.nbd.sst.devices;

import jp.co.fujixerox.nbd.sst.SstTestBase;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Enclosed.class)
public class GetDeviceTest {
    private static final String TARGET_PATH = "/torica/api/devices/";

    private static class TestDevice {
        private static String id = "098765432109876";
        private static String name = "sudeniR 001";
        private static String macAddress = "qq:ww:ee:rr:tt:yy";
    }

    public static class Success extends SstTestBase {
        @Test
        public void ok() throws Exception {
            ResponseEntity<String> response = getRestTemplate()
                    .exchange(
                            TARGET_PATH + TestDevice.id,
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
