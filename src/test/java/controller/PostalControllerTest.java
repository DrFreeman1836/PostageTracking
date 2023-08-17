package controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.Application;
import main.dto.RqMailingMovement;
import main.dto.RqRegisterMailingDto;
import main.dto.RsRegisterMailingDto;
import main.model.Mailing.MailingType;
import main.service.PostalOfficeService;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class PostalControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PostalOfficeService postalOfficeService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void testRegistrationCall() throws Exception {
    RqRegisterMailingDto rq = new RqRegisterMailingDto();
    rq.setIndex(601);
    rq.setType(MailingType.LETTER);
    rq.setAddressRecipient("Муром");
    rq.setIndexRecipient(701);
    rq.setNameRecipient("Шина");
    mockMvc.perform(post("/api/v1/postal").content(objectMapper.writeValueAsBytes(rq))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(ResultMatcher.matchAll());
    ArgumentCaptor<RqRegisterMailingDto> userCaptor = ArgumentCaptor.forClass(RqRegisterMailingDto.class);
    verify(postalOfficeService, times(1)).registrationOfMail(userCaptor.capture());
  }

  @Test
  public void testArrivalCall() throws Exception {
    RqMailingMovement rq = new RqMailingMovement();
    rq.setIndex(601);
    rq.setIdMailing(1L);
    mockMvc.perform(put("/api/v1/postal/arrival").content(objectMapper.writeValueAsBytes(rq))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(ResultMatcher.matchAll());
    ArgumentCaptor<RqMailingMovement> userCaptor = ArgumentCaptor.forClass(RqMailingMovement.class);
    verify(postalOfficeService, times(1)).arrivalPostage(userCaptor.capture());
  }

  @Test
  public void testDepartCall() throws Exception {
    RqMailingMovement rq = new RqMailingMovement();
    rq.setIndex(601);
    rq.setIdMailing(1L);
    mockMvc.perform(put("/api/v1/postal/depart").content(objectMapper.writeValueAsBytes(rq))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(ResultMatcher.matchAll());
    ArgumentCaptor<RqMailingMovement> userCaptor = ArgumentCaptor.forClass(RqMailingMovement.class);
    verify(postalOfficeService, times(1)).departurePostage(userCaptor.capture());
  }

  @Test
  public void testReceiptCall() throws Exception {
    RqMailingMovement rq = new RqMailingMovement();
    rq.setIndex(601);
    rq.setIdMailing(1L);
    mockMvc.perform(put("/api/v1/postal").content(objectMapper.writeValueAsBytes(rq))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(ResultMatcher.matchAll());
    ArgumentCaptor<RqMailingMovement> userCaptor = ArgumentCaptor.forClass(RqMailingMovement.class);
    verify(postalOfficeService, times(1)).receiptAddressee(userCaptor.capture());
  }

}
