package xyz.devlxl.chaos.s1.infrastructure.repos;

import java.util.Optional;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import xyz.devlxl.chaos.s1.domain.model.ExampleCache;
import xyz.devlxl.chaos.s1.domain.model.ExampleCacheRepository;

/**
 * EquipmentCacheRepository切面，为该存储库接口提供透明服务。
 * <p>
 * <strong>目前为findById方法做了透明处理，其他方法还没做</strong>
 * <p>
 * 待完善的点：1、自动拉取数据后的更新缓存操作应该通过领域事件发布，改切面无须依赖存储库本身；2、该切面可能应该放在基础设施区域；3、拉取数据解耦
 * 
 * @author Liu Xiaolei
 * @date 2018/07/23
 */
@Slf4j
@Aspect
@Component
public class ExampleCacheRepositoryAspect {
    private static final String SQL_BY_ID = ""
        + " SELECT "
        + "   t.key AS key, "
        + "   t.other_id AS other_id, "
        + "   ..."
        + " FROM table_111 t"
        + " WHERE t.key = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ExampleCacheRepository exampleCacheRepository;

    @Around("execution(public * xyz.devlxl.chaos.s1.domain.model.ExampleCacheRepository.findById(..))"
        + "&& args(key)")
    public Optional<ExampleCache> aroundFindById(ProceedingJoinPoint pjp, String key) throws Throwable {
        @SuppressWarnings("unchecked")
        Optional<ExampleCache> retVal = (Optional<ExampleCache>)pjp.proceed();

        if (!retVal.isPresent()) {
            ExampleByMysql voByMysql = null;
            try {
                voByMysql = jdbcTemplate.queryForObject(SQL_BY_ID,
                    BeanPropertyRowMapper.newInstance(ExampleByMysql.class),
                    key);
            } catch (Exception e) {
                // 任何异常都认为是没有找到数据，但如果不是"列表没有数据"的异常则按照警告级别记录日志
                if (e instanceof EmptyResultDataAccessException) {
                    log.debug("", e);
                } else {
                    log.warn("", e);
                }
            }
            if (voByMysql != null) {
                ExampleCache cache = new ExampleCache();
                cache.setKey(voByMysql.getKey());
                // ...
                retVal = Optional.of(cache);
                exampleCacheRepository.save(cache);
            }
        }

        return retVal;
    }

    @Setter
    @Getter
    public static class ExampleByMysql {
        private String key;

        private Long field1;

        private Boolean field2;
    }
}
