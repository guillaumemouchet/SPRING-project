package ch.hearc.jee2022.catalog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ch.hearc.jee2022.catalog.model.Book;
import ch.hearc.jee2022.catalog.model.Utilisateur;
import ch.hearc.jee2022.catalog.service.CatalogService;
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
		if (first) {
			first = Boolean.FALSE;
			catalogService.startApplication();
		}
		System.out.println();
		model.addAttribute("userSession", userSession);
		model.addAttribute("showAcc", Boolean.TRUE);
		model.addAttribute("showNew", Boolean.FALSE);
		return "accueil";
	}

	@GetMapping(value = { "/login" })
	public String showLoginPage(Model model) {

		System.out.println(userSession.getAttribute("user"));
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

		model.addAttribute("book", new Book());
		model.addAttribute("showAcc", Boolean.FALSE);
		model.addAttribute("showNew", Boolean.TRUE);

		model.addAttribute("isNew", Boolean.TRUE);
		model.addAttribute("isEdit", Boolean.FALSE);
		return "/accueil";
	}

	@PostMapping(value = "/create-user")
	public String saveUserCreate(@ModelAttribute Utilisateur user, BindingResult errors, Model model,
			@RequestParam String conPassword) {
		model.addAttribute("userSession", userSession);
		System.out.println("Create");
		System.out.println(user.getName());
		System.out.println(user.getPassword());
		System.out.println(conPassword);
		// Controller qu'il existe

		// new Utilisateur // Controler les mots de passes
		if (user.getPassword().equals(conPassword)) {
			catalogService.addUserToCatalog(user);
			userSession.setAttribute("user", user.getId());
			System.out.println("Create Session");
		}

		return "redirect:/accueil";

	}

	@PostMapping(value = "/save-user")
	public String saveUserLogin(@ModelAttribute Utilisateur user, BindingResult errors, Model model) {
		model.addAttribute("userSession", userSession);
		System.out.println("Login");
		System.out.println(user.getName());
		System.out.println(user.getPassword());

		Utilisateur utilisateur = null;
		try {
			utilisateur = catalogService.getUserByName(user.getName());
			System.out.println(utilisateur.getName());

		} catch (Exception e) { // User don't exist so no connection }
			System.out.println("NO");
			return "redirect:/login";

		}
		if (utilisateur.getPassword().equals(user.getPassword())) {
			// C'set les bon mots de passes
			System.out.println("CreateSession");

			userSession.setAttribute("user", utilisateur.getId());
		}

		return "redirect:/show-user-books";
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

		return "redirect:/show-books";
	}

	@PostMapping(value = "/add-collection")
	public String addToCollection(@ModelAttribute Book book, BindingResult errors, Model model) {
		model.addAttribute("userSession", userSession);
		model.addAttribute("isGlobal", Boolean.TRUE);

		try {
			catalogService.addBookToUser(book.getId(), (long) (userSession.getAttribute("user")));
		} catch (Exception e) {
			// TODO: handle exception
			return "redirect:/show-books";

		}
		return "redirect:/show-books";
	}

	@PostMapping(value = "/delete-user-book")
	public String removeFromCollection(@ModelAttribute Book book, BindingResult errors, Model model) {
		model.addAttribute("userSession", userSession);
		model.addAttribute("isGlobal", Boolean.FALSE);

		try {
			catalogService.removeBookFromUser(book.getId(), (long) (userSession.getAttribute("user")));
		} catch (Exception e) {
			return "redirect:/show-user-books";

		}
		// user

		return "redirect:/show-user-books";
	}

	@GetMapping(value = { "/show-books" })
	public String showBooks(Model model) {
		model.addAttribute("userSession", userSession);

		model.addAttribute("books", catalogService.getAllBooksFromCatalog());
		model.addAttribute("isGlobal", Boolean.TRUE);
		return "showBook";
	}

	@GetMapping(value = { "/show-user-books" })
	public String showUserBooks(Model model) {
		model.addAttribute("userSession", userSession);

		model.addAttribute("isGlobal", Boolean.FALSE);

		try {
			model.addAttribute("books", catalogService.getAllBooksFromUser((long) (userSession.getAttribute("user"))));
		} catch (Exception e) {
			return "redirect:/accueil";
		}
		return "showBook";
	}

}
