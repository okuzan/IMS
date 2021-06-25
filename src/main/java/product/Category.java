package product;

import java.io.Serializable;

public class Category implements Serializable {
    private String description;
    private String title;
    private Integer id;

    public Category(final int id, String title, String description) {
        this.id = id;
        this.description = description;
        this.title = title;
    }

    public Category(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String toFormat() {
        return id + "," + description + "," + title;
    }

    public static Category fromFormat(String s) {
        String[] splits = s.split(",");
        return new Category(Integer.parseInt(splits[0]), splits[1], splits[2]);
    }

    @Override
    public String toString() {
        return "Category{" +
                "description='" + description + '\'' +
                ", title='" + title + '\'' +
                ", id=" + id +
                '}';
    }
}
