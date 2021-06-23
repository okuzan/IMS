package product;

public class Product {
    private Integer id;
    private Integer category;
    private String name;
    private String description;
    private String producer;
    private Double amount;
    private Double price;


    public Product(){

    }
    public Product(final Integer id, Integer category, String name, String description, String producer, Double amount, Double price) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.description = description;
        this.producer = producer;
        this.amount = amount;
        this.price = price;
    }
    public Product(Integer category, String name, String description, String producer, Double amount, Double price) {
        this.category = category;
        this.name = name;
        this.description = description;
        this.producer = producer;
        this.amount = amount;
        this.price = price;
    }

//    public Product(String name, Double amount, Double price) {
//        this.id = null;
//        this.name = name;
//        this.amount = amount;
//        this.price = price;
//    }

    public void add(double amount) {
        this.amount += amount;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setId(int anInt) {
        this.id = anInt;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", producer='" + producer + '\'' +
                ", amount=" + amount +
                ", price=" + price +
                '}';
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public Integer getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Double.compare(product.amount, amount) == 0 && Double.compare(product.price, price) == 0 && id.equals(product.id) && name.equals(product.name);
    }

}
