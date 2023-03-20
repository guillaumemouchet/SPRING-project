package ch.hearc.jee2022.catalog.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
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
import ch.hearc.jee2022.catalog.model.CollectionResponse;
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
	 * 
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
		HttpHeaders responseHeader = new HttpHeaders();
		try {
			BookResponse book = new BookResponse(catalogService.getBookById((long) id));
			System.out.println(book);
			// URI location = new URI(""); Pas nécessaire pour un get, intéressant de
			// préciser l'emplacement de la nouvelle ressource p.ex.
			// responseHeader.setLocation(location);
			// responseHeader.set("MyResponseHeader", "MyValue"); Pas nécessaire, Spring le
			// fait tout seul
			return new ResponseEntity<BookResponse>(book, responseHeader, HttpStatus.FOUND);
		} catch (NoSuchElementException e) {
			BookResponse book = new BookResponse(new Book());
			return new ResponseEntity<BookResponse>(book, responseHeader, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			BookResponse book = new BookResponse(new Book());
			return new ResponseEntity<BookResponse>(book, responseHeader, HttpStatus.I_AM_A_TEAPOT);
		}
	}

//	@DeleteMapping("/book/{id}")
//	public void deleteById(@PathVariable("id") int id) {
//		catalogService.deleteBook((long) id);
//	}

	@PostMapping("/book")
	public ResponseEntity<String> newBook(@RequestBody Book book) {
		HttpHeaders responseHeader = new HttpHeaders();
		try
		{
			book = catalogService.addBookToCatalog(book);
			URI location = new URI("/book/"+book.getId());
			responseHeader.setLocation(location);
			return new ResponseEntity<String>("Book stored successfully", responseHeader, HttpStatus.ACCEPTED);
		}catch (Exception e) {
			//Isn't called if there is an error in the json
			return new ResponseEntity<String>(e.getMessage(), responseHeader, HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/book/{id}")
	public ResponseEntity<String> replaceBook(@RequestBody Book book, @PathVariable("id") long id) {
		HttpHeaders responseHeader = new HttpHeaders();
		try
		{
			//if no books have this id a new one is created
			book.setId(id);
			book = catalogService.addBookToCatalog(book);
			URI location = new URI("/book/"+book.getId());
			responseHeader.setLocation(location);
			return new ResponseEntity<String>("Book stored successfully", responseHeader, HttpStatus.ACCEPTED);
		}catch (Exception e) {
			//Isn't called if there is an error in the json
			return new ResponseEntity<String>(e.getMessage(), responseHeader, HttpStatus.BAD_REQUEST);
		}
	}

	/*************************************************************
	 * 
	 * Restfull Utilisateurs
	 *
	 **************************************************************/
	@GetMapping("/utilisateur/{id}")
	public ResponseEntity<UtilisateurResponse> showUtilisateur(@PathVariable("id") int id) { // insert
		// Id starts at 1
		HttpHeaders responseHeader = new HttpHeaders();
		try {
			UtilisateurResponse utilisateur = new UtilisateurResponse(catalogService.getUserById((long) id));

			return new ResponseEntity<UtilisateurResponse>(utilisateur, responseHeader, HttpStatus.FOUND);
		} catch (NoSuchElementException e) {
			UtilisateurResponse utilisateur = new UtilisateurResponse(new Utilisateur());
			return new ResponseEntity<UtilisateurResponse>(utilisateur, responseHeader, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			UtilisateurResponse utilisateur = new UtilisateurResponse(new Utilisateur());
			return new ResponseEntity<UtilisateurResponse>(utilisateur, responseHeader, HttpStatus.I_AM_A_TEAPOT);
		}
	}

	//Ask Question to the teacher
	@PostMapping(value = "/utilisateur")
	public String createUser(@RequestBody Utilisateur newUtilisateur, @RequestParam String conPassword) {
		// model.addAttribute("userSession", userSession); TODO : ADD user in the
		// session

		// Try to get the name of the new user
		UtilisateurResponse ExistingUtilisateur = new UtilisateurResponse(
				catalogService.getUserByName(newUtilisateur.getName()));

		// If no user where found we can create a new on
		if (ExistingUtilisateur == null) {
			// Check the passwords
			if (newUtilisateur.getPassword().equals(conPassword)) {
			//	BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

				//newUtilisateur.setPassword(bcrypt.encode(conPassword));
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
	public ResponseEntity<UtilisateurResponse> saveUserLogin(@RequestBody Utilisateur newUtilisateur) {
		HttpHeaders responseHeader = new HttpHeaders();

		Utilisateur existingUtilisateur = catalogService.getUserByName(newUtilisateur.getName());

		if (existingUtilisateur == null) {
			UtilisateurResponse utilisateur = new UtilisateurResponse(new Utilisateur());
			return new ResponseEntity<UtilisateurResponse>(utilisateur, responseHeader, HttpStatus.NOT_FOUND);
		}
		BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

		if (bcrypt.matches(newUtilisateur.getPassword(), existingUtilisateur.getPassword())) {
			// Same password can create the session
			userSession.setAttribute("user", existingUtilisateur.getId());
			UtilisateurResponse utilisateur = new UtilisateurResponse(existingUtilisateur);
			return new ResponseEntity<UtilisateurResponse>(utilisateur, responseHeader, HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<UtilisateurResponse>(new UtilisateurResponse(new Utilisateur()), responseHeader, HttpStatus.BAD_REQUEST);

	}

	@PostMapping(value = { "/logout" })
	public ResponseEntity<String> Logout(Model model) {
		HttpHeaders responseHeader = new HttpHeaders();
		try
		{
			userSession.setAttribute("user", null);
			return new ResponseEntity<String>("Logout successful", responseHeader, HttpStatus.OK);
		}
		catch (Exception e) {
			return new ResponseEntity<String>("Something went wrong", responseHeader, HttpStatus.BAD_REQUEST);
		}
	}

	/*************************************************************
	 * 
	 * Restfull Collection
	 * @throws URISyntaxException 
	 *
	 **************************************************************/
	@GetMapping("/books/utilisateur/{id}")
	public ResponseEntity<CollectionResponse> showCollection(@PathVariable("id") int id) { // insert
		// Id starts at 1
		HttpHeaders responseHeader = new HttpHeaders();
		try {
			
			CollectionResponse collection = new CollectionResponse(catalogService.getUserById((long) id));
			return new ResponseEntity<CollectionResponse>(collection, responseHeader, HttpStatus.FOUND);
		} catch (NoSuchElementException e) {
			CollectionResponse collection = new CollectionResponse(new Utilisateur());
			return new ResponseEntity<CollectionResponse>(collection, responseHeader, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			CollectionResponse collection = new CollectionResponse(new Utilisateur());
			return new ResponseEntity<CollectionResponse>(collection, responseHeader, HttpStatus.I_AM_A_TEAPOT);
		}
	}
	
	@PostMapping(value = "/book/{book_id}/utilisateur/{utilisateur_id}")
	public ResponseEntity<String> addToCollection(@PathVariable("book_id") long book_id,
			@PathVariable("utilisateur_id") long utilisateur_id) throws URISyntaxException {
		HttpHeaders responseHeader = new HttpHeaders();
		try
		{
			catalogService.addBookToUser(book_id, utilisateur_id);
			URI location = new URI("/books/utilisateur/" + utilisateur_id);
			responseHeader.setLocation(location);
			return new ResponseEntity<String>("Book added successfully to collection", responseHeader, HttpStatus.ACCEPTED);
		}catch (NoSuchElementException e) {
			return new ResponseEntity<String>("No such book or user", responseHeader, HttpStatus.NOT_FOUND);
		}
		catch (Exception e) {
			return new ResponseEntity<String>("Trying to meddle with dark and ancient magic are you eh ?", responseHeader, HttpStatus.I_AM_A_TEAPOT);
		}
	}

	@DeleteMapping(value = "/book/{book_id}/utilisateur/{utilisateur_id}")
	public ResponseEntity<String> removeFromCollection(@PathVariable("book_id") long book_id,
			@PathVariable("utilisateur_id") long utilisateur_id) {
		HttpHeaders responseHeader = new HttpHeaders();
		try
		{
			catalogService.removeBookFromUser(book_id, utilisateur_id);
			return new ResponseEntity<String>("Book added removed from collection", responseHeader, HttpStatus.ACCEPTED);
		}catch (NoSuchElementException e) {
			return new ResponseEntity<String>("No such book or user", responseHeader, HttpStatus.NOT_FOUND);
		}
		catch (Exception e) {
			return new ResponseEntity<String>("Why are you like this ?", responseHeader, HttpStatus.I_AM_A_TEAPOT);
		}
	}

}
