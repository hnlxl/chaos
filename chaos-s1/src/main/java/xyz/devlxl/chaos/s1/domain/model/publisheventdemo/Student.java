package xyz.devlxl.chaos.s1.domain.model.publisheventdemo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import xyz.devlxl.chaos.support.domain.AbstractAggregateRoot;

/**
 * @author Liu Xiaolei
 * @date 2018/08/28
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Entity
public class Student extends AbstractAggregateRoot<Student> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private StudentId id;

    private String name;

    public void changeName(String newName) {
        String oriName = this.getName();
        this.setName(newName);
        registerEvent(new StudentNameChanged(id, oriName, newName));
    }

    public void changeNameAndTwoEventDemo(String newName) {
        String oriName = this.getName();
        this.setName(newName);
        registerEvent(new StudentNameChanged(id, oriName, newName));
        registerEvent(new StudentNamerRereshed(id, newName));
    }
}
