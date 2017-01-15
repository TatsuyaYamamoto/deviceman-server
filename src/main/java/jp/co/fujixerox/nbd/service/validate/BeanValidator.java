package jp.co.fujixerox.nbd.service.validate;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * Bean Validationを実行するコンポーネントクラス
 *
 * @param <T> validation taget class
 */
@Component
public class BeanValidator<T> {
    private javax.validation.Validator beanValidator;

    /**
     * init this Validator class.
     */
    @PostConstruct
    private void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.beanValidator = factory.getValidator();
    }

    /**
     * validate properties of the given bean.
     *
     * @param bean validation target Java Bean.
     * @throws ConstraintViolationException One or more properties have a violation.
     */
    public void validate(T bean) {
        Set<ConstraintViolation<T>> results = beanValidator.validate(bean);

        if (results.isEmpty()) {
            return;
        }

        throw new ConstraintViolationException(
                String.format("There are some bean validation violation. results=[ %s ]", results),
                results);
    }
}
