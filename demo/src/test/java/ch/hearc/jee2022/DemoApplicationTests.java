package ch.hearc.jee2022;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ch.hearc.jee2022.catalog.model.Book;
import ch.hearc.jee2022.catalog.model.Utilisateur;
import ch.hearc.jee2022.catalog.repository.BookRepository;
import ch.hearc.jee2022.catalog.repository.UtilisateurRepository;
import ch.hearc.jee2022.catalog.service.CatalogService;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private UtilisateurRepository utilisateurRepository;
	@Autowired
	private CatalogService catalog;

	@Test
	public void testPagination() {

		// create a Book
		Book book = new Book("Maniefest", "Benjamin", "0", LocalDate.now());
		Book book2 = new Book("Chevalier", "Vincent", "1", LocalDate.now());
		Book book3 = new Book("Ewilan", "Titus", "2", LocalDate.now());
		Book book4 = new Book("Maniefest", "Benjamin", "3", LocalDate.now());
		Book book5 = new Book("Chevalier", "Vincent", "4", LocalDate.now());
		Book book6 = new Book("Ewilan", "Titus", "5", LocalDate.now());
		Book book7 = new Book("Maniefest", "Benjamin", "6", LocalDate.now());
		Book book8 = new Book("Chevalier", "Vincent", "7", LocalDate.now());
		Book book9 = new Book("Ewilan", "Titus", "8", LocalDate.now());
		Book book10 = new Book("Maniefest", "Benjamin", "9", LocalDate.now());
		Book book11 = new Book("Chevalier", "Vincent", "10", LocalDate.now());
		Book book12 = new Book("Ewilan", "Titus", "11", LocalDate.now());
		// save the Book
		bookRepository.saveAll(
				Arrays.asList(book, book2, book3, book4, book5, book6, book7, book8, book9, book10, book11, book12));

		Utilisateur user = new Utilisateur("Guillaume", "123"); 

		System.out.println(user);
		// save courses
		utilisateurRepository.save(user);

		// add courses to the student
		user.getBooks().addAll(
				Arrays.asList(book, book5, book2, book3, book4, book6, book7, book8, book9, book10, book11, book12));

		// update the student
		utilisateurRepository.save(user);
		
		
		
		// Tests
		List<Book> bookPage0 = catalog.getAllBooksFromCatalog(0);

		List<Book> expectedBooks = Arrays.asList(book2,book5,book8, book11, book3);
		
		System.out.println("Assertions.assertThat(bookPage0.size() == 5)");
		Assertions.assertTrue(bookPage0.size() == 5);
		
		System.out.println("Assertions.assertThat(bookPage0.equals(expectedBooks))");
		Assertions.assertTrue(bookPage0.equals(expectedBooks));

		
		List<Book> bookPage2 = catalog.getAllBooksFromCatalog(2);
		System.out.println("Assertions.assertThat(bookPage2.size() == 2)");

		Assertions.assertTrue(bookPage2.size() == 2);

	}

}
