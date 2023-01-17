package ch.hearc.jee2022.catalog.service;

import java.util.List;

import ch.hearc.jee2022.catalog.model.Book;
import ch.hearc.jee2022.catalog.model.Utilisateur;

public interface CatalogService {

	// Commun
	public void startApplication();

	// Book
	public void addBookToCatalog(Book book);

	public List<Book> getAllBooksFromCatalog();
	public List<Book> getAllBooksFromCatalog(int pageNo);

	public void deleteBook(Long id);

	public Book updateBook(Book book);

	public Book getBookById(Long id);

	// Utilisateur
	public void addUserToCatalog(Utilisateur utilisateur);

	public List<Utilisateur> getAllUserFromCatalog();

	public void deleteUser(Long id);

	public Utilisateur updateUser(Utilisateur utilisateur);

	public Utilisateur getUserById(Long id);
	public Utilisateur getUserByName(String name);


	// User_book

	public void addBookToUser(Long book_id, Long utilisateur_id);

	public void removeBookFromUser(Long book_id, Long utilisateur_id);

	public List<Book> getAllBooksFromUser(Long utilisateur_id);
	public List<Book> getAllBooksFromUser(Long userId, int pageNo);


}
