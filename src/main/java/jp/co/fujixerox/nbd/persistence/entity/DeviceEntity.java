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
@Table(name = "DEVICES")
@Getter
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DeviceEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 端末識別子。IMEI
     */
    @Id
    @Column(name = "ID")
    @Pattern(regexp = "[0-9]{15}")  // IMEI
    private String id;

    /**
     * MACアドレス
     */
    @Column(name = "MAC_ADDRESS", nullable = false)
    @Pattern(regexp = "([0-9A-Fa-f]{2}:){5}[0-9A-Fa-f]{2}")
    private String macAddress;

    /**
     * 端末名
     */
    @Column(name = "NAME", nullable = false)
    @Size(max = 60)
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
            @NonNull String id,
            @NonNull String macAddress,
            @NonNull String name,
            boolean hasWirelessLan) {
        this.id = id;
        this.macAddress = macAddress;
        this.name = name;
        this.hasWirelessLan = hasWirelessLan;
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
