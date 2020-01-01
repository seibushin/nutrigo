package de.seibushin.nutrigo.request;

public class FoodLink {
    private String name;
    private String link;

    public FoodLink(String name, String link) {
        this.name = name;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }
}
