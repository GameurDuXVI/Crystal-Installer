package fr.gameurduxvi.crystal.classes;

import lombok.Getter;
import lombok.Setter;

import java.util.TreeSet;

public class Category implements Comparable<Category> {

    @Getter @Setter
    private String name;
    @Getter
    private TreeSet<Link> links = new TreeSet<>();

    public Category(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Category category) {
        return this.getName().compareTo(category.getName());
    }
}
