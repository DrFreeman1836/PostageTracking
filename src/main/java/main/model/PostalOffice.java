package main.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "POSTAL_OFFICE")
public class PostalOffice {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private Integer indexPostal;

  @Column(nullable = false)
  private String titlePostal;

  @Column(nullable = false)
  private String addressPostal;

  @OneToMany(mappedBy = "postalOffice", fetch = FetchType.EAGER)
  private List<PostalMovement> postalMovementList;

}
