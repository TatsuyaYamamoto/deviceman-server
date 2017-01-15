package jp.co.fujixerox.nbd.persistence.specification;

import jp.co.fujixerox.nbd.persistence.entity.UserEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class UserSpecification {
    public enum Columns {
        id, name, address
    }

    /**
     * 指定カラムに対する文字列検索を実施するSpecificationを返却する
     *
     * @param targetColumn
     * @param targetQuery
     * @return
     */
    public static Specification<UserEntity> contain(Columns targetColumn, String targetQuery) {
        return StringUtils.isEmpty(targetQuery) ? null : new Specification<UserEntity>() {
            @Override
            public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.like(root.get(targetColumn.name()), "%" + targetQuery + "%");
            }
        };
    }
}
