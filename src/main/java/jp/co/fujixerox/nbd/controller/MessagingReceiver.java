package jp.co.fujixerox.nbd.controller;

import jp.co.fujixerox.nbd.ApplicationException;
import jp.co.fujixerox.nbd.domain.model.User;
import jp.co.fujixerox.nbd.service.UserService;
import jp.co.fujixerox.nbd.service.notification.NotificationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class MessagingReceiver {
    private static final Logger logger = LogManager.getLogger(MessagingReceiver.class);
    @Autowired
    private NotificationService notification;

    @Autowired
    private UserService userService;

    /**
     * payload(userId)を用いて、新規ユーザー登録通知を実行する
     *
     * @param message
     * @throws ApplicationException
     */
    @JmsListener(destination = "new-user")
    public void handleNewUserNotificationMessage(Message<String> message) throws ApplicationException {
        logger.entry(message);
        logger.info("received new message. {}", message.getPayload());

        User newUser = userService.getById(message.getPayload());

        if(newUser == null){
            logger.error("received new user's id is not existed. fail messageing.");
            return;
        }

        notification.toNewUser(
                newUser.getId(),
                newUser.getName(),
                newUser.getAddress()
        );
        logger.traceExit("success messaging process! {}", message);
    }
}
