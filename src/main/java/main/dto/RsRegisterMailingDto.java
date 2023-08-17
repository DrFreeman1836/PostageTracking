package main.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RsRegisterMailingDto extends RsDto {

  private Long idMailing;

  public static RsRegisterMailingDto getInstance(Long idMailing, String message, Boolean result) {
    RsRegisterMailingDto rs = new RsRegisterMailingDto();
    rs.setMessage(message);
    rs.setResult(result);
    rs.setIdMailing(idMailing);
    return rs;
  }

  public static RsRegisterMailingDto getInstance(String message, Boolean result) {
    RsRegisterMailingDto rs = new RsRegisterMailingDto();
    rs.setMessage(message);
    rs.setResult(result);
    return rs;
  }

}
