package com.github.missthee.db.repository;

import com.github.missthee.db.entity.SysNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SysNoteRepository extends JpaRepository<SysNote, Long> , JpaSpecificationExecutor<SysNote>{

}
