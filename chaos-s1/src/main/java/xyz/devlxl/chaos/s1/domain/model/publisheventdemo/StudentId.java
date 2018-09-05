package xyz.devlxl.chaos.s1.domain.model.publisheventdemo;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Embeddable;

import org.hibernate.annotations.Immutable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Liu Xiaolei
 * @date 2018/08/28
 */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@EqualsAndHashCode
@Immutable
@Embeddable
public class StudentId implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID uuid;

    public StudentId(StudentId aStudentId) {
        super();
        this.uuid = aStudentId.getUuid();
    }
}
