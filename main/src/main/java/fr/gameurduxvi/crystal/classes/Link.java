package fr.gameurduxvi.crystal.classes;

import lombok.Getter;
import lombok.Setter;

public class Link implements Comparable<Link> {

    @Getter @Setter
    private String name;
    @Getter @Setter
    private String url;
    @Getter @Setter
    private byte[] icon;
    @Getter @Setter
    private boolean downloadable;
    @Getter @Setter
    private boolean installable;

    public Link(String name, String url, byte[] icon, boolean downloadable, boolean installable) {
        this.name = name;
        this.url = url;
        this.icon = icon;
        this.downloadable = downloadable;
        this.installable = installable;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Link link) {
        return this.getName().compareTo(link.getName());
    }
}
