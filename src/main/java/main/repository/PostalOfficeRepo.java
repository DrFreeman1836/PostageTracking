package main.repository;

import java.util.Optional;
import main.model.PostalOffice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostalOfficeRepo extends JpaRepository<PostalOffice, Long> {

  Optional<PostalOffice> findByIndexPostal(Integer indexPostal);

}
