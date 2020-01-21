package com.github.missthee.service.imp;

import com.github.missthee.db.entity.SysNote;
import com.github.missthee.service.intef.SysNoteService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class SysNoteServiceImp implements SysNoteService {
    @PersistenceContext
    private EntityManager em;

    @Override
    public void testTransactionFailure() {
         doTransaction();
    }

    @Transactional(rollbackOn = Exception.class)
    SysNote doTransaction() {
        SysNote sysNote = em.find(SysNote.class, 1L);
        sysNote.setParam2("字段2设置" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
        em.flush();
        return sysNote;
    }


    @Override
    @Transactional(rollbackOn = Exception.class)
    public void testTransactionSuccess() {
        SysNote sysNote = doTrans();
        SysNote sysNote1 = doTrans1();
    }

    SysNote doTrans() {
        SysNote sysNote = em.find(SysNote.class, 1L);//sysNote为[托管]状态
        sysNote.setParam1("字段1设置" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
        em.refresh(sysNote);//转为[托管]态，从数据库现事务对应的快照中获取数据，刷新sysNote。可理解为此状态下的对象，做修改会产生sql语句，等着flush或commit发送给数据库
        sysNote.setParam2("字段2设置" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
        sysNote.setParam3(null);
        sysNote.setParam4(null);
        em.flush();//转为[持久]、[托管]态，但现在处于数据库事务中，存储在数据库现事务对应的快照中（取决于数据库事务隔离级别），不会持久到数据库中，事务进行commit后生效。可将flush理解为立即发送sql语句执行
        sysNote.setParam5("字段5设置" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
//        em.clear();//全部转为[游离]状态
        em.detach(sysNote);//转为[游离]状态
        sysNote.setParam4("字段4设置" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
//      em.persist(sysNote);//对[游离]态使用persist会抛出异常
        em.merge(sysNote);//[游离]转[托管]状态，需要使用merge方法，先由数据库查询对象内容，再用修改的现有对象的值进行覆盖
        em.flush();
        return sysNote;
    }
    SysNote doTrans1() {
        SysNote sysNote = new SysNote();
        em.persist(sysNote);//sysNote为[托管]状态，此时sysNote被id生成器分配id
        em.flush();//转为[持久]、[托管]态，存储进数据库现事务对应的快照中
        em.refresh(sysNote);//转为[托管]态，使用数据库现事务对应的快照，更新sysNote内容。此做法可将数据库新行默认值获取到（前提是保证实体类使用@DynamicInsert，让插入值不被null覆盖）
        sysNote.setParam2("字段2设置" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
        sysNote.setParam4(null);
        em.flush();//flush将sysNote转为[持久]态，但现在处于数据库事务中，不会持久到数据库中，事务进行commit后生效
        sysNote.setParam5("字段5设置" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
        em.clear();//sysNote将为[游离]状态
        sysNote.setParam4("字段4设置" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
//        em.persist(sysNote);//[游离]态使用persist会抛出异常
        em.merge(sysNote);//[游离]态再次进入[托管]状态，需要使用merge方法，修改内容会被覆盖

        return sysNote;
    }
}

