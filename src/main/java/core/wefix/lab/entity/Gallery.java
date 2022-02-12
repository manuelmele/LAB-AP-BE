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
@Table(name = "gallery")
public class Gallery implements Serializable {

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long galleryId;

	@Basic
	@Column(name = "user_id")
	private Long userId;

	@Lob
	@Column(name = "image")
	private byte[] galleryImage;

	@Basic
	@Column(name = "price", columnDefinition="Decimal(10,2)")
	private Double price;

	@Basic
	@Column(name = "description", length = 256)
	private String description;

	@Basic
	@Column(name = "deleted")
	private Boolean deletedGallery;

	public Gallery(Long userId, byte[] galleryImage, Double price, String description, String title) {
		this.userId = userId;
		this.galleryImage = galleryImage;
		this.price = price;
		this.description = description;
		this.title = title;
	}

	@Basic
	@Column(name = "title", length = 64)
	private String title;


	@OnDelete(action = CASCADE)
	@ManyToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
	private Account userIdGallery;
}
