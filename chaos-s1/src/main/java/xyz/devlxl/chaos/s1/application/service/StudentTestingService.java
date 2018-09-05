package xyz.devlxl.chaos.s1.application.service;

import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.devlxl.chaos.s1.domain.model.publisheventdemo.Student;
import xyz.devlxl.chaos.s1.domain.model.publisheventdemo.StudentId;
import xyz.devlxl.chaos.s1.domain.model.publisheventdemo.StudentRepository;

/**
 * @author Liu Xiaolei
 * @date 2018/08/31
 */
@Service
public class StudentTestingService {
    @Autowired
    private StudentRepository repo;

    @Transactional
    public void changeName(UUID studentIdUuid, String newName) {
        Student student = repo.findById(new StudentId(studentIdUuid)).get();
        student.changeName(newName);
        repo.save(student);
    }

    @Transactional
    public void changeNameNotSaveExplicitly(UUID studentIdUuid, String newName) {
        Student student = repo.findById(new StudentId(studentIdUuid)).get();
        student.changeName(newName);
    }

    @Transactional
    public void changeNameFailAfterSave(UUID studentIdUuid, String newName) {
        Student student = repo.findById(new StudentId(studentIdUuid)).get();
        student.changeName(newName);
        repo.save(student);
        throw new ArrayIndexOutOfBoundsException();
    }

    @Transactional
    public void changeNameAndTwoEventDemo(UUID studentIdUuid, String newName) {
        Student student = repo.findById(new StudentId(studentIdUuid)).get();
        student.changeNameAndTwoEventDemo(newName);
        repo.save(student);
    }
}
