package de.seibushin.nutrigo.request;

public class FoodLink {
    private String name;
    private String link;
    private String img;

    public FoodLink(String name, String link, String img) {
        this.name = name;
        this.link = link;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public String getImg() {
        return img;
    }
}
