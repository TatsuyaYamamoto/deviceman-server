package jp.co.fujixerox.nbd.controller;

import jp.co.fujixerox.nbd.ApplicationProperties;
import jp.co.fujixerox.nbd.controller.form.CreateUserForm;
import jp.co.fujixerox.nbd.exception.ConflictException;
import jp.co.fujixerox.nbd.persistence.entity.UserEntity;
import jp.co.fujixerox.nbd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    private ApplicationProperties properties;

    /**
     * 登録済みユーザーリストをすべて取得する
     *
     * @return
     */
    @RequestMapping(
            value = "/",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity findUsers(@RequestParam(name = "query", required = false) String query) {
        List<UserEntity> userEntities = userService.getAll(query);

        Map<String, List> response = new HashMap<>();
        response.put("users", userEntities);

        return ResponseEntity.ok().body(response);
    }

    /**
     * ユーザID検索
     *
     * @param id
     * @return
     */
    @RequestMapping(
            value = "/{userId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getUser(
            @PathVariable("userId") String id) {
        UserEntity userEntity;
        try {
            userEntity = userService.getById(id);
        } catch (Throwable e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().body(userEntity);
    }

    /**
     * ユーザーを新規作成する
     * DBへの反映成功後、
     *
     * @param userForm
     * @param uriBuilder
     * @return
     */
    @RequestMapping(
            value = "/",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity registerUser(
            @RequestBody @Valid CreateUserForm userForm,
            BindingResult bindingResult,
            UriComponentsBuilder uriBuilder) {

        /* bean validation */
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        /* execute */
        try {
            userService.register(userForm.getId(), userForm.getName(), userForm.getAddress());
        } catch (ConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        /* messaging */
        if (properties.getMessage().isAvailable()) {
            Message<String> message = MessageBuilder
                    .withPayload(userForm.getId())
                    .build();
            jmsMessagingTemplate.send("new-user", message);
        }

        /* response */
        URI location = uriBuilder.path("api/users/{userId}")
                .buildAndExpand(userForm.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
}
