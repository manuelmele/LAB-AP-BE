package core.wefix.lab.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name = "meeting")
public class Meeting implements Serializable {

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long meetingId;

	@Basic
	@Column(name = "userId_worker")
	private Long userIdWorkerMeeting;

	@Basic
	@Column(name = "userId_customer")
	private Long userIdCustomerMeeting;

	@Basic
	@Column(name = "description", length = 128)
	private String descriptionMeeting;

	@Column(name = "date", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
	private LocalDateTime dateMeeting;

	@Basic
	@Column(name = "slot_time", length = 32)
	private String slotTime;

	@Basic
	@Column(name = "accepted")
	private Boolean acceptedMeeting;

	@Basic
	@Column(name = "started")
	private Boolean startedMeeting;

	@Basic
	@Column(name = "lat")
	private Double latPosition;

	@Basic
	@Column(name = "lng")
	private Double lngPosition;

	@OnDelete(action = CASCADE)
	@ManyToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "userId_worker", referencedColumnName = "id", insertable = false, updatable = false)
	private Account userIdWorkerAccountMeeting;

	@OnDelete(action = CASCADE)
	@ManyToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "userId_customer", referencedColumnName = "id", insertable = false, updatable = false)
	private Account userIdCustomerAccountMeeting;

	public Meeting(Long accountIdWorker, Long accountIdCustomer, String description, LocalDateTime date, String slot_time) {
		this.userIdWorkerMeeting = accountIdWorker;
		this.userIdCustomerMeeting = accountIdCustomer;
		this.descriptionMeeting = description;
		this.dateMeeting = date;
		this.slotTime = slot_time;
	}
}
