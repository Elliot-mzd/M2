package umontpellier.erl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.time.LocalDate;
public class ProductService {
    private MongoCollection<Document> productCollection;

    public ProductService() {
        MongoDatabase database = MongoDBConnection.getDatabase();
        productCollection = database.getCollection("Product");
    }

    public void addProduct(Product product) throws Exception {
        Document doc = new Document("id", product.getId())
                .append("name", product.getName())
                .append("price", product.getPrice())
                .append("expirationDate", product.getExpirationDate().toString());
        productCollection.insertOne(doc);
    }

    public Product getProduct(String id) throws Exception {
        Document query = new Document("id", id);
        Document doc = productCollection.find(query).first();
        if (doc == null) {
            throw new Exception("Produit non trouvé.");
        }
        return new Product(doc.getString("id"), doc.getString("name"), doc.getDouble("price"), LocalDate.parse(doc.getString("expirationDate")));
    }

    public void deleteProduct(String id) throws Exception {
        Document query = new Document("id", id);
        if (productCollection.deleteOne(query).getDeletedCount() == 0) {
            throw new Exception("Produit non trouvé.");
        }
    }

    public void updateProduct(Product product) throws Exception {
        Document query = new Document("id", product.getId());
        Document update = new Document("$set", new Document("name", product.getName())
                .append("price", product.getPrice())
                .append("expirationDate", product.getExpirationDate().toString()));
        if (productCollection.updateOne(query, update).getMatchedCount() == 0) {
            throw new Exception("Produit non trouvé.");
        }
    }

    public void displayProducts() {
        for (Document doc : productCollection.find()) {
            System.out.println(new Product(doc.getString("id"), doc.getString("name"), doc.getDouble("price"), LocalDate.parse(doc.getString("expirationDate"))));
        }
    }
}
