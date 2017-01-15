package jp.co.fujixerox.nbd.persistence.specification;

import jp.co.fujixerox.nbd.persistence.entity.DeviceEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class DeviceSpecification {
    public enum Columns {
        id, name, macAddress
    }

    /**
     * 指定カラムに対する文字列検索を実施するSpecificationを返却する
     *
     * @param targetColumn
     * @param targetQuery
     * @return
     */
    public static Specification<DeviceEntity> contain(Columns targetColumn, String targetQuery) {
        return StringUtils.isEmpty(targetQuery) ? null : new Specification<DeviceEntity>() {
            @Override
            public Predicate toPredicate(Root<DeviceEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.like(root.get(targetColumn.name()), "%" + targetQuery + "%");
            }
        };
    }
}
