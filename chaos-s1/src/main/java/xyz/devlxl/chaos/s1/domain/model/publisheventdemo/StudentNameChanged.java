package xyz.devlxl.chaos.s1.domain.model.publisheventdemo;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import xyz.devlxl.chaos.support.domain.DomainEvent;
import xyz.devlxl.chaos.support.jpa.domain.AbstractJpaDomainEvent;

/**
 * @author Liu Xiaolei
 * @date 2018/08/28
 */
@AllArgsConstructor
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class StudentNameChanged extends AbstractJpaDomainEvent {
    private StudentId studentId;
    private String oriName;
    private String newName;

    @Override
    public boolean equalsExcludedOccurTime(DomainEvent other) {
        if (!(other instanceof StudentNameChanged)) {
            return false;
        } else {
            StudentNameChanged other2 = (StudentNameChanged)other;
            return Objects.equals(studentId, other2.getStudentId())
                && Objects.equals(oriName, other2.getOriName())
                && Objects.equals(newName, other2.getNewName());
        }

    }
}
