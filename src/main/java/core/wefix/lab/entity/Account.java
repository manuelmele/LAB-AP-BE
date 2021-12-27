package core.wefix.lab.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import core.wefix.lab.utils.object.staticvalues.Role;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Account implements Serializable {

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long accountId;

	@Basic
	@Column(name = "first_name", nullable = false, unique = true, length = 64)
	private String firstName;

	@Basic
	@Column(name = "second_name", nullable = false, unique = true, length = 64)
	private String secondName;

	@Basic
	@Column(name = "email", nullable = false, unique = true, length = 64)
	private String email;

	@Basic
	@Hidden
	@JsonIgnore
	@Column(name = "user_password", nullable = false, length = 64)
	private String userPassword;

	@Basic
	@Column(name = "user_role", nullable = false)
	@Enumerated(EnumType.STRING)
	private Role userRole;

	@Basic
	@Column(name = "bio", length = 128)
	private String bio;

	@Lob
	@Column(name = "photo_profile")
	private byte[] photoProfile;

	@Column(name = "date_reset", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss")
	private LocalDateTime dateReset;

	@Basic
	@Column(name = "reset_code", length = 64)
	private String resetCode;

	@Basic
	@Column(name = "p_iva", length = 11)
	private String pIva;

	@Basic
	@Column(name = "identity_card_number", length = 7)
	private String identityCardNumber;

	public Account(String firstName, String secondName, String email, String userPassword, Role userRole) {
		this.firstName = firstName;
		this.secondName = secondName;
		this.email = email;
		this.userPassword = userPassword;
		this.userRole = userRole;
	}

	public Account(String firstName, String secondName, String email, String userPassword, Role userRole, String bio, byte[] photoProfile, LocalDateTime dateReset, String resetCode, String pIva, String identityCardNumber) {
		this.firstName = firstName;
		this.secondName = secondName;
		this.email = email;
		this.userPassword = userPassword;
		this.userRole = userRole;
		this.bio = bio;
		this.photoProfile = photoProfile;
		this.dateReset = dateReset;
		this.resetCode = resetCode;
		this.pIva = pIva;
		this.identityCardNumber = identityCardNumber;
	}

}
