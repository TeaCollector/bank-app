package ru.alex.msdeal.entity;

import java.time.LocalDate;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;


@Getter
@Setter
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class Passport {

    @Id
    @Column(name = "passport_id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;
    private String series;
    private String number;
    private String issueBranch;
    private LocalDate issueDate;

}
