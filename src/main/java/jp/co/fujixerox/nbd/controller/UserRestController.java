package jp.co.fujixerox.nbd.controller;

import jp.co.fujixerox.nbd.ApplicationException;
import jp.co.fujixerox.nbd.controller.form.CreateUserForm;
import jp.co.fujixerox.nbd.domain.model.User;
import jp.co.fujixerox.nbd.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    UserService userService;

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

    @RequestMapping(
            value = "/",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity register(
            @RequestBody
            @Valid
                    CreateUserForm userForm,
            UriComponentsBuilder uriBuilder){

        try {
            userService.register(userForm.getId(), userForm.getName(), userForm.getAddress(), userForm.getPassword());
        } catch (ApplicationException e) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }

        URI location = uriBuilder.path("api/users/{userId}")
                .buildAndExpand(userForm.getId())
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }
}
