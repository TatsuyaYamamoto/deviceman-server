package jp.co.fujixerox.nbd.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "CHECKOUT_LOGS")
@Getter
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CheckOutLogEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 借り出しID
     */
    @Id
    @Column(name = "CHECKOUT_ID", nullable = false)
    private Integer checkoutId;

    /**
     * 貸出端末
     */
    @Column(name = "DEVICE_ID", nullable = false)
    private String deviceId;

    /**
     * 借り出しユーザID
     */
    @Column(name = "USER_ID", nullable = false)
    private String userId;

    /**
     * 貸出日
     */
    @Column(name = "CHECK_OUT_TIME", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date checkOutTime;

    /**
     * 返却予定日
     */
    @Column(name = "DUE_RETURN_TIME", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dueReturnTime;

    /**
     * 返却日
     */
    @Column(name = "RETURN_TIME", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date returnTime;

    /******************************************************************
     * コンストラクタ
     */
    protected CheckOutLogEntity() {

    }

    public CheckOutLogEntity(@NonNull CheckOutEntity checkOut, @NonNull Date returnTime) {
        this.checkoutId = checkOut.getId();
        this.deviceId = checkOut.getDeviceId();
        this.userId = checkOut.getUserId();
        this.checkOutTime = checkOut.getCheckOutTime();
        this.dueReturnTime = checkOut.getDueReturnTime();
        this.returnTime = returnTime;
    }
}
