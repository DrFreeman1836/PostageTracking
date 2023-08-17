package main.controller;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import main.dto.RsHistoryMailing;
import main.exception.UserException;
import main.model.PostalMovement;
import main.service.MailingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/mailing")
@RequiredArgsConstructor
public class MailingController {

  private final MailingService mailingService;

  @Operation(summary = "Просмотр статуса и истории отправления")
  @GetMapping("/{id}")
  public ResponseEntity<RsHistoryMailing> getStatusAndHistory(@PathVariable Long id) {
    try {
      List<PostalMovement> movementList = mailingService.getStatusAndHistory(id);
      return ResponseEntity.ok(RsHistoryMailing.getInstance(movementList, true));
    } catch (UserException ex) {
      return ResponseEntity.status(400).body(RsHistoryMailing.getInstance(ex.getMessage(), false));
    } catch (Exception ex) {
      return ResponseEntity.status(500).body(RsHistoryMailing.getInstance("Ошибка сервера", false));
    }
  }

}
