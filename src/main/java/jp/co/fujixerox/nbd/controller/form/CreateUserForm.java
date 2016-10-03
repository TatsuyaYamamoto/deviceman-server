package jp.co.fujixerox.nbd.controller.form;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

public class CreateUserForm {
    @NotBlank(message = "ユーザIDは必須項目です。")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "ユーザIDは英数字のみを使用してください。")
    @Getter
    @Setter
    private String id;

    @NotBlank(message = "ユーザ名は必須項目です。")
    @Getter
    @Setter
    private String name;

    @Email(message = "Emailアドレスの形式で文字を入力してください。")
    @NotBlank(message = "アドレスは必須項目です。")
    @Getter
    @Setter
    private String address;
}
