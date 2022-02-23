package core.wefix.lab.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import core.wefix.lab.utils.object.staticvalues.CurrencyPayPal;
import lombok.*;
import org.hibernate.annotations.OnDelete;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "payment")
public class PaymentInfo implements Serializable {

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long paymentId;

	@Basic
	@Column(name = "account_id")
	private Long payerId;

	@Column(name = "date", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss")
	private LocalDateTime paymentDate;

	@Column(name = "deadline", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss")
	private LocalDateTime deadline;

	@Basic
	@Column(name = "price", columnDefinition="Decimal(10,2)")
	private Double price;

	@Basic
	@Column(name = "currency", length = 10)
	@Enumerated(EnumType.STRING)
	private CurrencyPayPal currency;

	@Basic
	@Column(name = "paymentId", length = 64)
	private String paypalPaymentId;

	@Basic
	@Column(name = "payerId", length = 64)
	private String paypalPayerId;

	@Basic
	@Column(name = "is_valid")
	private Boolean isValid;

	@OnDelete(action = CASCADE)
	@ManyToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "account_id", referencedColumnName = "id", insertable = false, updatable = false)
	private Account payerIdAccount;

	public PaymentInfo(LocalDateTime paymentDate, LocalDateTime deadline, Double price, CurrencyPayPal currency, String paypalPaymentId, String paypalPayerId) {
		this.paymentDate = paymentDate;
		this.deadline = deadline;
		this.price = price;
		this.currency = currency;
		this.paypalPaymentId = paypalPaymentId;
		this.paypalPayerId = paypalPayerId;
		isValid = false;
	}
}
