package jp.co.fujixerox.nbd.controller.form;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by TATSUYA-PC4 on 2016/07/16.
 */
public class CreateUserForm {
    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private String address;
}
