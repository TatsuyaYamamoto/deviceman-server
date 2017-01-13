package jp.co.fujixerox.nbd.service;

import jp.co.fujixerox.nbd.ApplicationException;
import jp.co.fujixerox.nbd.HttpError;
import jp.co.fujixerox.nbd.domain.model.User;
import jp.co.fujixerox.nbd.domain.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Transactional(readOnly = true)
public class UserService {
    private static Logger logger = LogManager.getLogger(UserService.class);
    private static String ENCRYPT_ALGORITHM = "MD5";

    @Autowired
    UserRepository repository;

    public List<User> getAll(int offset, int limit){
        return repository.findAll();
    }

    public User getById(String id){
        logger.entry(id);
        User user = repository.findOne(id);
        return logger.traceExit(user);
    }

    @Transactional(readOnly = false)
    public void register(
            String id,
            String name,
            String address) throws ApplicationException {
        logger.entry(id, name, address);

        if(repository.exists(id)){
            logger.trace("cannot register as a result of to conflict (ID: {})", id);
            throw new ApplicationException(HttpError.ENTITY_CONFLICT);
        }

        User newUser = new User();
        newUser.setId(id);
        newUser.setName(name);
        newUser.setAddress(address);
        newUser.setCreated(System.currentTimeMillis());

        repository.save(newUser);
        logger.traceExit("success to save a new user, {}", newUser);
    }

    private String encryptPassword(String rawPassword){
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance(ENCRYPT_ALGORITHM);
            messageDigest.update(rawPassword.getBytes());

            return Base64.getEncoder().encodeToString(messageDigest.digest());

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("暗号化出来ませんでした。");
        }
    }
}
