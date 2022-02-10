package core.wefix.lab.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;

import javax.persistence.*;
import java.io.Serializable;

import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "review")
public class Review implements Serializable {

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reviewId;

	@Basic
	@Column(name = "userId_receive")
	private Long userIdReceiveReview;

	@Basic
	@Column(name = "userId_assign")
	private Long userIdAssignReview;

	@Basic
	@Column(name = "content", length = 256)
	private String content;

	@OnDelete(action = CASCADE)
	@ManyToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "userId_receive", referencedColumnName = "id", insertable = false, updatable = false)
	private Account userIdReceiveAccountReview;

	@OnDelete(action = CASCADE)
	@ManyToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "userId_assign", referencedColumnName = "id", insertable = false, updatable = false)
	private Account userIdAssignAccountReview;
}
