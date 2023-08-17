package main.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import main.dto.RqMailingMovement;
import main.dto.RqRegisterMailingDto;
import main.dto.RsDto;
import main.dto.RsRegisterMailingDto;
import main.exception.UserException;
import main.service.PostalOfficeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/postal")
@RequiredArgsConstructor
public class PostalController {

  private final PostalOfficeService postalOfficeService;

  @Operation(summary = "Регистрация почтового отправления")
  @PostMapping
  public ResponseEntity<RsRegisterMailingDto> registrationOfMail(@RequestBody RqRegisterMailingDto mailing) {
    try {
      Long id = postalOfficeService.registrationOfMail(mailing);
      return ResponseEntity.ok(RsRegisterMailingDto.getInstance(id, "Отправление принято", true));
    } catch (UserException ex) {
      return ResponseEntity.status(400).body(RsRegisterMailingDto.getInstance(ex.getMessage(), false));
    } catch (Exception ex) {
      return ResponseEntity.status(500).body(RsRegisterMailingDto.getInstance("Ошибка сервера", false));
    }
  }

  @Operation(summary = "Прибыите в промежуточное отделение")
  @PutMapping("/arrival")
  public ResponseEntity<RsDto> arrivalPostage(@RequestBody RqMailingMovement movement) {
    try {
      postalOfficeService.arrivalPostage(movement);
      return ResponseEntity.ok(RsDto.getInstance("Прибытие зарегистрировано", true));
    } catch (UserException ex) {
      return ResponseEntity.status(500).body(RsDto.getInstance(ex.getMessage(), false));
    } catch (Exception ex) {
      return ResponseEntity.status(500).body(RsDto.getInstance("Ошибка сервера", false));
    }
  }

  @Operation(summary = "Убытие из отделения")
  @PutMapping("/depart")
  public ResponseEntity<RsDto> departurePostage(@RequestBody RqMailingMovement movement) {
    try {
      postalOfficeService.departurePostage(movement);
      return ResponseEntity.ok(RsDto.getInstance("Убытие зарегистрировано", true));
    } catch (UserException ex) {
      return ResponseEntity.status(500).body(RsDto.getInstance(ex.getMessage(), false));
    } catch (Exception ex) {
      return ResponseEntity.status(500).body(RsDto.getInstance("Ошибка сервера", false));
    }
  }

  @Operation(summary = "Получение адресатом")
  @PutMapping
  public ResponseEntity<RsDto> receiptAddressee(@RequestBody RqMailingMovement movement) {
    try {
      postalOfficeService.receiptAddressee(movement);
      return ResponseEntity.ok(RsDto.getInstance("Получено", true));
    } catch (UserException ex) {
      return ResponseEntity.status(500).body(RsDto.getInstance(ex.getMessage(), false));
    } catch (Exception ex) {
      return ResponseEntity.status(500).body(RsDto.getInstance("Ошибка сервера", false));
    }
  }

}
