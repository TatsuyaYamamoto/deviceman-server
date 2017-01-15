package jp.co.fujixerox.nbd.service;

import jp.co.fujixerox.nbd.ApplicationException;
import jp.co.fujixerox.nbd.HttpError;
import jp.co.fujixerox.nbd.exception.ConflictException;
import jp.co.fujixerox.nbd.persistence.entity.CheckOutEntity;
import jp.co.fujixerox.nbd.persistence.entity.CheckOutLogEntity;
import jp.co.fujixerox.nbd.persistence.entity.DeviceEntity;
import jp.co.fujixerox.nbd.persistence.repository.CheckOutRepository;
import jp.co.fujixerox.nbd.persistence.repository.DeviceRepository;
import jp.co.fujixerox.nbd.persistence.repository.LogRepository;
import jp.co.fujixerox.nbd.persistence.repository.UserRepository;
import jp.co.fujixerox.nbd.persistence.specification.DeviceSpecification;
import jp.co.fujixerox.nbd.service.validate.BeanValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
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


    @Autowired
    private BeanValidator<DeviceEntity> validator;

    public DeviceEntity getById(String id) {
        if (deviceRepo.exists(id)) {
            return deviceRepo.findOne(id);
        } else {
            throw new EntityNotFoundException();
        }
    }

    public List<DeviceEntity> getAll(String query) {
        return deviceRepo.findAll(
                Specifications
                        .where(contain(DeviceSpecification.Columns.id, query))
                        .or(contain(DeviceSpecification.Columns.name, query))
                        .or(contain(DeviceSpecification.Columns.macAddress, query)));
    }

    /**
     * 端末を登録する
     * IDは小文字に変換される
     *
     * @param id
     * @param macAddress
     * @param name
     * @throws ConflictException
     * @throws ConstraintViolationException
     */
    @Transactional(readOnly = false)
    public void register(
            String id,
            String macAddress,
            String name) throws ConflictException, ConstraintViolationException {
        logger.entry(id, name);

        if (deviceRepo.exists(id)) {
            String message = String.format("cannot register as a result of to conflict (ID: %s)", id);
            throw new ConflictException(message);
        }

        DeviceEntity newDeviceEntity = new DeviceEntity(
                id,
                macAddress.toLowerCase(),
                name,
                false
        );

        validator.validate(newDeviceEntity);
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
