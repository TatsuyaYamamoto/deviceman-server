package jp.co.fujixerox.nbd.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "CHECKOUT")
@Getter
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CheckOutEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 借り出しID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    /**
     * 借り出しユーザID
     */
    @Column(name = "USER_ID", nullable = false)
    private String userId;

    /**
     * 貸出端末
     */
    @Column(name = "DEVICE_ID", nullable = false, unique = true)
    private String deviceId;


    /**
     * 貸出日
     */
    @Column(name = "CHECKOUT_TIME", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date checkOutTime;

    /**
     * 返却予定日
     */
    @Column(name = "DUE_RETURN_TIME", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dueReturnTime;

    /******************************************************************
     * コンストラクタ
     */
    protected CheckOutEntity() {

    }

    public CheckOutEntity(
            String userId,
            String deviceId,
            Date checkOutTime,
            Date dueReturnTime) {
        this.userId = userId;
        this.deviceId = deviceId;
        this.checkOutTime = checkOutTime;
        this.dueReturnTime = dueReturnTime;
    }

    /******************************************************************
     * リレーションシップ
     */
    @JoinColumn(name = "DEVICE_ID", insertable = false, updatable = false)
    @ManyToOne
    private DeviceEntity device;

    @JoinColumn(name = "USER_ID", insertable = false, updatable = false)
    @ManyToOne
    private UserEntity user;
}
