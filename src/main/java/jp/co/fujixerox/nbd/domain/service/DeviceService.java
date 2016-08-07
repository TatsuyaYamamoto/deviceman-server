package jp.co.fujixerox.nbd.domain.service;

import jp.co.fujixerox.nbd.ApplicationException;
import jp.co.fujixerox.nbd.HttpError;
import jp.co.fujixerox.nbd.domain.model.CheckOut;
import jp.co.fujixerox.nbd.domain.model.Device;
import jp.co.fujixerox.nbd.domain.model.Log;
import jp.co.fujixerox.nbd.domain.repository.CheckOutRepository;
import jp.co.fujixerox.nbd.domain.repository.DeviceRepository;
import jp.co.fujixerox.nbd.domain.repository.LogRepository;
import jp.co.fujixerox.nbd.domain.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
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

    public List<Device> getAll(int offset, int limit){
        logger.entry(offset, limit);
        List<Device> devices = deviceRepo.findAll();
        return logger.traceExit(devices);
    }

    public Device getById(String deviceId){
        logger.entry(deviceId);
        Device device = deviceRepo.findOne(deviceId);
        return logger.traceExit(device);
    }

    @Transactional(readOnly = false)
    public void register(
            String id,
            String name) throws ApplicationException {
        logger.entry(id, name);

        if(deviceRepo.exists(id)){
            logger.trace("cannot register a device as a result of to conflict (ID: {})", id);
            throw new ApplicationException(HttpError.ENTITY_CONFLICT);
        }

        Device newDevice = new Device();
        newDevice.setId(id);
        newDevice.setName(name);
        newDevice.setCreated(System.currentTimeMillis());

        deviceRepo.save(newDevice);

        logger.traceExit("success to save a new device, {}", newDevice);
    }

    @Transactional(readOnly = false)
    public void checkout(
            String userId,
            String deviceId,
            long dueReturnTime) throws ApplicationException {
        logger.entry(userId, deviceId, dueReturnTime);

        // 入力値確認
        if(!userRepo.exists(userId)){
            logger.trace("cannnot checkout not for storing a user (ID: {}))", userId);
            throw new ApplicationException(HttpError.ENTITY_NOT_FOUND);
        }
        if(!deviceRepo.exists(deviceId)){
            logger.trace("cannnot checkout not for storing a device (ID: {}))", deviceId);
            throw new ApplicationException(HttpError.ENTITY_NOT_FOUND);
        }

        CheckOut co = checkOutRepo.findByUserIdAndDeviceId(userId, deviceId);
        if (co != null) {
            // 貸出中
            logger.trace("cannnot checkout for to confict checking out)", deviceId);
            throw new ApplicationException(HttpError.CHECKOUT_CONFLICT);
        }

        if(dueReturnTime < System.currentTimeMillis()){
            // 今日の日付より古い返却日を指定させない
            logger.trace("cannnot checkout as a result of requested date later than now)", deviceId);
            throw new ApplicationException(HttpError.ILLEGAL_DATE);
        }

        co = new CheckOut();
        co.setUserId(userId);
        co.setDeviceId(deviceId);
        co.setCheckOutTime(System.currentTimeMillis());
        co.setDueReturnTime(dueReturnTime);
        checkOutRepo.save(co);

        logger.traceExit("success to checkout, {}", co);
    }

    @Transactional(readOnly = false)
    public void updateCheckout(
            String userId,
            String deviceId,
            long dueReturnTime) throws ApplicationException {
        logger.entry(userId, deviceId, dueReturnTime);

        // 入力値確認
        if(!userRepo.exists(userId)){
            logger.trace("cannnot checkout not for storing a user (ID: {}))", userId);
            throw new ApplicationException(HttpError.ENTITY_NOT_FOUND);
        }
        if(!deviceRepo.exists(deviceId)){
            logger.trace("cannnot checkout not for storing a device (ID: {}))", deviceId);
            throw new ApplicationException(HttpError.ENTITY_NOT_FOUND);
        }

        CheckOut co = checkOutRepo.findByUserIdAndDeviceId(userId, deviceId);

        // 未貸出中の端末は更新できない
        if (co == null) {
            throw logger.throwing(new ApplicationException(HttpError.NOT_CHECKOUT));
        }

        // 更新前の返却予定日、現在時刻より古い返却予定日を指定させない
        if(dueReturnTime < co.getDueReturnTime() || dueReturnTime < System.currentTimeMillis()){
            logger.trace("cannnot checkout as a result of requested date later than now)", deviceId);
            throw new ApplicationException(HttpError.ILLEGAL_DATE);
        }


        co.setDueReturnTime(dueReturnTime);
        checkOutRepo.save(co);

        logger.traceExit("success to update checkout, {}", co);
    }

    @Transactional(readOnly = false)
    public void returnDevice(
            String userId,
            String deviceId) throws ApplicationException {
        logger.entry(userId, deviceId);


        // 入力値確認
        if(!userRepo.exists(userId)){
            logger.trace("cannnot checkout not for storing a user (ID: {}))", userId);
            throw new ApplicationException(HttpError.ENTITY_NOT_FOUND);
        }

        CheckOut co = checkOutRepo.findByDeviceId(deviceId);

        if(co == null){
            // 未貸出状態
            logger.trace("not checked out");
            throw logger.throwing(new ApplicationException(HttpError.NOT_CHECKOUT));
        }

        Log log = new Log();
        log.setUserId(co.getUserId());
        log.setDeviceId(co.getDeviceId());
        log.setCheckOutTime(co.getCheckOutTime());
        log.setReturnTime(System.currentTimeMillis());

        checkOutRepo.delete(co);
        logRepo.save(log);

        logger.traceExit("success to return device , {}", co);
        logger.traceExit("success to logging, {}", log);
    }
}
