package main.repository;

import main.model.Mailing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailingRepo extends JpaRepository<Mailing, Long> {

}
