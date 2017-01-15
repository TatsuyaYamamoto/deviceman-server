package jp.co.fujixerox.nbd.sst.devices;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.fujixerox.nbd.persistence.entity.DeviceEntity;
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
public class RegisterDeviceTest {
    private static final String TARGET_PATH = "/torica/api/devices/";

    private static class TestDevice {
        private static String id = "123456789012345";
        private static String name = "まにゅふぁくちゃ社　スーパーデバイス";
        private static String macAddress = "11:aa:22:bb:cc:dd";
    }

    private static class ConflictDevice {
        private static String id = "098765432109876";
        private static String name = "sudeniR 001";
        private static String macAddress = "qq:ww:ee:rr:tt:yy";
    }

    public static class Success extends SstTestBase {
        @Test
        public void ok() throws Exception {
            Map<String, Object> parameters = new HashedMap();
            parameters.put("id", TestDevice.id);
            parameters.put("name", TestDevice.name);
            parameters.put("macAddress", TestDevice.macAddress);

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
            assertThat(response.getHeaders().getLocation().toString()).contains(TestDevice.id);

            DeviceEntity registered = getEntityManager().find(DeviceEntity.class, TestDevice.id);
            assertThat(registered).isNotNull();
            assertThat(registered.getName()).isEqualTo(TestDevice.name);
            assertThat(registered.getMacAddress()).isEqualTo(TestDevice.macAddress);
        }
    }

    public static class FailureDueTo extends SstTestBase {
        @Test
        public void notEnoughParam() throws Exception {
            Map<String, Object> parameters = new HashedMap();
            parameters.put("id", TestDevice.id);
            parameters.put("name", TestDevice.name);
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
            parameters.put("id", TestDevice.id);
            parameters.put("name", TestDevice.name);
            parameters.put("macAddress", "this_is_not_mac_address");

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
            parameters.put("id", ConflictDevice.id);
            parameters.put("name", ConflictDevice.name);
            parameters.put("macAddress", ConflictDevice.macAddress);

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
