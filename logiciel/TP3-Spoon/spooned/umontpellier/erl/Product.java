package umontpellier.erl;
import java.time.LocalDate;
import org.slf4j.Logger;
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
        logger.info("Entering method: getId()");
        return id;
    }

    public void setId(String id) {
        logger.info("Entering method: setId(java.lang.String)");
        this.id = id;
    }

    public String getName() {
        logger.info("Entering method: getName()");
        return name;
    }

    public void setName(String name) {
        logger.info("Entering method: setName(java.lang.String)");
        this.name = name;
    }

    public double getPrice() {
        logger.info("Entering method: getPrice()");
        return price;
    }

    public void setPrice(double price) {
        logger.info("Entering method: setPrice(double)");
        this.price = price;
    }

    public LocalDate getExpirationDate() {
        logger.info("Entering method: getExpirationDate()");
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        logger.info("Entering method: setExpirationDate(java.time.LocalDate)");
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        logger.info("Entering method: toString()");
        return (((((((((((((("Product { \n" + "id='") + id) + '\'') + "\n") + "name='") + name) + '\'') + "\n") + "price=") + price) + "\n") + "expirationDate=") + expirationDate) + "\n") + " }  \n";
    }

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(umontpellier.erl.Product.class);
}
