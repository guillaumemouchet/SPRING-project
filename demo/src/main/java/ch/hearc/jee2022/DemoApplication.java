package ch.hearc.jee2022;

import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import ch.hearc.jee2022.catalog.model.Book;
import ch.hearc.jee2022.catalog.model.Utilisateur;
import ch.hearc.jee2022.catalog.repository.BookRepository;
import ch.hearc.jee2022.catalog.repository.UtilisateurRepository;
import jakarta.annotation.PostConstruct;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class DemoApplication {

	@Autowired
	BookRepository bookRepository;

	@Autowired
	UtilisateurRepository utilisateurRepository;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@PostConstruct
	public void startApplication() {

		// create Books
		Book book = new Book("Maniefestation en France ?", "François Beaujoli", "Payot", LocalDate.now());
		Book book2 = new Book("Chevalier D'émeraude 1", "Mireille Magie", "Le rat conteur", LocalDate.now());
		Book book3 = new Book("La Quête Ewilan", "Miguel Richard", "A son conte", LocalDate.now());
		// save the Books
		bookRepository.saveAll(Arrays.asList(book, book2, book3));

		// Create user
		Utilisateur user = new Utilisateur("Guillaume", "123");

		// save user
		utilisateurRepository.save(user);

		// add book to the user
		user.getBooks().addAll(Arrays.asList(book, book2));

		// update the user
		utilisateurRepository.save(user);
	}
}
