package main.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "POSTAL_MOVEMENT")
public class PostalMovement {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "mailing_id", nullable = false)
  private Mailing mailing;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "postal_id", nullable = false)
  private PostalOffice postalOffice;

  @Column(nullable = false)
  private Date date;

  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "enum('ARRIVED', 'SUBSIDED', 'AWAITING_RECEIPT', 'RECEIVED')", nullable = false)
  private Status status;

  public enum Status {
    ARRIVED,
    SUBSIDED,
    AWAITING_RECEIPT,
    RECEIVED
  }

}
