package main.service;

import java.util.Date;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import main.dto.RqMailingMovement;
import main.dto.RqRegisterMailingDto;
import main.exception.UserException;
import main.model.Mailing;
import main.model.PostalMovement;
import main.model.PostalMovement.Status;
import main.model.PostalOffice;
import main.repository.MailingRepo;
import main.repository.PostalMovementRepo;
import main.repository.PostalOfficeRepo;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostalOfficeService {

  private final MailingRepo mailingRepo;

  private final PostalOfficeRepo postalOfficeRepo;

  private final PostalMovementRepo postalMovementRepo;

  private final MailingService mailingService;

  @Transactional
  public Long registrationOfMail(RqRegisterMailingDto mailingDto) {
    PostalOffice postal = postalOfficeRepo.findByIndexPostal(mailingDto.getIndex()).orElseThrow(() -> new UserException("Почтовое отделение не найдено"));
    Mailing mailing = Mailing.builder()
        .type(mailingDto.getType())
        .indexRecipient(mailingDto.getIndexRecipient())
        .addressRecipient(mailingDto.getAddressRecipient())
        .nameRecipient(mailingDto.getNameRecipient())
        .build();
    try {
      Long idMailing = mailingRepo.save(mailing).getId();
      editStatusMailing(postal, mailing, Status.ARRIVED);
      return idMailing;
    } catch (DataIntegrityViolationException ex) {
      throw new UserException("В отправлении не заполнены обязательные поля");
    }
  }

  public void departurePostage(RqMailingMovement movement) {
    PostalOffice postal = postalOfficeRepo.findByIndexPostal(movement.getIndex()).orElseThrow(() -> new UserException("Почтовое отделение не найдено"));
    Mailing mailing = mailingRepo.findById(movement.getIdMailing()).orElseThrow(() -> new UserException("Почтовое отправление не найдено"));
    editStatusMailing(postal, mailing, Status.SUBSIDED);
  }

  public void arrivalPostage(RqMailingMovement movement) {
    PostalOffice postal = postalOfficeRepo.findByIndexPostal(movement.getIndex()).orElseThrow(() -> new UserException("Почтовое отделение не найдено"));
    Mailing mailing = mailingRepo.findById(movement.getIdMailing()).orElseThrow(() -> new UserException("Почтовое отправление не найдено"));
    editStatusMailing(postal, mailing, Status.ARRIVED);
  }

  public void receiptAddressee(RqMailingMovement movement) {
    PostalOffice postal = postalOfficeRepo.findByIndexPostal(movement.getIndex()).orElseThrow(() -> new UserException("Почтовое отделение не найдено"));
    Mailing mailing = mailingRepo.findById(movement.getIdMailing()).orElseThrow(() -> new UserException("Почтовое отправление не найдено"));
    PostalMovement currentMovement = mailingService.currentStatusMailing(mailing.getId());
    if (currentMovement.getStatus().equals(Status.RECEIVED)) {
      throw new UserException("Отправление уже получено");
    } else if (!currentMovement.getStatus().equals(Status.AWAITING_RECEIPT)) {
      throw new UserException("Отправление не прибыло в пункт назначения");
    } else if (!movement.getIndex().equals(currentMovement.getPostalOffice().getIndexPostal())) {
      throw new UserException("Отправление в другом почтовом отделении");
    } else {
      editStatusMailing(postal, mailing, Status.RECEIVED);
    }
  }

  private void editStatusMailing(PostalOffice postalOffice, Mailing mailing, Status status) throws UserException {
    Optional<PostalMovement> movement = postalMovementRepo.findFirstByPostalOfficeAndMailingOrderByIdDesc(postalOffice, mailing);
    if (movement.isPresent() && movement.get().getStatus().equals(Status.RECEIVED)) {
      throw new UserException("Отправление уже получено");
    } else if (movement.isPresent() && movement.get().getStatus().equals(status)) {
      throw new UserException("Отправление уже обработано");
    } else if (movement.isPresent() && mailing.getIndexRecipient().equals(postalOffice.getIndexPostal()) && !Status.RECEIVED.equals(status)) {
      throw new UserException("Отправление ожидает получения");
    } else {
      createMovement(postalOffice, mailing, status);
    }
  }

  private void createMovement(PostalOffice postalOffice, Mailing mailing, Status status) {
    PostalMovement newMovement = new PostalMovement();
    newMovement.setMailing(mailing);
    newMovement.setPostalOffice(postalOffice);
    newMovement.setDate(new Date());
    newMovement.setStatus
        (postalOffice.getIndexPostal().equals(mailing.getIndexRecipient()) && !Status.RECEIVED.equals(status)
            ? Status.AWAITING_RECEIPT : status);// убрать сделать новое получение потои готовность и переработать проверки на более лаконичные
    postalMovementRepo.save(newMovement);
  }

}
