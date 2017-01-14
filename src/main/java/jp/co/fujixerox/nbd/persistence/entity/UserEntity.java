package jp.co.fujixerox.nbd.persistence.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "USERS")
@Getter
public class UserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 社員番号
     */
    @Id
    @Column(name = "ID")
    private String id;


    /**
     * アプリとかの表示名
     */
    @Column(name = "NAME", nullable = false, unique = false)
    @Setter
    private String name;

    /**
     * 通知アドレス
     */
    @Column(name = "ADDRESS", nullable = false)
    @Setter
    private String address;

    /**
     * 通知アドレス
     */
    @Column(name = "PASSWORD", nullable = false)
    @Setter
    private String password;

    /**
     * 管理者権限
     */
    @Column(name = "ADMIN", nullable = false)
    private Boolean isAdmin;

    /**
     * 登録日
     */
    @Column(name = "CREATED", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    /**
     * 更新日
     */
    @Column(name = "UPDATED", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    protected UserEntity() {
    }

    public UserEntity(
            @NonNull String id,
            @NonNull String name,
            @NonNull String address,
            @NonNull String password,
            boolean isAdmin) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    @PrePersist
    public void onPersist() {
        created = new Date();
    }

    @PreUpdate
    public void onUpdate() {
        updated = new Date();
    }
}
