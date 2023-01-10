package ch.hearc.jee2022.catalog.repository;

import org.springframework.data.repository.CrudRepository;

import ch.hearc.jee2022.catalog.model.Book;

public interface BookRepository extends CrudRepository<Book, Long> {

}