package com.bookstore.resources;

import com.bookstore.exceptions.BookNotFoundException;
import com.bookstore.exceptions.InvalidInputException;
import com.bookstore.models.Book;
import com.bookstore.store.DataStore;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Collection;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {
    private final DataStore store = DataStore.getInstance();

    @GET
    public Collection<Book> getBooks() {
        return store.getAllBooks().values();
    }

    @POST
    public Response addBook(Book book) {
        if (book.getTitle() == null || book.getTitle().isEmpty()) {
            throw new InvalidInputException("Title cannot be empty");
        }
        if (book.getAuthorId() <= 0) {
            throw new InvalidInputException("Valid authorId is required");
        }
        Book created = store.addBook(book);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @GET
    @Path("/{id}")
    public Book getBook(@PathParam("id") int id) {
        Book book = store.getBook(id);
        if (book == null) {
            throw new BookNotFoundException(id);
        }
        return book;
    }

    @PUT
    @Path("/{id}")
    public Book updateBook(@PathParam("id") int id, Book book) {
        if (book.getTitle() == null || book.getTitle().isEmpty()) {
            throw new InvalidInputException("Title cannot be empty");
        }
        Book updated = store.updateBook(id, book);
        if (updated == null) {
            throw new BookNotFoundException(id);
        }
        return updated;
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") int id) {
        boolean deleted = store.deleteBook(id);
        if (!deleted) {
            throw new BookNotFoundException(id);
        }
        return Response.noContent().build();
    }
}
