package xyz.devlxl.chaos.s1.domain.model;

import org.springframework.data.repository.CrudRepository;

/**
 * 示例Redis缓存模型的存储库
 * <p>
 * 有一个对调用方透明的处理：查询数据时，当没有读取到数据时，从外部服务（目前直接从数据库）拉取数据，并且更新自身存储库。通过切面去提供透明处理。
 * <strong>注意：目前只对CrudRepository的标准方法进行了透明处理，没有为自定义方法做透明处理。</strong>
 * 
 * @author Liu Xiaolei
 * @date 2018/07/23
 */
public interface ExampleCacheRepository extends CrudRepository<ExampleCache, String> {

}
