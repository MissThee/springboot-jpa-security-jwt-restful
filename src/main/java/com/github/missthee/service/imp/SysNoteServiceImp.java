package com.github.missthee.service.imp;

import com.github.missthee.db.primary.entity.SysNote;
import com.github.missthee.db.primary.repository.SysNoteRepository;
import com.github.missthee.service.intef.SysNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class SysNoteServiceImp implements SysNoteService {
//@PersistenceContext
//private EntityManager entityManager;
    private final EntityManager entityManager;
    private final EntityManagerFactory entityManagerFactory;

    private final SysNoteRepository sysNoteRepository;
    public SysNoteServiceImp(@Qualifier("primaryEntityManager") EntityManager entityManager, SysNoteRepository sysNoteRepository,@Qualifier("primaryEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        this.entityManager = entityManager;
        this.sysNoteRepository = sysNoteRepository;
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void testTranAnnoFailure() {
        doTransFailure();
    }

    @Transactional(rollbackFor = Exception.class,value="primaryTransactionManager")
    SysNote doTransFailure() {
        SysNote sysNote = entityManager.find(SysNote.class, 1L);
        sysNote.setParam2("字段2设置" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
        entityManager.flush();
        return sysNote;
    }


    @Override
    @Transactional(rollbackFor = Exception.class,value="primaryTransactionManager")
    public void testTranAnnoSuccess()  {
        sysNoteRepository.save(new SysNote().setParam1("123123"));

    }

    @Override
    @Transactional(rollbackFor = Exception.class,value="primaryTransactionManager")
    public void testEM()  {
       SysNote sysNote = doTrans();
       SysNote sysNote1 = doTrans1();
    }

    @Override
    public void testTranObjSuccess() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            entityManager.persist(new SysNote().setParam1("应该看不到此信息"));
            sysNoteRepository.save(new SysNote().setParam1("应该看到此信息"));
            System.out.println("抛出异常！！");
            throw new Exception();
        } catch (Exception e) {
            //此处回滚仅对相应的EntityManager执行的语句有效
            transaction.rollback();
        }
        if (transaction.isActive()) {
            transaction.commit();
        }
    }

    SysNote doTrans() {
//        EntityTransaction transaction = entityManager.getTransaction();
//        transaction.begin();
        SysNote sysNote = entityManager.find(SysNote.class, 1L);//sysNote为[托管]状态
        sysNote.setParam1("字段1设置" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
        entityManager.refresh(sysNote);//转为[托管]态，从数据库现事务对应的快照中获取数据，刷新sysNote。可理解为此状态下的对象，做修改会产生sql语句，等着flush或commit发送给数据库
        sysNote.setParam2("字段2设置" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
        sysNote.setParam3(null);
        sysNote.setParam4(null);
        entityManager.flush();//转为[持久]、[托管]态，但现在处于数据库事务中，存储在数据库现事务对应的快照中（取决于数据库事务隔离级别），不会持久到数据库中，事务进行commit后生效。可将flush理解为立即发送sql语句执行
        sysNote.setParam5("字段5设置" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
//        em.clear();//全部转为[游离]状态
        entityManager.detach(sysNote);//转为[游离]状态
        sysNote.setParam4("字段4设置" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
//      em.persist(sysNote);//对[游离]态使用persist会抛出异常
        entityManager.merge(sysNote);//[游离]转[托管]状态，需要使用merge方法，先由数据库查询对象内容，再用修改的现有对象的值进行覆盖
        entityManager.flush();
//        transaction.commit();
        return sysNote;
    }

    SysNote doTrans1() {
        SysNote sysNote = new SysNote();
        entityManager.persist(sysNote);//sysNote为[托管]状态，此时sysNote被id生成器分配id
        entityManager.flush();//转为[持久]、[托管]态，存储进数据库现事务对应的快照中
        entityManager.refresh(sysNote);//转为[托管]态，使用数据库现事务对应的快照，更新sysNote内容。此做法可将数据库新行默认值获取到（前提是保证实体类使用@DynamicInsert，让插入值不被null覆盖）
        sysNote.setParam2("字段2设置" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
        sysNote.setParam4(null);
        entityManager.flush();//flush将sysNote转为[持久]态，但现在处于数据库事务中，不会持久到数据库中，事务进行commit后生效
        sysNote.setParam5("字段5设置" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
        entityManager.clear();//sysNote将为[游离]状态
        sysNote.setParam4("字段4设置" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
//        em.persist(sysNote);//[游离]态使用persist会抛出异常
        entityManager.merge(sysNote);//[游离]态再次进入[托管]状态，需要使用merge方法，修改内容会被覆盖

        return sysNote;
    }
}

