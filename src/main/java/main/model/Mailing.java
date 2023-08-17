package main.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Mailing {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "enum('LETTER', 'PACKAGE', 'PARCEL', 'POSTCARD')", nullable = false)
  private MailingType type;

  @Column(nullable = false)
  private Integer indexRecipient;

  @Column(nullable = false)
  private String addressRecipient;

  @Column(nullable = false)
  private String nameRecipient;

  @OneToMany(mappedBy = "mailing", fetch = FetchType.EAGER)
  private List<PostalMovement> postalMovementList;

  public enum MailingType {
    LETTER,
    PACKAGE,
    PARCEL,
    POSTCARD
  }

}
