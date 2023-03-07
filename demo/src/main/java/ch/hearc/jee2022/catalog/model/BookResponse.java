package ch.hearc.jee2022.catalog.model;

import java.time.LocalDate;
import java.util.Objects;

public class BookResponse {

	private Long id;
	private String author;
	private String name;
	private String editor;
	private LocalDate release;
	
	
	public BookResponse(Book book)
	{
		this.id = book.getId();
		this.author = book.getAuthor();
		this.name = book.getName();
		this.editor = book.getEditor();
		this.release = book.getRelease();
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BookResponse [id=");
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


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getAuthor() {
		return author;
	}


	public void setAuthor(String author) {
		this.author = author;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getEditor() {
		return editor;
	}


	public void setEditor(String editor) {
		this.editor = editor;
	}


	public LocalDate getRelease() {
		return release;
	}


	public void setRelease(LocalDate release) {
		this.release = release;
	}


	@Override
	public int hashCode() {
		return Objects.hash(author, editor, id, name, release);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BookResponse other = (BookResponse) obj;
		return Objects.equals(author, other.author) && Objects.equals(editor, other.editor)
				&& Objects.equals(id, other.id) && Objects.equals(name, other.name)
				&& Objects.equals(release, other.release);
	}
	
	
}
