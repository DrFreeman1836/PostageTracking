package main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.model.Mailing.MailingType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RqRegisterMailingDto {

  private MailingType type;

  private Integer indexRecipient;

  private String addressRecipient;

  private String nameRecipient;

  private Integer index;

}
