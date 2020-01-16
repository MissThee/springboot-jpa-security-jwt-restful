package com.github.missthee.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.missthee.db.entity.SysNote;
import com.github.missthee.db.repository.NoteRepository;
import com.github.missthee.tool.res.Res;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class NoteController {
    private final NoteRepository noteRepository;

    public NoteController(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @PostMapping(value = "/note1")
    public Res<SysNote> note1() {
        Optional<SysNote> noteOp = noteRepository.findById(1L);
        //noteRepository.getOne(1L);返回的是实体类的一个代理对象，会携带额外的属性，如hibernateLazyInitializer
        return Res.success(noteOp.orElseGet(SysNote::new));

    }

    @PostMapping(value = "/note2")
    @JsonView(SysNote.first2Param.class)
    public Res<SysNote> note2() {
        Optional<SysNote> noteOp = noteRepository.findById(1L);
        return Res.success(noteOp.orElseGet(SysNote::new));
    }

    @PostMapping(value = "/note3")
    @JsonView(SysNote.first4Param.class)
    public Res<SysNote> note3() {
        Optional<SysNote> noteOp = noteRepository.findById(1L);
        return Res.success(noteOp.orElseGet(SysNote::new));
    }

}
