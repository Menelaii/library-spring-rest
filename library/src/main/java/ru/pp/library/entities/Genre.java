package ru.pp.library.entities;

import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(name = "Genre")
public class Genre extends AbstractEntity {
    
    @Size(min = 1, max = 120, message = "Название жанра должно быть больше 1 и меньше 120 символов")
    @NotEmpty(message = "Название жанра не должно быть пустым")
    @Column(name = "name", unique = true)
    private String name;

    @OneToMany(mappedBy = "genre", cascade = CascadeType.REMOVE)
    private List<Book> books;

    public Genre() {
    }

    public Genre(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
