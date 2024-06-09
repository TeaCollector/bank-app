package ru.alex.msdeal.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
import ru.alex.msdeal.entity.constant.CreditStatus;


@Getter
@Setter
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "credit", schema = "public")
@Entity(name = "Credit")
public class Credit {

    @Id
    @Column(name = "credit_id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    private BigDecimal amount;

    private Integer term;

    private BigDecimal monthlyPayment;

    private BigDecimal rate;

    private BigDecimal psk;

    @OneToOne(mappedBy = "credit", fetch = FetchType.LAZY)
    private Statement statement;

    @Type(type = "jsonb")
    private List<PaymentSchedule> paymentSchedule = new ArrayList<>();

    private Boolean insuranceEnabled;

    private Boolean salaryClient;

    @Enumerated(EnumType.STRING)
    private CreditStatus creditStatus;

}
