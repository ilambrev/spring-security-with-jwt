package bg.softuni.springsecuritywithjwt.repository;

import bg.softuni.springsecuritywithjwt.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {



}