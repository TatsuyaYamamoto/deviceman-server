package jp.co.fujixerox.nbd.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "USERS")
@Getter
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 社員番号
     */
    @Id
    @Column(name = "ID")
    @Pattern(regexp = "fx[0-9]{5}")
    private String id;


    /**
     * アプリとかの表示名
     */
    @Column(name = "NAME", nullable = false, unique = false)
    @Size(max = 60)
    @Setter
    private String name;

    /**
     * 通知アドレス
     */
    @Column(name = "ADDRESS", nullable = false)
    @Pattern(regexp = "[a-z0-9\\+\\-_]+(\\.[a-z0-9\\+\\-_]+)*@([a-z0-9\\-]+\\.)+[a-z]{2,6}")
    @Setter
    private String address;

    /**
     * パスワード
     */
    @Column(name = "PASSWORD", nullable = false)
    @Size(min = 4)
    @Pattern(regexp = "[\\x20-\\x7e]+") // ascii
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
        updated = new Date();
    }

    @PreUpdate
    public void onUpdate() {
        updated = new Date();
    }
}
