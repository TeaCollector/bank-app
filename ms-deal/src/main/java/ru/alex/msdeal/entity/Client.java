package ru.alex.msdeal.entity;

import java.util.Date;
import java.util.UUID;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.alex.msdeal.entity.constant.Gender;
import ru.alex.msdeal.entity.constant.MaritalStatus;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Client")
@Table(name = "client", schema = "public")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private UUID id;

    private String lastName;

    private String firstName;

    private String middleName;

    private Date birthDate;

    private String email;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;

    private Integer dependentAmount;

    @JdbcTypeCode(SqlTypes.JSON)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "passport_id")
    private Passport passport;

    @JdbcTypeCode(SqlTypes.JSON)
    @OneToOne
    @JoinColumn(name = "employment_id")
    private Employment employment;
    private String accountNumber;

}
