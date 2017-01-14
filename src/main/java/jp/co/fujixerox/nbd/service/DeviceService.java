package jp.co.fujixerox.nbd.service;

import jp.co.fujixerox.nbd.ApplicationException;
import jp.co.fujixerox.nbd.HttpError;
import jp.co.fujixerox.nbd.persistence.entity.CheckOutEntity;
import jp.co.fujixerox.nbd.persistence.entity.CheckOutLogEntity;
import jp.co.fujixerox.nbd.persistence.entity.DeviceEntity;
import jp.co.fujixerox.nbd.persistence.repository.CheckOutRepository;
import jp.co.fujixerox.nbd.persistence.repository.DeviceRepository;
import jp.co.fujixerox.nbd.persistence.repository.LogRepository;
import jp.co.fujixerox.nbd.persistence.repository.UserRepository;
import jp.co.fujixerox.nbd.persistence.specification.DeviceSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static jp.co.fujixerox.nbd.persistence.specification.DeviceSpecification.contain;

@Service
@Transactional(readOnly = true)
public class DeviceService {
    private static Logger logger = LogManager.getLogger(DeviceService.class);

    @Autowired
    UserRepository userRepo;

    @Autowired
    DeviceRepository deviceRepo;

    @Autowired
    CheckOutRepository checkOutRepo;

    @Autowired
    LogRepository logRepo;

    public DeviceEntity getById(String id) {
        return deviceRepo.findOne(id);
    }

    public List<DeviceEntity> getAll(int offset, int limit) {
        return deviceRepo.findAll(new PageRequest(offset, limit)).getContent();
    }

    public List<DeviceEntity> getAll(int offset, int limit, String query) {
        return deviceRepo.findAll(
                Specifications
                        .where(contain(DeviceSpecification.Columns.ID, query))
                        .or(contain(DeviceSpecification.Columns.NAME, query))
                        .or(contain(DeviceSpecification.Columns.MAC_ADDRESS, query))
                , new PageRequest(offset, limit)).getContent();
    }

    /**
     * 端末を登録する
     * IDは小文字に変換される
     *
     * @param id
     * @param name
     * @throws ApplicationException
     */
    @Transactional(readOnly = false)
    public void register(
            String uuid,
            String macAddress,
            String name) throws ApplicationException {
        logger.entry(uuid, name);

        if (deviceRepo.exists(uuid)) {
            logger.trace("cannot register a device as a result of to conflict (ID: {})", uuid);
            throw new ApplicationException(HttpError.ENTITY_CONFLICT);
        }

        DeviceEntity newDeviceEntity = new DeviceEntity(
                uuid,
                macAddress.toLowerCase(),
                name,
                false
        );

        deviceRepo.save(newDeviceEntity);

        logger.traceExit("success to save a new device, {}", newDeviceEntity);
    }

    @Transactional(readOnly = false)
    public void checkout(
            String userId,
            String deviceId,
            long dueReturnTime) throws ApplicationException {
        logger.entry(userId, deviceId, dueReturnTime);

        // 入力値確認
        if (!userRepo.exists(userId)) {
            logger.trace("cannnot checkout not for storing a user (ID: {}))", userId);
            throw new ApplicationException(HttpError.ENTITY_NOT_FOUND);
        }
        if (!deviceRepo.exists(deviceId)) {
            logger.trace("cannnot checkout not for storing a device (ID: {}))", deviceId);
            throw new ApplicationException(HttpError.ENTITY_NOT_FOUND);
        }

        CheckOutEntity co = checkOutRepo.findByUserIdAndDeviceId(userId, deviceId);
        if (co != null) {
            // 貸出中
            logger.trace("cannnot checkout for to confict checking out)", deviceId);
            throw new ApplicationException(HttpError.CHECKOUT_CONFLICT);
        }

        if (dueReturnTime < System.currentTimeMillis()) {
            // 今日の日付より古い返却日を指定させない
            logger.trace("cannnot checkout as a result of requested date later than now)", deviceId);
            throw new ApplicationException(HttpError.ILLEGAL_DATE);
        }

        co = new CheckOutEntity(userId, deviceId, new Date(), new Date(dueReturnTime));
        checkOutRepo.save(co);

        logger.traceExit("success to checkout, {}", co);
    }

    @Transactional(readOnly = false)
    public void returnDevice(
            String userId,
            String deviceId) throws ApplicationException {
        logger.entry(userId, deviceId);

        // 入力値確認
        if (!userRepo.exists(userId)) {
            logger.trace("cannnot checkout not for storing a user (ID: {}))", userId);
            throw new ApplicationException(HttpError.ENTITY_NOT_FOUND);
        }

        CheckOutEntity co = checkOutRepo.findByDeviceId(deviceId);

        if (co == null) {
            // 未貸出状態
            logger.trace("not checked out");
            throw logger.throwing(new ApplicationException(HttpError.NOT_CHECKOUT));
        }

        CheckOutLogEntity checkOutLogEntity = new CheckOutLogEntity(co, new Date());

        checkOutRepo.delete(co);
        logRepo.save(checkOutLogEntity);

        logger.traceExit("success to return device , {}", co);
    }
}
