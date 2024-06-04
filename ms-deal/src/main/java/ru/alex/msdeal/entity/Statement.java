package ru.alex.msdeal.entity;

import java.time.Instant;
import java.util.UUID;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.alex.msdeal.entity.constant.ApplicationStatus;


@Getter
@Setter
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
    @JoinColumn(name = "client_id")
    private Credit credit;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    private Instant creationDate;

    @JdbcTypeCode(SqlTypes.JSON)
    private AppliedOffer aplliedOffer;

    private Instant signDate;

    private Integer sesCode;

    @JdbcTypeCode(SqlTypes.JSON)
    private StatusHistory statusHistory;


}
