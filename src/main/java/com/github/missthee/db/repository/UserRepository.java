package com.github.missthee.db.repository;

import com.github.missthee.db.entity.SysUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SysUser, Long>, JpaSpecificationExecutor<SysUser> {

    @Query(value = "select t from SysUser t where t.username=:username")
    SysUser findFirstByUsernameQuery(@Param("username") String username);

    Optional<SysUser> findFirstByUsername(String username);

    //使用 @EntityGraph指定视图时，NamedEntityGraph中指定的attributeNodes会被处理为EAGER类型，其他字段保持默认设定值
//使用了分页
// 1. 使用EntityGraph，实际为内存分页，有性能问题。
//    + 会进行left join查询，减少查询次数，但即使使用了分页，也仍会查询出所有匹配数据，不会直接使用limit。因为有一/多对多关系，直接limit分页数据会不对，其保证了分页内容正确，但会查询所有数据。
//    + 查询时控制台输出警告firstResult/maxResults specified with collection fetch; applying in memory!
// 2. 不使用EntityGraph时，查询次数多，n+1问题。
//    + 关联信息为多个时，会多次使用id查询多个关联对象信息。
//实际生产环境不能依赖jpa默认的这些查询方式进行复杂查询，将查询拆分，关联对象查询设置LAZY，自行进行分步查询、拼接数据
    @EntityGraph(value = SysUser.NamedEntityGraph.Graph1, type = EntityGraph.EntityGraphType.LOAD)
    Page<SysUser> findAll(Specification<SysUser> specification, Pageable pageable);

}
