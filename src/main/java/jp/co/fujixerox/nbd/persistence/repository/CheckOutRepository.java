package jp.co.fujixerox.nbd.persistence.repository;

import jp.co.fujixerox.nbd.persistence.entity.CheckOutEntity;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface CheckOutRepository extends JpaRepository<CheckOutEntity, Integer>, JpaSpecificationExecutor<CheckOutEntity> {
    CheckOutEntity findByDeviceId(String deviceId);
    CheckOutEntity findByUserIdAndDeviceId(String userId, String deviceId);
}