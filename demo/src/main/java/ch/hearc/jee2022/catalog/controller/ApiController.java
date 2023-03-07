package ch.hearc.jee2022.catalog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.hearc.jee2022.catalog.model.Book;
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
	public void showBook(@PathVariable("id") int id) { // insert
		// Id starts at 1

		Book book = catalogService.getBookById((long) id);

		System.out.println(book);
	}

	@DeleteMapping("/book/{id}")
	public void deleteById(@PathVariable("id") int id) {
		catalogService.deleteBook((long) id);
	}

	@PostMapping("/book")
	public void newBook(@ModelAttribute Book book) {
		catalogService.addBookToCatalog(book);
	}

	@PutMapping("/book/{id}")
	public void replaceBook(@ModelAttribute Book book, @PathVariable("id") long id) {
		book.setId(id);
		catalogService.updateBook(book);
		
	}

}
