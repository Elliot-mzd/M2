package umontpellier.erl;
import java.time.LocalDate;
import org.slf4j.Logger;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
public class ProductService {
    private MongoCollection<Document> productCollection;

    public ProductService() {
        MongoDatabase database = MongoDBConnection.getDatabase();
        productCollection = database.getCollection("Product");
    }

    public void addProduct(Product product) throws Exception {
        logger.info("Entering method: addProduct(umontpellier.erl.Product)");
        Document query = new Document("_id", product.getId());
        if (productCollection.find(query).first() != null) {
            logger.error("Exception thrown in method: addProduct(umontpellier.erl.Product) - " + new Exception("Produit avec cet ID existe déjà."));
            throw new Exception("Produit avec cet ID existe déjà.");
        }
        Document doc = new Document("_id", product.getId()).append("name", product.getName()).append("price", product.getPrice()).append("expirationDate", product.getExpirationDate().toString());
        productCollection.insertOne(doc);
    }

    public Product getProduct(String id) throws Exception {
        logger.info("Entering method: getProduct(java.lang.String)");
        Document query = new Document("_id", id);
        Document doc = productCollection.find(query).first();
        if (doc == null) {
            logger.error("Exception thrown in method: getProduct(java.lang.String) - " + new Exception("Produit non trouvé."));
            throw new Exception("Produit non trouvé.");
        }
        return new Product(doc.getString("_id"), doc.getString("name"), doc.getDouble("price"), LocalDate.parse(doc.getString("expirationDate")));
    }

    public void deleteProduct(String id) throws Exception {
        logger.info("Entering method: deleteProduct(java.lang.String)");
        Document query = new Document("_id", id);
        if (productCollection.find(query).first() == null) {
            logger.error("Exception thrown in method: deleteProduct(java.lang.String) - " + new Exception("Produit non trouvé."));
            throw new Exception("Produit non trouvé.");
        }
        productCollection.deleteOne(query);
    }

    public void updateProduct(Product product) throws Exception {
        logger.info("Entering method: updateProduct(umontpellier.erl.Product)");
        Document query = new Document("_id", product.getId());
        if (productCollection.find(query).first() == null) {
            logger.error("Exception thrown in method: updateProduct(umontpellier.erl.Product) - " + new Exception("Produit non trouvé."));
            throw new Exception("Produit non trouvé.");
        }
        Document update = new Document("$set", new Document("name", product.getName()).append("price", product.getPrice()).append("expirationDate", product.getExpirationDate().toString()));
        productCollection.updateOne(query, update);
    }

    public void displayProducts() {
        logger.info("Entering method: displayProducts()");
        for (Document doc : productCollection.find()) {
            System.out.println(new Product(doc.getString("_id"), doc.getString("name"), doc.getDouble("price"), LocalDate.parse(doc.getString("expirationDate"))));
        }
    }

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(umontpellier.erl.ProductService.class);
}
