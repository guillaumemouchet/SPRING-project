package ch.hearc.jee2022.catalog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.hearc.jee2022.catalog.model.Book;
import ch.hearc.jee2022.catalog.model.BookResponse;
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

	@GetMapping("/book/{id}")
	public BookResponse showBook(@PathVariable("id") int id) { // insert
		// Id starts at 1

		BookResponse book = new BookResponse(catalogService.getBookById((long) id));

		System.out.println(book);
		
		return book;
	}
	
	@GetMapping("/utilisateur/{id}")
	public UtilisateurResponse showUtilisateur(@PathVariable("id") int id) { // insert
		// Id starts at 1

		UtilisateurResponse utilisateur = new UtilisateurResponse(catalogService.getUserById((long) id));

		System.out.println(utilisateur);
		
		return utilisateur;
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
	public void replaceBook(@ModelAttribute Book book, @PathVariable("id") long id) {
		book.setId(id);
		catalogService.updateBook(book);
		
	}

}
