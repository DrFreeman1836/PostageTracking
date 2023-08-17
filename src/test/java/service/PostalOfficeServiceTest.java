package service;


import java.util.Date;
import main.Application;
import main.dto.RqMailingMovement;
import main.dto.RqRegisterMailingDto;
import main.exception.UserException;
import main.model.Mailing.MailingType;
import main.model.PostalMovement;
import main.model.PostalMovement.Status;
import main.model.PostalOffice;
import main.repository.PostalOfficeRepo;
import main.service.MailingService;
import main.service.PostalOfficeService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "/application-test.yaml")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class PostalOfficeServiceTest {

  @Autowired
  private PostalOfficeRepo postalOfficeRepo;

  private PostalOffice postal1;
  private PostalOffice postal2;

  @Autowired
  private PostalOfficeService postalOfficeService;

  @Autowired
  private MailingService mailingService;

  @Before
  public void init() {
    postal1 = new PostalOffice();
    postal1.setId(1L);
    postal1.setAddressPostal("Ковров");
    postal1.setIndexPostal(601);
    postal1.setTitlePostal("Первый");
    postalOfficeRepo.save(postal1);
    postal2 = new PostalOffice();
    postal2.setId(2L);
    postal2.setAddressPostal("Муром");
    postal2.setIndexPostal(701);
    postal2.setTitlePostal("Второй");
    postalOfficeRepo.save(postal2);
  }

  @Test
  public void aTestRegistrationOfMailFail() {
    RqRegisterMailingDto rq = new RqRegisterMailingDto();
    rq.setIndex(601);
    rq.setType(MailingType.LETTER);
    rq.setAddressRecipient("Муром");
    rq.setIndexRecipient(701);

    try {
      postalOfficeService.registrationOfMail(rq);
    } catch (UserException ex){
      Assert.assertTrue(true);
      return;
    }
    Assert.fail();
  }

  @Test
  public void bTestRegistrationOfMail() {
    RqRegisterMailingDto rq = new RqRegisterMailingDto();
    rq.setIndex(601);
    rq.setType(MailingType.LETTER);
    rq.setAddressRecipient("Муром");
    rq.setNameRecipient("Шива");
    rq.setIndexRecipient(701);

    Long actual = postalOfficeService.registrationOfMail(rq);
    Assert.assertEquals(actual, Long.valueOf(1));
  }

  @Test
  public void cTestDeparturePostage() {
    RqMailingMovement rq = new RqMailingMovement();
    rq.setIndex(601);
    rq.setIdMailing(1L);
    PostalMovement movementActual1 = mailingService.currentStatusMailing(1L);
    Assert.assertEquals(Status.ARRIVED, movementActual1.getStatus());
    postalOfficeService.departurePostage(rq);
    PostalMovement movementActual2 = mailingService.currentStatusMailing(1L);
    Assert.assertEquals(Status.SUBSIDED, movementActual2.getStatus());
  }

  @Test
  public void dTestArrivalPostage() {
    RqMailingMovement rq = new RqMailingMovement();
    rq.setIndex(701);
    rq.setIdMailing(1L);
    PostalMovement movementActual1 = mailingService.currentStatusMailing(1L);
    Assert.assertEquals(Status.SUBSIDED, movementActual1.getStatus());
    postalOfficeService.arrivalPostage(rq);
    PostalMovement movementActual2 = mailingService.currentStatusMailing(1L);
    Assert.assertEquals(Status.AWAITING_RECEIPT, movementActual2.getStatus());
    Assert.assertEquals(Integer.valueOf(701), movementActual2.getPostalOffice().getIndexPostal());
  }

  @Test
  public void eTestReceiptAddressee() {
    RqMailingMovement rq = new RqMailingMovement();
    rq.setIndex(701);
    rq.setIdMailing(1L);
    postalOfficeService.receiptAddressee(rq);
    PostalMovement movementActual2 = mailingService.currentStatusMailing(1L);
    Assert.assertEquals(Status.RECEIVED, movementActual2.getStatus());
  }

}