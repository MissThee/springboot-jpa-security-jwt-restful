package com.github.missthee.controller;

import com.github.missthee.db.entity.SysNote;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestTransaction {
    @Transactional(rollbackFor = Exception.class)
    SysNote doPersistence(EntityManager entityManager) {
        SysNote sysNote = entityManager.find(SysNote.class, 1L);
        sysNote.setParam1("设置一个值" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
        entityManager.flush();
        return sysNote;
    }
}
