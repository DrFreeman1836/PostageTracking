package main.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RsDto {

  protected Boolean result;

  protected String message;

  public static RsDto getInstance(String message, Boolean result) {
    RsDto rs = new RsDto();
    rs.setMessage(message);
    rs.setResult(result);
    return rs;
  }

}
