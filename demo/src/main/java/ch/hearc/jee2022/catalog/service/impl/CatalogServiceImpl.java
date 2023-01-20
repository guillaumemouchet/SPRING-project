package ch.hearc.jee2022.catalog.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import ch.hearc.jee2022.catalog.model.Book;
import ch.hearc.jee2022.catalog.model.Utilisateur;
import ch.hearc.jee2022.catalog.repository.BookRepository;
import ch.hearc.jee2022.catalog.repository.UtilisateurRepository;
import ch.hearc.jee2022.catalog.service.CatalogService;
import jakarta.servlet.http.HttpSession;

@Service
public class CatalogServiceImpl implements CatalogService {

	@Autowired
	BookRepository bookRepository;

	@Autowired
	UtilisateurRepository utilisateurRepository;

	@Autowired
	HttpSession userSession;

	// Décommenter pour le rendu final !!
	public void startApplication() {

		// create a Book
		Book book = new Book("Maniefestation en France ?", "François Beaujoli", "Payot", LocalDate.now());
		Book book2 = new Book("Chevalier D'émeraude 1", "Mireille Magie", "Le rat conteur", LocalDate.now());
		Book book3 = new Book("La Quête Ewilan", "Miguel Richard", "A son conte", LocalDate.now());
		// save the Book
		bookRepository.saveAll(Arrays.asList(book, book2, book3));

		Utilisateur user = new Utilisateur("Guillaume", "123");

		System.out.println(user);
		// save courses
		utilisateurRepository.save(user);

		// add courses to the student
		user.getBooks().addAll(Arrays.asList(book, book2));

		// update the student
		utilisateurRepository.save(user);
	}
	
	// Book
	public void addBookToCatalog(Book book) {
		bookRepository.save(book);
	}

	@Override
	public List<Book> getAllBooksFromCatalog() {
		List<Book> result = new ArrayList<Book>();
		bookRepository.findAll().forEach(result::add);
		return result;
	}

	public List<Book> getAllBooksFromCatalog(int pageNo) {

		int page_size = 5;
		PageRequest paging = PageRequest.of(pageNo, page_size, Direction.ASC,	
				"name", "id");
		Page<Book> page = bookRepository.findAll(paging);

		// Retrieve the items
		return page.toList();
	}

	public void deleteBook(Long id) {
		bookRepository.deleteById(id);
	}

	public void deleteBook(Integer id) {
		Book book = bookRepository.findById(Long.valueOf(id)).get();
		bookRepository.delete(book);

	}

	public Book updateBook(Book book) {
		bookRepository.save(book);
		return book;
	}

	public Book getBookById(Long id) {
		return bookRepository.findById(Long.valueOf(id)).get();

	}

	// Utilisateur
	public void addUserToCatalog(Utilisateur utilisateur) {
		utilisateurRepository.save(utilisateur);
	}

	public List<Utilisateur> getAllUserFromCatalog() {
		List<Utilisateur> result = new ArrayList<Utilisateur>();
		utilisateurRepository.findAll().forEach(result::add);

		return result;

	}

	public void deleteUser(Long id) {
		utilisateurRepository.deleteById(id);
	}

	public Utilisateur updateUser(Utilisateur utilisateur) {
		utilisateurRepository.save(utilisateur);
		return utilisateur;
	}

	public Utilisateur getUserById(Long id) {
		return utilisateurRepository.findById(Long.valueOf(id)).get();

	}

	public Utilisateur getUserByName(String name) {
		return utilisateurRepository.findByName(name);

	}

	// User_Book
	public void addBookToUser(Long book_id, Long utilisateur_id) {
		Utilisateur user = utilisateurRepository.findById(utilisateur_id).get();
		Book book = bookRepository.findById(book_id).get();
		user.getBooks().add(book);
		utilisateurRepository.save(user);
	}

	public void removeBookFromUser(Long book_id, Long utilisateur_id) {
		Utilisateur user = utilisateurRepository.findById(utilisateur_id).get();
		Book book = bookRepository.findById(book_id).get();
		user.getBooks().remove(book);
		utilisateurRepository.save(user);
	}

	public List<Book> getAllBooksFromUser(Long userId) {

		Utilisateur user = utilisateurRepository.findById(userId).get();
		return user.getBooks().stream().toList();

	}

	public List<Book> getAllBooksFromUser(Long userId, int pageNo) {

		Utilisateur user = utilisateurRepository.findById(userId).get();
		int page_size = 5;
		List<Book> page = user.getBooks().stream().skip(pageNo * page_size).toList();
		try {
			page = page.subList(0, page_size);

		} catch (Exception e) {
		}

		System.out.println(page);
		return page;
	}

}
