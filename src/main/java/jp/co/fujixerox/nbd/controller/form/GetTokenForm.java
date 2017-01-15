package jp.co.fujixerox.nbd.controller.form;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
public class GetTokenForm {

    @NotBlank(message = "端末IDは必須項目です。")
    private String id;

    @NotBlank(message = "端末IDは必須項目です。")
    private String password;
}
