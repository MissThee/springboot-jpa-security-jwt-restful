package com.github.missthee.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.missthee.db.primary.entity.SysNote;
import com.github.missthee.db.primary.repository.SysNoteRepository;
import com.github.missthee.service.intef.SysNoteService;
import com.github.missthee.tool.res.Res;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.*;
import java.util.Optional;

@RestController()
@RequestMapping("/note")
public class NoteController {
    private final SysNoteService sysNoteService;
    private final SysNoteRepository sysNoteRepository;

    public NoteController(SysNoteRepository sysNoteRepository, SysNoteService sysNoteService) {
        this.sysNoteRepository = sysNoteRepository;
        this.sysNoteService = sysNoteService;
    }

    @GetMapping(value = "/test")
    public Res<SysNote> note1() {
        Optional<SysNote> noteOp = sysNoteRepository.findById(1L);
        //noteRepository.getOne(1L);返回的是实体类的一个代理对象，会携带额外的属性，如hibernateLazyInitializer
        return Res.success(noteOp.orElseGet(SysNote::new));

    }

    @GetMapping(value = "/jv1")
    @JsonView(SysNote.first2Param.class)
    public Res<SysNote> jv1() {
        Optional<SysNote> noteOp = sysNoteRepository.findById(1L);
        return Res.success(noteOp.orElseGet(SysNote::new));
    }

    @GetMapping(value = "/jv2")
    @JsonView(SysNote.first4Param.class)
    public Res<SysNote> jv2() {
        Optional<SysNote> noteOp = sysNoteRepository.findById(1L);
        return Res.success(noteOp.orElseGet(SysNote::new));
    }

    @GetMapping(value = "/tranAnno")
    //@Transactional(rollbackOn = Exception.class,value="primaryTransactionManager")//可以在controller使用事务注解
    public Res tranAnno() throws Exception {
        sysNoteService.testTranAnnoSuccess();
        return Res.success();
    }

    @GetMapping(value = "/tranAnno1")
    public Res tranAnno1() throws Exception {
        sysNoteService.testTranAnnoFailure();//抛出异常，解使用在非spring管理的方法或对象上，不能正确开启事务
        return Res.success();
    }

    @GetMapping(value = "/tranObj")
    public Res tranObj() {
        sysNoteService.testTranObjSuccess();
        return Res.success();
    }

    @GetMapping(value = "/testEM")
    public Res testEM() {
        sysNoteService.testEM();
        return Res.success();
    }

    @GetMapping(value = "/complex")
    public Res complex() {
        Optional<SysNote> oneOp = sysNoteRepository.findFirstByIdEqualsOrderByIdDesc(1L);
        Iterable<SysNote> allOp = sysNoteRepository.findAllByIdAfterOrderByIdDesc(1L);
        //分页，page页号0开始，size每页条数1开始
        Page<SysNote> pageOp = sysNoteRepository.findAllByIdAfterOrderByIdDesc(1L, PageRequest.of(0, 1));
        return Res.success();
    }

}
