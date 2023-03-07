package ch.hearc.jee2022.catalog.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity(name = "book")
@Table(name = "books")
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String author;
	private String name;
	private String editor;
	private LocalDate release;

	@ManyToMany(mappedBy = "books", fetch = FetchType.LAZY)
	private Set<Utilisateur> utilisateurs = new HashSet<>();

	public Set<Utilisateur> getUsers() {
		return utilisateurs;
	}

	public Book(String name, String author, String editor, LocalDate release) {
		this.author = author;
		this.name = name;
		this.editor = editor;
		this.release = release;
	}

	public Book() {

	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Book other = (Book) obj;
		return Objects.equals(id, other.id);
	}

	public Long getId() {
		return id;
	}

	public String getAuthor() {
		return author;
	}

	public String getName() {
		return name;
	}

	public String getEditor() {
		return editor;
	}

	public LocalDate getRelease() {
		return release;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Book [id=");
		builder.append(id);
		builder.append(", author=");
		builder.append(author);
		builder.append(", name=");
		builder.append(name);
		builder.append(", editor=");
		builder.append(editor);
		builder.append(", release=");
		builder.append(release);
		builder.append("]");
		return builder.toString();
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public void setRelease(LocalDate release) {
		this.release = release;
	}

}
