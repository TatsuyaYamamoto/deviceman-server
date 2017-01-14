package jp.co.fujixerox.nbd.persistence.repository;

import jp.co.fujixerox.nbd.persistence.entity.CheckOutLogEntity;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface LogRepository extends JpaRepository<CheckOutLogEntity, Integer>, JpaSpecificationExecutor<CheckOutLogEntity> {}