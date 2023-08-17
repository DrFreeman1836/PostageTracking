package main.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import main.exception.UserException;
import main.model.Mailing;
import main.model.PostalMovement;
import main.repository.MailingRepo;
import main.repository.PostalMovementRepo;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailingService {

  private final PostalMovementRepo postalMovementRepo;

  private final MailingRepo mailingRepo;

  public PostalMovement currentStatusMailing(Long idMailing) {
    Mailing mailing = mailingRepo.findById(idMailing).orElseThrow(() -> new UserException("Почтовое отправление не найдено"));
    return postalMovementRepo.findFirstByMailingOrderByIdDesc(mailing)
        .orElseThrow(() -> new UserException("Запись об отправлении не найдена"));
  }

  public List<PostalMovement> getStatusAndHistory(Long idMailing) {
    Mailing mailing = mailingRepo.findById(idMailing).orElseThrow(() -> new UserException("Почтовое отправление не найдено"));
    return postalMovementRepo.findAllByMailingOrderByIdDesc(mailing);
  }

}
