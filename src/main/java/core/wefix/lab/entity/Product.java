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
@Table(name = "product")
public class Product implements Serializable {

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productId;

	@Basic
	@Column(name = "user_id")
	private Long userId;

	@Lob
	@Column(name = "image")
	private byte[] productImage;

	@Basic
	@Column(name = "price", columnDefinition="Decimal(10,2)")
	private Double price;

	@Basic
	@Column(name = "description", length = 256)
	private String description;

	@Basic
	@Column(name = "deleted")
	private Boolean deletedProduct;

	public Product(Long userId, byte[] productImage, Double price, String description, String title) {
		this.userId = userId;
		this.productImage = productImage;
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
	private Account userIdProduct;
}
