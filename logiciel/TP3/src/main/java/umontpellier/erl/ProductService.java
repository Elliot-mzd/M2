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
        org.slf4j.MDC.put("userId", UserSession.getInstance().getCurrentUser().getId());org.slf4j.MDC.put("action", "addProduct");org.slf4j.MDC.put("productId", String.valueOf(product.getId()));;
        logger.info("Entered method: addProduct");
        Document query = new Document("_id", product.getId());
        if (productCollection.find(query).first() != null) {
            logger.error("Encountered an error in method: addProduct - Exception: " + new Exception("Produit avec cet ID existe déjà."));
            throw new Exception("Produit avec cet ID existe déjà.");
        }
        Document doc = new Document("_id", product.getId()).append("name", product.getName()).append("price", product.getPrice()).append("expirationDate", product.getExpirationDate().toString());
        productCollection.insertOne(doc);
        logger.info("Exited method: addProduct");
        org.slf4j.MDC.clear();
    }

    public void deleteProduct(String id) throws Exception {
        org.slf4j.MDC.put("userId", UserSession.getInstance().getCurrentUser().getId());org.slf4j.MDC.put("action", "deleteProduct");org.slf4j.MDC.put("productId", String.valueOf(id));;
        logger.info("Entered method: deleteProduct");
        Document query = new Document("_id", id);
        if (productCollection.find(query).first() == null) {
            logger.error("Encountered an error in method: deleteProduct - Exception: " + new Exception("Produit non trouvé."));
            throw new Exception("Produit non trouvé.");
        }
        productCollection.deleteOne(query);
        logger.info("Exited method: deleteProduct");
        org.slf4j.MDC.clear();
    }

    public Product getProduct(String id) throws Exception {
        org.slf4j.MDC.put("userId", UserSession.getInstance().getCurrentUser().getId());org.slf4j.MDC.put("action", "getProduct");org.slf4j.MDC.put("productId", String.valueOf(id));;
        logger.info("Entered method: getProduct");
        Document query = new Document("_id", id);
        Document doc = productCollection.find(query).first();
        if (doc == null) {
            logger.error("Encountered an error in method: getProduct - Exception: " + new Exception("Produit non trouvé."));
            throw new Exception("Produit non trouvé.");
        }
        Product product = new Product(doc.getString("_id"), doc.getString("name"), doc.getDouble("price"), LocalDate.parse(doc.getString("expirationDate")));
        logger.info("Exited method: getProduct");
        org.slf4j.MDC.clear();
        return product;
    }

    public void updateProduct(Product product) throws Exception {
        org.slf4j.MDC.put("userId", UserSession.getInstance().getCurrentUser().getId());org.slf4j.MDC.put("action", "updateProduct");org.slf4j.MDC.put("productId", String.valueOf(product.getId()));;
        logger.info("Entered method: updateProduct");
        Document query = new Document("_id", product.getId());
        if (productCollection.find(query).first() == null) {
            logger.error("Encountered an error in method: updateProduct - Exception: " + new Exception("Produit non trouvé."));
            throw new Exception("Produit non trouvé.");
        }
        Document update = new Document("$set", new Document("name", product.getName()).append("price", product.getPrice()).append("expirationDate", product.getExpirationDate().toString()));
        productCollection.updateOne(query, update);
        logger.info("Exited method: updateProduct");
        org.slf4j.MDC.clear();
    }

    public void displayProducts() {
        org.slf4j.MDC.put("userId", UserSession.getInstance().getCurrentUser().getId());org.slf4j.MDC.put("action", "displayProducts");;
        logger.info("Entered method: displayProducts");
        for (Document doc : productCollection.find()) {
            System.out.println(new Product(doc.getString("_id"), doc.getString("name"), doc.getDouble("price"), LocalDate.parse(doc.getString("expirationDate"))));
        }
        logger.info("Exited method: displayProducts");
        org.slf4j.MDC.clear();
    }

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(umontpellier.erl.ProductService.class);
}
