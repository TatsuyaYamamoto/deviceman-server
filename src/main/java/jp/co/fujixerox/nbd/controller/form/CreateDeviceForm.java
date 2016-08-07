package jp.co.fujixerox.nbd.controller.form;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

public class CreateDeviceForm {

    @NotBlank
    @Getter
    @Setter
    private String id;

    @NotBlank
    @Getter
    @Setter
    private String name;
}
