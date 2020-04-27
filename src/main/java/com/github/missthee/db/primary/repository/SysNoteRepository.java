package com.github.missthee.db.primary.repository;

import com.github.missthee.db.primary.entity.SysNote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface SysNoteRepository extends JpaRepository<SysNote, Long> , JpaSpecificationExecutor<SysNote>{
     Optional<SysNote> findFirstByIdEqualsOrderByIdDesc(Long id);
     Iterable<SysNote> findAllByIdAfterOrderByIdDesc(Long id);
     Page<SysNote> findAllByIdAfterOrderByIdDesc(Long id, Pageable pageable);
}
