package jp.co.fujixerox.nbd.service;

import jp.co.fujixerox.nbd.ApplicationException;
import jp.co.fujixerox.nbd.HttpError;
import jp.co.fujixerox.nbd.persistence.entity.UserEntity;
import jp.co.fujixerox.nbd.persistence.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository repository;

    public List<UserEntity> getAll(int offset, int limit){
        return repository.findAll();
    }

    public UserEntity getById(String id){
        logger.entry(id);
        UserEntity userEntity = repository.findOne(id);
        return logger.traceExit(userEntity);
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

        UserEntity newUserEntity = new UserEntity(
                id,
                name,
                address,
                passwordEncoder.encode(id),
                false);

        repository.save(newUserEntity);
        logger.traceExit("success to save a new user, {}", newUserEntity);
    }
}
