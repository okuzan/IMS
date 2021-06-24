package product;

public class Category {
    private String description;
    private String title;
    private Integer id;

    public Category(final int id, String title, String description){
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

    public String parseCategory() {
        String str = new String();
        str += id + "," + description + "," + title;
        return str;
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
