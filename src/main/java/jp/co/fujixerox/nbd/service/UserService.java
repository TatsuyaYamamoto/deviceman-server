package jp.co.fujixerox.nbd.service;

import jp.co.fujixerox.nbd.ApplicationException;
import jp.co.fujixerox.nbd.HttpError;
import jp.co.fujixerox.nbd.persistence.entity.UserEntity;
import jp.co.fujixerox.nbd.persistence.repository.UserRepository;
import jp.co.fujixerox.nbd.persistence.specification.UserSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static jp.co.fujixerox.nbd.persistence.specification.UserSpecification.contain;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Transactional(readOnly = true)
public class UserService {
    private static Logger logger = LogManager.getLogger(UserService.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository repository;

    public UserEntity getById(String id){
        return repository.findOne(id);
    }

    public List<UserEntity> getAll(int offset, int limit){
        return repository.findAll(new PageRequest(offset, limit)).getContent();
    }

    public List<UserEntity> getAll(int offset, int limit, String query){
        return repository.findAll(
                Specifications
                        .where(contain(UserSpecification.Columns.ID, query))
                        .or(contain(UserSpecification.Columns.NAME, query))
                        .or(contain(UserSpecification.Columns.ADDRESS, query))
                ,new PageRequest(offset, limit)).getContent();
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
