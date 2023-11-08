package bg.softuni.springsecuritywithjwt.service;

import bg.softuni.springsecuritywithjwt.model.dto.BookRequest;
import bg.softuni.springsecuritywithjwt.model.entity.Book;
import bg.softuni.springsecuritywithjwt.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void save(BookRequest bookRequest) {

        Book book = new Book()
                .setTitle(bookRequest.getTitle())
                .setAuthor(bookRequest.getAuthor())
                .setIsbn(bookRequest.getIsbn());

        this.bookRepository.save(book);
    }

    public List<Book> findAll() {
        return this.bookRepository.findAll();
    }

    public void update(BookRequest bookRequest) {
        Book book = this.bookRepository.findById(bookRequest.getId()).orElseThrow();

        book.setTitle(bookRequest.getTitle())
                .setAuthor(bookRequest.getAuthor())
                .setIsbn(bookRequest.getIsbn());

        this.bookRepository.save(book);
    }
}