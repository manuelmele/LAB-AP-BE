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
	@Column(name = "userId_receive")
	private Long userIdReceiveMeeting;

	@Basic
	@Column(name = "userId_assign")
	private Long userIdAssignMeeting;

	@Column(name = "date", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
	private LocalDateTime dateMeeting;

	@Basic
	@Column(name = "slot_time", columnDefinition="Decimal(2,2)")
	private Double slotTime;

	@Basic
	@Column(name = "accepted")
	private Boolean acceptedMeeting;

	@OnDelete(action = CASCADE)
	@ManyToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "userId_receive", referencedColumnName = "id", insertable = false, updatable = false)
	private Account userIdReceiveAccountMeeting;

	@OnDelete(action = CASCADE)
	@ManyToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "userId_assign", referencedColumnName = "id", insertable = false, updatable = false)
	private Account userIdAssignAccountMeeting;

}
