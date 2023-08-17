package service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import main.Application;
import main.model.Mailing;
import main.model.Mailing.MailingType;
import main.model.PostalMovement;
import main.model.PostalMovement.Status;
import main.model.PostalOffice;
import main.repository.MailingRepo;
import main.repository.PostalMovementRepo;
import main.repository.PostalOfficeRepo;
import main.service.MailingService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "/application-test.yaml")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class MailingServiceTest {

  @Autowired
  private MailingService mailingService;

  @Autowired
  private PostalOfficeRepo postalOfficeRepo;

  @Autowired
  private MailingRepo mailingRepo;

  @Autowired
  private PostalMovementRepo postalMovementRepo;

  PostalOffice postal1;
  PostalOffice postal2;
  Mailing mailing;
  PostalMovement movement;
  PostalMovement movement1;

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
    movement.setPostalOffice(postal1);
    movement.setDate(new Date());
    movement.setStatus(Status.ARRIVED);
    postalMovementRepo.save(movement);
    movement1 = new PostalMovement();
    movement1.setId(2L);
    movement1.setMailing(mailing);
    movement1.setPostalOffice(postal1);
    movement1.setDate(new Date());
    movement1.setStatus(Status.SUBSIDED);
    postalMovementRepo.save(movement1);
  }

  @Test
  public void testCurrentStatusMailing() {
    PostalMovement actual = mailingService.currentStatusMailing(1L);
    PostalMovement expected = movement1;
    Assert.assertEquals(expected.getId(), actual.getId());
  }

  @Test
  public void testGetStatusAndHistory() {
    List<Long> actual = mailingService.getStatusAndHistory(1L).stream().map(PostalMovement::getId).toList();
    List<Long> expected = new ArrayList<>();
    expected.add(movement1.getId());
    expected.add(movement.getId());
    Assert.assertEquals(expected, actual);
  }

}
