package main.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import main.model.PostalMovement;
import main.model.PostalMovement.Status;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RsHistoryMailing extends RsDto {

  private Status status;
  private Date date;
  private String address;

  private List<RsHistoryMailing> listMovement;

  public static RsHistoryMailing getInstance(String message, Boolean result) {
    RsHistoryMailing rs = new RsHistoryMailing();
    rs.setMessage(message);
    rs.setResult(result);
    return rs;
  }

  public static RsHistoryMailing getInstance(List<PostalMovement> postalMovements, Boolean result) {
    RsHistoryMailing rs = new RsHistoryMailing();
    List<RsHistoryMailing> rsList = postalMovements.stream().map(m -> {
      RsHistoryMailing h = new RsHistoryMailing();
      h.setAddress(m.getPostalOffice().getAddressPostal());
      h.setDate(m.getDate());
      h.setStatus(m.getStatus());
      return h;
    }).toList();
    rs.setStatus(rsList.get(0).getStatus());
    rs.setDate(rsList.get(0).getDate());
    rs.setAddress(rsList.get(0).getAddress());
    rs.setListMovement(rsList);
    rs.setResult(result);
    return rs;
  }

}
