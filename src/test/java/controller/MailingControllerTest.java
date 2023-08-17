package controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import main.Application;
import main.dto.RsHistoryMailing;
import main.model.Mailing;
import main.model.Mailing.MailingType;
import main.model.PostalMovement;
import main.model.PostalMovement.Status;
import main.model.PostalOffice;
import main.repository.MailingRepo;
import main.repository.PostalMovementRepo;
import main.repository.PostalOfficeRepo;
import main.service.MailingService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
@TestPropertySource(locations = "/application-test.yaml")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class MailingControllerTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private MailingService mailingService;

  @Autowired
  private PostalOfficeRepo postalOfficeRepo;
  @Autowired
  private MailingRepo mailingRepo;
  @Autowired
  private PostalMovementRepo postalMovementRepo;
  PostalMovement movement;
  PostalMovement movement1;
  private PostalOffice postal;
  private Mailing mailing;
  @Before
  public void init() {
    postal = new PostalOffice();
    postal.setId(1L);
    postal.setAddressPostal("Ковров");
    postal.setIndexPostal(601);
    postal.setTitlePostal("Первый");
    postalOfficeRepo.save(postal);

    mailing = new Mailing();
    mailing.setId(1L);
    mailing.setType(MailingType.LETTER);
    mailing.setAddressRecipient("Муром");
    mailing.setNameRecipient("Шива");
    mailing.setIndexRecipient(701);
    mailingRepo.save(mailing);

    movement = new PostalMovement();
    movement.setId(1L);
    movement.setMailing(mailing);
    movement.setPostalOffice(postal);
    movement.setDate(new Date());
    movement.setStatus(Status.ARRIVED);
    postalMovementRepo.save(movement);
    movement1 = new PostalMovement();
    movement1.setId(2L);
    movement1.setMailing(mailing);
    movement1.setPostalOffice(postal);
    movement1.setDate(new Date());
    movement1.setStatus(Status.SUBSIDED);
    postalMovementRepo.save(movement1);
  }

  @Test
  public void testHistory() {
    ResponseEntity<RsHistoryMailing> response = restTemplate.getForEntity("/api/v1/mailing/1", RsHistoryMailing.class);
    RsHistoryMailing historyMailing = response.getBody();
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(2, historyMailing.getListMovement().size());
  }

  @Test
  public void testHistoryFail() {
    ResponseEntity<RsHistoryMailing> response = restTemplate.getForEntity("/api/v1/mailing/2", RsHistoryMailing.class);
    RsHistoryMailing historyMailing = response.getBody();
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Почтовое отправление не найдено", historyMailing.getMessage());
  }

}
