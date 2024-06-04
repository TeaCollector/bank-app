package ru.alex.msdeal.entity;

import java.math.BigDecimal;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.alex.msdeal.entity.constant.CreditStatus;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "credit", schema = "public")
@Entity(name = "Credit")
public class Credit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    private BigDecimal amount;

    private Integer term;

    private BigDecimal MonthlyPayment;

    private BigDecimal rate;

    private BigDecimal psk;

    @JdbcTypeCode(SqlTypes.JSON)

    private PaymentSchedule paymentSchedule;

    private Boolean insuranceEnable;

    private Boolean salaryClient;

    @Enumerated(EnumType.STRING)
    private CreditStatus creditStatus;

}
