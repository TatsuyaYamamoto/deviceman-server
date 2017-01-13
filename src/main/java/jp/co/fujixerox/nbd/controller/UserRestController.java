package jp.co.fujixerox.nbd.controller;

import jp.co.fujixerox.nbd.ApplicationException;
import jp.co.fujixerox.nbd.controller.form.CreateUserForm;
import jp.co.fujixerox.nbd.domain.model.User;
import jp.co.fujixerox.nbd.service.UserService;
import jp.co.fujixerox.nbd.exception.InvalidRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

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

    /**
     * 登録済みユーザーリストをすべて取得する
     * @return
     */
    @RequestMapping(
            value = "/",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getAll(){
        List<User> users = userService.getAll(0, 0);

        Map<String, List> response = new HashMap();
        response.put("users", users);

        return new ResponseEntity(response, HttpStatus.OK);
    }

    /**
     * ユーザID検索
     * @param id
     * @return
     */
    @RequestMapping(
            value = "/{userId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getOne(
            @PathVariable("userId") String id){
        User user = userService.getById(id);

        if(user == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(user, HttpStatus.OK);
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
    public ResponseEntity register(
            @RequestBody @Valid CreateUserForm userForm,
            BindingResult bindingResult,
            UriComponentsBuilder uriBuilder){

        /* bean validation */
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException(bindingResult.toString(), bindingResult);
        }

        /* execute */
        try {
            userService.register(userForm.getId(), userForm.getName(), userForm.getAddress());
        } catch (ApplicationException e) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }

        /* messaging */
        Message<String> message = MessageBuilder
                .withPayload(userForm.getId())
                .build();
        jmsMessagingTemplate.send("new-user", message);

        /* response */
        URI location = uriBuilder.path("api/users/{userId}")
                .buildAndExpand(userForm.getId())
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);

        return new ResponseEntity(headers, HttpStatus.ACCEPTED);
    }
}
