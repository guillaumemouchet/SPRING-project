package ch.hearc.jee2022.catalog.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.hearc.jee2022.catalog.model.Book;
import ch.hearc.jee2022.catalog.model.BookResponse;
import ch.hearc.jee2022.catalog.model.Utilisateur;
import ch.hearc.jee2022.catalog.model.UtilisateurResponse;
import ch.hearc.jee2022.catalog.service.CatalogService;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
public class ApiController {

	@Autowired
	CatalogService catalogService;

	@Autowired
	HttpSession userSession;

	/*************************************************************
	 * 
	 * Restfull Books
	 * @throws URISyntaxException 
	 *
	 **************************************************************/
//	@GetMapping("/book/{id}")
//	public BookResponse showBook(@PathVariable("id") int id) { // insert
//		// Id starts at 1
//
//		BookResponse book = new BookResponse(catalogService.getBookById((long) id));
//
//		System.out.println(book);
//
//		return book;
//	}
	
	@GetMapping("/book/{id}")
	public ResponseEntity<BookResponse> showBook(@PathVariable("id") int id) throws URISyntaxException { // insert
		// Id starts at 1

		BookResponse book = new BookResponse(catalogService.getBookById((long) id));

		System.out.println(book);
		//URI location = new URI(""); Pas nécessaire pour un get, intéressant de préciser l'emplacement de la nouvelle ressource p.ex.
		HttpHeaders responseHeader= new HttpHeaders();
		//responseHeader.setLocation(location);
		//responseHeader.set("MyResponseHeader", "MyValue"); Pas nécessaire, Spring le fait tout seul
		return new ResponseEntity<BookResponse>(book, responseHeader, HttpStatus.FOUND);
	}

//	@DeleteMapping("/book/{id}")
//	public void deleteById(@PathVariable("id") int id) {
//		catalogService.deleteBook((long) id);
//	}

	@PostMapping("/book")
	public String newBook(@RequestBody Book book) {
		System.out.println(book);
		catalogService.addBookToCatalog(book);
		return "200 : all good";
	}

	@PutMapping("/book/{id}")
	public String replaceBook(@ModelAttribute Book book, @PathVariable("id") long id) {
		book.setId(id);
		catalogService.updateBook(book);
		return "200 : all good";
	}

	/*************************************************************
	 * 
	 * 						Restfull Utilisateurs
	 *
	 **************************************************************/
	@GetMapping("/utilisateur/{id}")
	public UtilisateurResponse showUtilisateur(@PathVariable("id") int id) { // insert
		// Id starts at 1

		UtilisateurResponse utilisateur = new UtilisateurResponse(catalogService.getUserById((long) id));

		System.out.println(utilisateur);

		return utilisateur;
	}

	@PostMapping(value = "/utilisateur")
	public String createUser(@RequestBody Utilisateur newUtilisateur, @RequestParam String conPassword) {
		// model.addAttribute("userSession", userSession); TODO : ADD user in the session

		// Try to get the name of the new user
		UtilisateurResponse ExistingUtilisateur = new UtilisateurResponse(
				catalogService.getUserByName(newUtilisateur.getName()));

		// If no user where found we can create a new on
		if (ExistingUtilisateur == null) {
			// Check the passwords
			if (newUtilisateur.getPassword().equals(conPassword)) {
				BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

				newUtilisateur.setPassword(bcrypt.encode(conPassword));
				catalogService.addUserToCatalog(newUtilisateur);
				userSession.setAttribute("user", newUtilisateur.getId());

				return "200 : all good";
			}
			// passwords where wrong, go back to the create account page
			return "400 : Error in the authentification";
		}
		// User was already found can't create a new one
		return "401 : User already exist with that name";
	}
	
	/*************************************************************
	 * 
	 * Connexions
	 *
	 **************************************************************/
	
	@PostMapping(value = "/login")
	public String saveUserLogin(@RequestBody Utilisateur newUtilisateur) {

		Utilisateur ExistingUtilisateur = catalogService.getUserByName(newUtilisateur.getName());

		if(ExistingUtilisateur == null)
		{
			return "400 : user don't exist";

		}
		BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

		
		if (bcrypt.matches(newUtilisateur.getPassword(), ExistingUtilisateur.getPassword()))
		{ 
			//Same password can create the session
			userSession.setAttribute("user", ExistingUtilisateur.getId());
		}

		return "200 : all good";
	}
	
	@PostMapping(value = { "/logout" })
	public String Logout(Model model) {

		userSession.setAttribute("user", null);

		return "200 : all good";
	}

	/*************************************************************
	 * 
	 * Restfull Collection
	 *
	 **************************************************************/
	@PostMapping(value = "/book/{book_id}/utilisateur/{utilisateur_id}")
	public String addToCollection(@PathVariable("book_id") long book_id,
			@PathVariable("utilisateur_id") long utilisateur_id) {
		catalogService.addBookToUser(book_id, utilisateur_id);
		return "200 : all good";
	}

	@DeleteMapping(value = "/book/{book_id}/utilisateur/{utilisateur_id}")
	public String removeFromCollection(@PathVariable("book_id") long book_id,
			@PathVariable("utilisateur_id") long utilisateur_id) {
		catalogService.removeBookFromUser(book_id, utilisateur_id);
		return "200 : all good";
	}

}
