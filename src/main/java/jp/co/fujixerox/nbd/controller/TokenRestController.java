package jp.co.fujixerox.nbd.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import jp.co.fujixerox.nbd.ApplicationProperties;
import jp.co.fujixerox.nbd.controller.form.GetTokenForm;
import jp.co.fujixerox.nbd.persistence.entity.UserEntity;
import jp.co.fujixerox.nbd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/token")
public class TokenRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationProperties properties;

    /**
     * トークンを取得する
     *
     * @return
     */
    @RequestMapping(
            value = "/",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getToken(
            @RequestBody @Valid GetTokenForm form,
            BindingResult bindingResult,
            UriComponentsBuilder uriBuilder) {

        /* bean validation */
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        /* check password */
        if (!userService.verifyPassword(form.getId(), form.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        /* generate token */
        String token = null;
        UserEntity user = userService.getById(form.getId());
        try {
            token = JWT.create()
                    .withJWTId(UUID.randomUUID().toString())
                    .withIssuer(properties.getToken().getIssuer())
                    .withIssuedAt(new Date())
                    .withClaim("user_id", user.getId())
                    .withClaim("admin", user.getIsAdmin())
                    .sign(Algorithm.HMAC256("secret"));
        } catch (JWTCreationException | UnsupportedEncodingException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }

        return ResponseEntity.ok().body(
                String.format("{token:\"%s\"}", token));
    }
}
