package com.github.missthee.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.missthee.db.entity.SysNote;
import com.github.missthee.db.repository.SysNoteRepository;
import com.github.missthee.service.intef.SysNoteService;
import com.github.missthee.tool.res.Res;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@RestController
public class NoteController {
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    @PersistenceContext
    private EntityManager entityManager;
    private final SysNoteService sysNoteService;
    private final SysNoteRepository sysNoteRepository;

    public NoteController(SysNoteRepository sysNoteRepository, SysNoteService sysNoteService) {
        this.sysNoteRepository = sysNoteRepository;
        this.sysNoteService = sysNoteService;
    }

    @RequestMapping(value = "/note1")
    public Res<SysNote> note1() {
        Optional<SysNote> noteOp = sysNoteRepository.findById(1L);
        //noteRepository.getOne(1L);返回的是实体类的一个代理对象，会携带额外的属性，如hibernateLazyInitializer
        return Res.success(noteOp.orElseGet(SysNote::new));

    }

    @RequestMapping(value = "/note2")
    @JsonView(SysNote.first2Param.class)
    public Res<SysNote> note2() {
        Optional<SysNote> noteOp = sysNoteRepository.findById(1L);
        return Res.success(noteOp.orElseGet(SysNote::new));
    }

    @RequestMapping(value = "/note3")
    @JsonView(SysNote.first4Param.class)
    public Res<SysNote> note3() {
        Optional<SysNote> noteOp = sysNoteRepository.findById(1L);
        return Res.success(noteOp.orElseGet(SysNote::new));
    }

    @RequestMapping(value = "/note4")
//   @Transactional(rollbackOn = Exception.class)
    public Res<SysNote> note4() {

        //使用spring管理的共享entityManager，不能使用entityManager.getTransaction()相关方法管理事务，必须使用@Transactional注解管理
//       doPersistence(); //事务异常。除非启用本方法的@Transactional注解
//       new TestTransaction().doPersistence(entityManager); //事务异常。除非启用本方法的@Transactional注解
//       sysNoteService.testTransactionFailure(); //事务异常。除非启用本方法的@Transactional注解。testTransactionFailure()方法不需要加注解
        sysNoteService.testTransactionSuccess();
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return Res.success();
    }

    @Transactional(rollbackOn = Exception.class)
    SysNote doPersistence() {
        SysNote sysNote = entityManager.find(SysNote.class, 1L);
        sysNote.setParam1("设置一个值" +  new SimpleDateFormat("HH:mm:ss").format(new Date()));
        entityManager.flush();
        return sysNote;
    }
}
