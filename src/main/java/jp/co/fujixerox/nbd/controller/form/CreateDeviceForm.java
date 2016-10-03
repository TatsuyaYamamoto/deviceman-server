package jp.co.fujixerox.nbd.controller.form;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

public class CreateDeviceForm {

    @NotBlank(message = "端末IDは必須項目です。")
    @Getter
    @Setter
    private String id;

    @NotBlank(message = "端末名は必須項目です。")
    @Getter
    @Setter
    private String name;
}
