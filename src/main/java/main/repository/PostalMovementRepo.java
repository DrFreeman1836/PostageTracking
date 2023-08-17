package main.repository;

import java.util.List;
import java.util.Optional;
import main.model.Mailing;
import main.model.PostalMovement;
import main.model.PostalOffice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostalMovementRepo extends JpaRepository<PostalMovement, Long> {

  Optional<PostalMovement> findFirstByPostalOfficeAndMailingOrderByIdDesc(PostalOffice postalOffice, Mailing mailing);

  List<PostalMovement> findAllByMailingOrderByIdDesc(Mailing mailing);

  Optional<PostalMovement> findFirstByMailingOrderByIdDesc(Mailing mailing);

}
