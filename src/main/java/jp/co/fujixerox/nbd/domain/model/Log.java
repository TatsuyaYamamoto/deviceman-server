package jp.co.fujixerox.nbd.domain.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    @Getter
    @Setter
    private Integer id;

    /**
     * 貸出端末
     */
    @Column(name = "DEVICE_ID", nullable = false)
    @Getter
    @Setter
    private String deviceId;

    /**
     * 借り出しユーザID
     */
    @Column(name = "USER_ID", nullable = false)
    @Getter
    @Setter
    private String userId;

    /**
     * 貸出日
     */
    @Column(name = "CHECK_OUT_TIME", nullable = false)
    @Getter
    @Setter
    private Long checkOutTime;

    /**
     * 返却日
     */
    @Column(name = "RETURN_TIME", nullable = false)
    @Getter
    @Setter
    private Long returnTime;

    /**
     * リレーション deviceオブジェクト
     */
    @JoinColumn(name = "DEVICE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    @Getter
    @Setter
    private Device device;

    /**
     * リレーション userオブジェクト
     */
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    @Getter
    @Setter
    private User user;
}
