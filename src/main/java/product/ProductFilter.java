package product;

import java.io.Serializable;

public class ProductFilter implements Serializable {
    private Integer category;
    private String name;
    private String producer;
    private Double minPrice;
    private Double maxPrice;
    private Double minAmount;
    private Double maxAmount;

    public String toFormat() {
        return category + "," + name + "," + producer + "," + minPrice + "," + maxPrice + "," + minAmount + "," + maxAmount;
    }

    public ProductFilter() {
    }

    public ProductFilter(Integer category, String name, String producer, Double minAmount, Double maxAmount, Double minPrice, Double maxPrice) {
        this.category = category;
        this.name = name;
        this.producer = producer;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Double getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(Double minAmount) {
        this.minAmount = minAmount;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Double maxAmount) {
        this.maxAmount = maxAmount;
    }

    @Override
    public String toString() {
        return "ProductFilter{" +
                "category=" + category +
                ", name='" + name + '\'' +
                ", producer='" + producer + '\'' +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                ", minAmount=" + minAmount +
                ", maxAmount=" + maxAmount +
                '}';
    }
}
