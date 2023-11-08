package bg.softuni.springsecuritywithjwt.web;

import bg.softuni.springsecuritywithjwt.model.dto.BookRequest;
import bg.softuni.springsecuritywithjwt.model.entity.Book;
import bg.softuni.springsecuritywithjwt.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<Book>> findAllBooks() {
        return ResponseEntity.ok(this.bookService.findAll());
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody BookRequest bookRequest) {
        this.bookService.save(bookRequest);

        return ResponseEntity.accepted().build();
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody BookRequest bookRequest) {
        this.bookService.update(bookRequest);

        return ResponseEntity.accepted().build();
    }

}