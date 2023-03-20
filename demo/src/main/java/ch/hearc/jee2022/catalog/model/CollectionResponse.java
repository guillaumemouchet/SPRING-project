package ch.hearc.jee2022.catalog.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class CollectionResponse {
	
	private String name;
	private Set<BookResponse> books = new HashSet<>();
	
	public CollectionResponse(String name, Set<BookResponse> books) {
		this.name = name;
		this.books = books;
	}
	
	public CollectionResponse(Utilisateur utilisateur)
	{
		this.name = utilisateur.getName();
		for(Book book : utilisateur.getBooks())
		{
			books.add(new BookResponse(book));
		}
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set<BookResponse> getBooks() {
		return books;
	}
	public void setBooks(Set<BookResponse> books) {
		this.books = books;
	}
	@Override
	public int hashCode() {
		return Objects.hash(books, name);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CollectionResponse other = (CollectionResponse) obj;
		return Objects.equals(books, other.books) && Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CollectionResponse [name=");
		builder.append(name);
		builder.append(", books=");
		builder.append(books);
		builder.append("]");
		return builder.toString();
	}
	
}
