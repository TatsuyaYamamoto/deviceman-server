package jp.co.fujixerox.nbd.persistence.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "DEVICES")
@Getter
public class DeviceEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 端末識別子。UUID
     */
    @Id
    @Column(name = "ID")
    private String uuid;

    /**
     * MACアドレス
     */
    @Column(name = "MAC_ADDRESS", nullable = false)
    private String macAddress;

    /**
     * 端末名
     */
    @Column(name = "NAME", nullable = false)
    @Setter
    private String name;

    /**
     * 無線LAN登録済みか
     */
    @Column(name = "HAS_WIRELESS_LAN", nullable = false)
    @Setter
    private Boolean hasWirelessLan;

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

    protected DeviceEntity() {
    }

    public DeviceEntity(
            @NonNull String uuid,
            @NonNull String macAddress,
            @NonNull String name,
            boolean hasWirelessLan) {
        this.uuid = uuid;
        this.macAddress = macAddress;
        this.name = name;
        this.hasWirelessLan = hasWirelessLan;
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
