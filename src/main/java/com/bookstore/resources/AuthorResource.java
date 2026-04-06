package com.bookstore.resources;

import com.bookstore.exceptions.AuthorNotFoundException;
import com.bookstore.exceptions.InvalidInputException;
import com.bookstore.models.Author;
import com.bookstore.models.Book;
import com.bookstore.store.DataStore;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Collection;

@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {
    private final DataStore store = DataStore.getInstance();

    @GET
    public Collection<Author> getAuthors() {
        return store.getAllAuthors().values();
    }

    @POST
    public Response addAuthor(Author author) {
        if (author.getName() == null || author.getName().isEmpty()) {
            throw new InvalidInputException("Author name cannot be empty");
        }
        Author created = store.addAuthor(author);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @GET
    @Path("/{id}")
    public Author getAuthor(@PathParam("id") int id) {
        Author author = store.getAuthor(id);
        if (author == null) {
            throw new AuthorNotFoundException(id);
        }
        return author;
    }

    @PUT
    @Path("/{id}")
    public Author updateAuthor(@PathParam("id") int id, Author author) {
        if (author.getName() == null || author.getName().isEmpty()) {
            throw new InvalidInputException("Author name cannot be empty");
        }
        Author updated = store.updateAuthor(id, author);
        if (updated == null) {
            throw new AuthorNotFoundException(id);
        }
        return updated;
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAuthor(@PathParam("id") int id) {
        boolean deleted = store.deleteAuthor(id);
        if (!deleted) {
            throw new AuthorNotFoundException(id);
        }
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/books")
    public Collection<Book> getBooksByAuthor(@PathParam("id") int id) {
        if (store.getAuthor(id) == null) {
            throw new AuthorNotFoundException(id);
        }
        return store.getBooksByAuthor(id).values();
    }
}
