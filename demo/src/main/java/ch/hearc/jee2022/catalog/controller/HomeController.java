package ch.hearc.jee2022.catalog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ch.hearc.jee2022.catalog.model.Book;
import ch.hearc.jee2022.catalog.model.Utilisateur;
import ch.hearc.jee2022.catalog.service.CatalogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	@Autowired
	CatalogService catalogService;
	public boolean first = Boolean.TRUE;

	@Autowired
	HttpSession userSession;

	@GetMapping(value = { "/", "/accueil" })
	public String showAccueilPage(Model model) {
		
		//Fill DB with values on first load
		if (first) {
			first = Boolean.FALSE;
			catalogService.startApplication();
		}
		model.addAttribute("userSession", userSession);
		model.addAttribute("showAcc", Boolean.TRUE);
		model.addAttribute("showNew", Boolean.FALSE);
		return "accueil";
	}

	@GetMapping(value = { "/login" })
	public String showLoginPage(Model model) {

		//Check if the user is already connected
		if (userSession.getAttribute("user") == null) {
			model.addAttribute("utilisateur", new Utilisateur());

			model.addAttribute("userSession", userSession);

			model.addAttribute("login", Boolean.TRUE);
			model.addAttribute("create", Boolean.FALSE);
			return "login";
		}
		return "redirect:/acceuil";
	}

	@GetMapping(value = { "/logout" })
	public String Logout(Model model) {

		userSession.setAttribute("user", null);

		return "redirect:/login";
	}

	@GetMapping(value = { "/create-account" })
	public String showCreateAccountPage(Model model) {
		model.addAttribute("utilisateur", new Utilisateur());

		model.addAttribute("userSession", userSession);
		model.addAttribute("login", Boolean.FALSE);
		model.addAttribute("create", Boolean.TRUE);
		return "login";
	}

	@GetMapping(value = { "/new-book" })
	public String showCreateBook(Model model) {
		model.addAttribute("userSession", userSession);

		//Check if the user is already connected
		if (userSession.getAttribute("user") == null) {
			return "redirect:/accueil";
		}

		model.addAttribute("book", new Book());
		model.addAttribute("showAcc", Boolean.FALSE);
		model.addAttribute("showNew", Boolean.TRUE);

		model.addAttribute("isNew", Boolean.TRUE);
		model.addAttribute("isEdit", Boolean.FALSE);
		return "accueil";
	}

	@PostMapping(value = "/create-user")
	public String saveUserCreate(@ModelAttribute Utilisateur user, BindingResult errors, Model model,
			@RequestParam String conPassword) {
		model.addAttribute("userSession", userSession);

		//Try to get the name of the new user
		Utilisateur utilisateur = catalogService.getUserByName(user.getName());
		
		//If no user where found we can create a new on
		if(utilisateur == null)
		{
			//Check the passwords
			if (user.getPassword().equals(conPassword)) {
				BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

				user.setPassword(bcrypt.encode(conPassword));
				catalogService.addUserToCatalog(user);
				userSession.setAttribute("user", user.getId());
				System.out.println("Create Session");
				return "redirect:/accueil";
			}
			//passwords where wrong, go back to the create account page
			return "redirect:/create-account";
		}
		//User was already found can't create a new one
		return "redirect:/create-account";

		

	}

	@PostMapping(value = "/save-user")
	public String saveUserLogin(@ModelAttribute Utilisateur user, BindingResult errors, Model model) {
		model.addAttribute("userSession", userSession);

		Utilisateur utilisateur = catalogService.getUserByName(user.getName());

		if(utilisateur == null)
		{
			return "redirect:/login";

		}
		BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
		
		System.out.println(user.getPassword());
		System.out.println(utilisateur.getPassword());
		
		if (bcrypt.matches(user.getPassword(), utilisateur.getPassword()))
		{ //Same password can create the session
			System.out.println("Create Session");

			userSession.setAttribute("user", utilisateur.getId());
		}

		return "redirect:/show-user-books/0";
	}

	@PostMapping(value = "/save-book")
	public String saveBeer(@ModelAttribute Book book, BindingResult errors, Model model, @RequestParam String type) {
		model.addAttribute("userSession", userSession);

		if (type.equals("new")) {
			catalogService.addBookToCatalog(book);
		} else {
			catalogService.deleteBook(book.getId());
			catalogService.addBookToCatalog(book);
		}

		return "redirect:/show-books/0";
	}

	@PostMapping(value = "/add-collection")
	public String addToCollection(@ModelAttribute Book book, BindingResult errors, Model model) {
		model.addAttribute("userSession", userSession);
		model.addAttribute("isGlobal", Boolean.TRUE);

		try {
			catalogService.addBookToUser(book.getId(), (long) (userSession.getAttribute("user")));
		} catch (Exception e) {
			return "redirect:/show-books/0";

		}
		return "redirect:/show-books/0";
	}

	@PostMapping(value = "/delete-user-book")
	public String removeFromCollection(@ModelAttribute Book book, BindingResult errors, Model model) {
		model.addAttribute("userSession", userSession);
		model.addAttribute("isGlobal", Boolean.FALSE);

		try {
			catalogService.removeBookFromUser(book.getId(), (long) (userSession.getAttribute("user")));
		} catch (Exception e) {
			return "redirect:/show-user-books/0";

		}
		return "redirect:/show-user-books/0";
	}

	@GetMapping(value = { "/show-books/{pageNo}" })
	public String showBooks(Model model, @PathVariable int pageNo) {
		model.addAttribute("userSession", userSession);

		//Get number of book to create how many pages we need
		int numberofbooks = catalogService.getAllBooksFromCatalog().size();
		int numberofpages = (int) Math.ceil(numberofbooks / 5.0);

		if (pageNo >= numberofpages && pageNo != 0) { //check that pageNo is not to big
			return "redirect:/show-books/0";
		}
		
		model.addAttribute("books", catalogService.getAllBooksFromCatalog(pageNo));

		//Pass number of page to create pagination href in view
		model.addAttribute("number_of_page", numberofpages);

		model.addAttribute("isGlobal", Boolean.TRUE);
		return "showBook";
	}

	@GetMapping(value = { "/show-user-books/{pageNo}" })
	public String showUserBooks(Model model, @PathVariable int pageNo) {
		model.addAttribute("userSession", userSession);
		model.addAttribute("isGlobal", Boolean.FALSE);

		//Check user is connected
		if (userSession.getAttribute("user") == null) {
			return "redirect:/accueil";
		}
		
		//Get number of book to create how many pages we need
		int numberofbooks = catalogService.getAllBooksFromUser((long) (userSession.getAttribute("user"))).size();
		int numberofpages = (int) Math.ceil(numberofbooks / 5.0);
		
		if (pageNo >= numberofpages && pageNo != 0) { //check that pageNo is not to big
			return "redirect:/show-user-books/0";
		}

		//Pass number of page to create pagination href in view

		model.addAttribute("number_of_page", numberofpages);
		model.addAttribute("books",
				catalogService.getAllBooksFromUser((long) (userSession.getAttribute("user")), pageNo));

		return "showBook";
	}

}
