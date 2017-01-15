package jp.co.fujixerox.nbd.controller.form;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

@Getter
@Setter
public class CreateUserForm {
    @NotBlank
    private String id;

    @NotBlank
    private String name;

    @NotBlank
    private String address;
}
