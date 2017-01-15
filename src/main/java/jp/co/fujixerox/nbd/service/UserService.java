package jp.co.fujixerox.nbd.service;

import jp.co.fujixerox.nbd.exception.ConflictException;
import jp.co.fujixerox.nbd.persistence.entity.UserEntity;
import jp.co.fujixerox.nbd.persistence.repository.UserRepository;
import jp.co.fujixerox.nbd.persistence.specification.UserSpecification;
import jp.co.fujixerox.nbd.service.validate.BeanValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static jp.co.fujixerox.nbd.persistence.specification.UserSpecification.contain;

@Service
@Transactional(readOnly = true)
public class UserService {
    private static Logger logger = LogManager.getLogger(UserService.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private BeanValidator<UserEntity> validator;

    @Autowired
    UserRepository repository;

    /**
     * ID検索
     *
     * @param id
     * @return
     * @throws EntityNotFoundException
     */
    public UserEntity getById(String id) throws EntityNotFoundException {
        if (repository.exists(id)) {
            return repository.findOne(id);
        } else {
            throw new EntityNotFoundException();
        }
    }

    /**
     * キーワード検索
     *
     * @param query
     * @return
     */
    public List<UserEntity> getAll(String query) {
        return repository.findAll(
                Specifications
                        .where(contain(UserSpecification.Columns.id, query))
                        .or(contain(UserSpecification.Columns.name, query))
                        .or(contain(UserSpecification.Columns.address, query)));
    }

    /**
     * ユーザー登録
     *
     * @param id
     * @param name
     * @param address
     * @throws ConflictException
     */
    @Transactional(readOnly = false)
    public void register(
            String id,
            String name,
            String address) throws ConflictException {
        logger.entry(id, name, address);

        if (repository.exists(id)) {
            String message = String.format("cannot register as a result of to conflict (ID: %s)", id);
            throw new ConflictException(message);
        }

        UserEntity newUserEntity = new UserEntity(
                id,
                name,
                address,
                passwordEncoder.encode(id),
                false);
        validator.validate(newUserEntity);
        repository.save(newUserEntity);
        logger.traceExit("success to save a new user, {}", newUserEntity);
    }
}
