package com.up.libraryBookingSystem.dao;


import com.up.libraryBookingSystem.ENUMS.GENRES;
import com.up.libraryBookingSystem.pojo.Books;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Locale;
@Repository("books")
public class BooksDataAccessService implements BooksDao {

    private JdbcTemplate jdbcTemplate;

    public BooksDataAccessService(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Books selectBookById(Integer bookId) {
        List<Books> booksList = displayBooks();
        for (int i = 0; i < booksList.size() ; i++) {
            if (booksList.get(i).getBookId().equals(bookId)){
                return booksList.get(i);
            }
        }
        return null;
    }
    @Override
    public Books selectBookByTitle(String bookTitle) {
        List<Books> booksList = displayBooks();
        for (int i = 0; i < booksList.size() ; i++) {
            if (booksList.get(i).getTitle()
                    .toLowerCase(Locale.ROOT)
                    .equals(bookTitle.toLowerCase(Locale.ROOT))){
                return booksList.get(i);
            }
        }
        return null;
    }
    //todo: write method for finding book by author id
    public Books selectBookByAuthorId(Integer AuthorId){
        List<Books> booksList = displayBooks();
        for (int i = 0; i < booksList.size() ; i++) {
            if (booksList.get(i).getAuthorId().equals(AuthorId)){
                return booksList.get(i);
            }
        }
        return null;
    }

    @Override
    public int addBook(Books book) {

        String sql= """
                INSERT INTO books (title, genre, author_id, loaned, ISBN) 
                VALUES (?,?,?,?,?)
                """;

        int rowsAffected =jdbcTemplate.update(
                sql,
                book.getTitle(),
                book.getGenre().name(),
                book.getAuthorId(),
                book.isLoaned(),
                book.getISBN()
        );
        return rowsAffected;
    }

    @Override
    public List<Books> displayBooks() {
        String sql = """
                SELECT title, genre, author_id, loaned, ISBN
                FROM books 
                WHERE book_id = ?
                """;
        RowMapper<Books> booksRowMapper = ((rs, rowNum) -> {
            Books book = new Books(
                    rs.getString("title"),
                    GENRES.valueOf(rs.getString("genre")),
                    rs.getInt("author_id"),
                    rs.getBoolean("loaned"),
                    rs.getInt("ISBN")
            );
            return book;
        });
        List<Books> authors = jdbcTemplate.query(sql, booksRowMapper);
        return authors;
    }


    @Override
    public int deleteBook(Integer bookId) {
        String sql = """
                DELETE * FROM books
                WHERE bookId = ?
                """;
        return jdbcTemplate.update(
                sql,
                bookId);

    }
    @Override
    public int updateBook(Integer bookId, Books bookUpdate){
        String sql = """
                UPDATE books 
                SET (title, genre, author_id, loaned, ISBN) = (?, ?, ?, ?, ?)    
                WHERE bookId = ?            
                """;
        return jdbcTemplate.update(
                sql,
                bookUpdate.getTitle(),
                bookUpdate.getGenre(),
                bookUpdate.getAuthorId(),
                bookUpdate.isLoaned(),
                bookUpdate.getISBN()
        );
    }
    @Override
    public boolean isLoaned(Books book){
        return book.isLoaned();
    }

}
//test for branch 
