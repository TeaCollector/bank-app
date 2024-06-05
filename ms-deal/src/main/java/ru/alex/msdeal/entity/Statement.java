package ru.alex.msdeal.entity;

import java.time.Instant;
import java.util.UUID;
import javax.persistence.*;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import ru.alex.msdeal.entity.constant.ApplicationStatus;


@Getter
@Setter
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "statement", schema = "public")
@Entity
public class Statement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "statement_id")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_id")
    private Credit credit;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    private Instant creationDate;

    @Type(type = "jsonb")
    private AppliedOffer aplliedOffer;

    private Instant signDate;

    private Integer sesCode;

    @Type(type = "jsonb")
    private StatusHistory statusHistory;


}
