package bg.softuni.springsecuritywithjwt.model.dto;

public class BookRequest {

    private Long id;

    private String title;

    private String author;

    private String isbn;

    public BookRequest() {

    }

    public Long getId() {
        return id;
    }

    public BookRequest setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public BookRequest setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public BookRequest setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getIsbn() {
        return isbn;
    }

    public BookRequest setIsbn(String isbn) {
        this.isbn = isbn;
        return this;
    }

}