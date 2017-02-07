package jp.co.fujixerox.nbd.service.notification;

import org.junit.Test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class NotificationServiceTest {
    private static final String TEMPLATE_FILE_PATH_NEW_USER = "message-template/newuser-body.mustache";

    @Test
    public void test_createMessage_テンプレートからメッセージ文字列を構築できる() throws Exception {
        String userId = "userIDDDDD";
        String userName = "usernameeeeee";
        String address = "adddddressssssss";

        Map<String, String> bindResource = new HashMap();
        bindResource.put("receiver.name", userId);
        bindResource.put("receiver.id", userName);
        bindResource.put("receiver.address", address);
        bindResource.put("representative.name", userName);

        NotificationService notificationService = new NotificationService();
        Method method = NotificationService.class.getDeclaredMethod("createMessage", String.class, Map.class);
        method.setAccessible(true);
        String actual = (String)method.invoke(notificationService, TEMPLATE_FILE_PATH_NEW_USER, bindResource);
        System.out.println(actual);

        assertTrue(actual.contains(userId));
        assertTrue(actual.contains(userName));
        assertTrue(actual.contains(address));
    }
}