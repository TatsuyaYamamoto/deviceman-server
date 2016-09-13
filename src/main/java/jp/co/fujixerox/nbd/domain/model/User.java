package jp.co.fujixerox.nbd.domain.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {

    /**
     * 社員番号
     */
    @Id
    @Column(name = "ID")
    @Getter
    @Setter
    private String id;


    /**
     * アプリとかの表示名
     */
    @Column(name = "NAME", nullable = false, unique = false)
    @Getter
    @Setter
    private String name;

    /**
     * 通知アドレス
     */
    @Column(name = "ADDRESS", nullable = false)
    @Getter
    @Setter
    private String address;

    /**
     * 登録日
     */
    @Column(name = "CREATED", nullable = false)
    @Getter
    @Setter
    private Long created;
}
