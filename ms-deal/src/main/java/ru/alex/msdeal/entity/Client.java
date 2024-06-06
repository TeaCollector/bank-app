package ru.alex.msdeal.entity;

import java.time.LocalDate;
import java.util.UUID;
import javax.persistence.*;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import ru.alex.msdeal.entity.constant.Gender;
import ru.alex.msdeal.entity.constant.MaritalStatus;


@Getter
@Setter
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Client")
@Table(name = "client", schema = "public")
public class Client {

    @Id
    @Column(name = "client_id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    private String lastName;

    private String firstName;

    private String middleName;

    @Column(name = "birth_date")
    private LocalDate birthdate;

    private String email;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;

    private Integer dependentAmount;

    @OneToOne(cascade = CascadeType.ALL)
    @Type(type = "jsonb")
    @JoinColumn(name = "passport_id")
    private Passport passport;

    @Type(type = "jsonb")
    @Column(name = "employment_id")
    private Employment employment;

    private String accountNumber;

}
