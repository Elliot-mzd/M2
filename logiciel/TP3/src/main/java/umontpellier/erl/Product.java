package umontpellier.erl;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class Product {
    private String id;

    private String name;

    private double price;

    private LocalDate expirationDate;

    public Product(String id, String name, double price, LocalDate expirationDate) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.expirationDate = expirationDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        return (((((((((((((("Product { \n" + "id='") + id) + '\'') + "\n") + "name='") + name) + '\'') + "\n") + "price=") + price) + "\n") + "expirationDate=") + expirationDate) + "\n") + " }  \n";
    }

    private static final Logger logger = LoggerFactory.getLogger(Product.class);
}
