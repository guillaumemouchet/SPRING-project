package ch.hearc.jee2022;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {

		SpringApplication.run(DemoApplication.class, args);
	}

	/*
	 * @Bean public CommandLineRunner mappingDemo(BookRepository bookRepository,
	 * UtilisateurRepository utilisateurRepository) { return args -> { // create a
	 * Book Book book = new Book("Maniefest", "Benjamin", "Payot",
	 * LocalDateTime.now()); Book book2 = new Book("Chevalier", "Vincent", "Payot",
	 * LocalDateTime.now()); Book book3 = new Book("Ewilan", "Titus", "Franz",
	 * LocalDateTime.now());
	 * 
	 * // save the Book bookRepository.saveAll(Arrays.asList(book, book2, book3));
	 * 
	 * // create three courses Utilisateur user = new Utilisateur("Guillaume");
	 * 
	 * // save courses utilisateurRepository.save(user);
	 * 
	 * // add courses to the student user.getBooks().addAll(Arrays.asList(book,
	 * book2, book3));
	 * 
	 * // update the student utilisateurRepository.save(user);         };     }
	 */

}
