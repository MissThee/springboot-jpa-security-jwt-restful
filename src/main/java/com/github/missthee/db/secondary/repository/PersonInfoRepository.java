package com.github.missthee.db.secondary.repository;

import com.github.missthee.db.secondary.entity.PersonInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PersonInfoRepository extends JpaRepository<PersonInfo, Long> , JpaSpecificationExecutor<PersonInfo>{

}
