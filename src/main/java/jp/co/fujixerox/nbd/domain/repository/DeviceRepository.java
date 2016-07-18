package jp.co.fujixerox.nbd.domain.repository;

import jp.co.fujixerox.nbd.domain.model.Device;
import jp.co.fujixerox.nbd.domain.model.User;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public interface DeviceRepository extends JpaRepository<Device, String>, JpaSpecificationExecutor<Device> {}