package jp.co.fujixerox.nbd.domain.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
public class Device {
    /**
     * 端末識別子。MACアドレスとか
     */
    @Id
    @Column(name = "ID")
    @Getter
    @Setter
    private String id;

    /**
     * 端末名
     */
    @Column(name = "NAME", nullable = false, unique = true)
    @Getter
    @Setter
    private String name;

    /**
     * 登録日
     */
    @Column(name = "CREATED", nullable = false)
    @Getter
    @Setter
    private Long created;
}
