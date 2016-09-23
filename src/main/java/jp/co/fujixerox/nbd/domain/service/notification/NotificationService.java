package jp.co.fujixerox.nbd.domain.service.notification;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.api.services.gmail.model.Message;
import jp.co.fujixerox.nbd.ApplicationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NotificationService {
    private static final Logger logger = LogManager.getLogger(NotificationService.class);
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.JAPAN);

    private static final String SENDER_ADDRESS = "torica.fx@gmail.com";

    private static final String TEMPLATE_FILE_PATH_NEW_USER_BODY = "message-template/newuser-body.mustache";
    private static final String TEMPLATE_FILE_PATH_NEW_USER_SUBJECT = "message-template/newuser-subject.mustache";

    private static final String TEMPLATE_FILE_PATH_DELAYED_USER_BODY = "message-template/delayeduser-body.mustache";
    private static final String TEMPLATE_FILE_PATH_DELAYED_USER_SUBJECT = "message-template/delayeduser-subject.mustache";

    private static final String TEMPLATE_FILE_PATH_REMINDER_BODY = "message-template/reminder-body.mustache";
    private static final String TEMPLATE_FILE_PATH_REMINDER_SUBJECT = "message-template/reminder-subject.mustache";

    @Autowired
    private GMailService gMailService;

    /**
     * 新規ユーザーの通知を行う
     *
     * @param userId
     * @param userName
     * @param address
     * @throws ApplicationException
     */
    public void toNewUser(
            String userId,
            String userName,
            String address)
            throws ApplicationException {
        logger.entry(userId, userName, address);

        Map<String, String> bindResource = new HashMap();
        bindResource.put("receiver.name", userName);
        bindResource.put("receiver.id", userId);
        bindResource.put("receiver.address", address);

        try {
            Message message = new MessageBuilder(SENDER_ADDRESS, address)
                    .subject(createMessage(TEMPLATE_FILE_PATH_NEW_USER_SUBJECT))
                    .bodyText(createMessage(TEMPLATE_FILE_PATH_NEW_USER_BODY, bindResource))
                    .build();

            Message response = gMailService.send(message);
            logger.traceExit("success to notificate to new user. {}", response);
        } catch (MessagingException | IOException e) {
            logger.catching(e);
            throw new ApplicationException(e);
        }
    }

    /**
     * 返却予定日を過ぎているユーザーに通知を行う
     *
     * @param userName
     * @param dueReturnTime
     * @param address
     * @throws ApplicationException
     */
    public void toDelayedUser(
            String userName,
            String deviceName,
            long dueReturnTime,
            String address) throws ApplicationException {

        Map<String, String> bindResource = new HashMap();
        bindResource.put("receiver.name", userName);
        bindResource.put("device.name", deviceName);
        bindResource.put("dueReturnTime", sdf.format(new Date(dueReturnTime)));

        try {
            Message message = new MessageBuilder(SENDER_ADDRESS, address)
                    .subject(createMessage(TEMPLATE_FILE_PATH_DELAYED_USER_SUBJECT))
                    .bodyText(createMessage(TEMPLATE_FILE_PATH_DELAYED_USER_BODY, bindResource))
                    .build();

            gMailService.send(message);
        } catch (MessagingException | IOException e) {
            logger.catching(e);
            throw new ApplicationException(e);
        }
    }

    /**
     * 返却予定日のリマインダーを送る
     *
     * @param userName
     * @param dueReturnTime
     * @param address
     * @throws ApplicationException
     */
    public void reminder(
            String userName,
            String deviceName,
            long dueReturnTime,
            String address) throws ApplicationException {

        Map<String, String> bindResource = new HashMap();
        bindResource.put("receiver.name", userName);
        bindResource.put("device.name", deviceName);
        bindResource.put("device.dueReturnTime", sdf.format(new Date(dueReturnTime)));

        try {
            Message message = new MessageBuilder(SENDER_ADDRESS, address)
                    .subject(createMessage(TEMPLATE_FILE_PATH_REMINDER_SUBJECT))
                    .bodyText(createMessage(TEMPLATE_FILE_PATH_REMINDER_BODY, bindResource))
                    .build();

            gMailService.send(message);
        } catch (MessagingException | IOException e) {
            logger.catching(e);
            throw new ApplicationException(e);
        }

    }

    /**
     * テンプレートファイルから文字列を取得する
     *
     * @param templatePath
     * @return
     * @throws IOException
     */
    private String createMessage(String templatePath) throws IOException{
        String filePath = NotificationService.class.getClassLoader().getResource(templatePath).getPath();
        return Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    /**
     * テンプレートファイルから文字列を取得する
     *
     * @param templatePath
     * @param bindResource
     * @return
     * @throws IOException
     */
    private String createMessage(String templatePath, Map bindResource) throws IOException{
        Writer writer = new StringWriter();

        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile(NotificationService.class.getClassLoader().getResource(templatePath).getPath());
        mustache.execute(writer, bindResource).flush();

        return writer.toString();
    }
}