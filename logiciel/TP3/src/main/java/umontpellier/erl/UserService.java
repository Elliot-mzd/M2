package umontpellier.erl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
public class UserService {
    private MongoCollection<Document> userCollection;

    public UserService() {
        MongoDatabase database = MongoDBConnection.getDatabase();
        userCollection = database.getCollection("User");
    }

    public void addUser(User user) throws Exception {
        try {
            Document doc = new Document("_id", user.getId()).append("name", user.getName()).append("age", user.getAge()).append("email", user.getEmail()).append("password", user.getPassword());
            userCollection.insertOne(doc);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'ajout de l'utilisateur: " + e.getMessage());
        }
    }

    public User getUser(String id) throws Exception {
        Document query = new Document("_id", id);
        Document doc = userCollection.find(query).first();
        if (doc == null) {
            throw new Exception("Utilisateur non trouvé.");
        }
        return new User(doc.getString("_id"), doc.getString("name"), doc.getInteger("age"), doc.getString("email"), doc.getString("password"));
    }

    public void deleteUser(String id) throws Exception {
        Document query = new Document("_id", id);
        if (userCollection.deleteOne(query).getDeletedCount() == 0) {
            throw new Exception("Utilisateur non trouvé.");
        }
    }

    public void updateUser(User user) throws Exception {
        Document query = new Document("_id", user.getId());
        Document update = new Document("$set", new Document("name", user.getName()).append("age", user.getAge()).append("email", user.getEmail()).append("password", user.getPassword()));
        if (userCollection.updateOne(query, update).getMatchedCount() == 0) {
            throw new Exception("Utilisateur non trouvé.");
        }
    }

    public void displayUsers() {
        for (Document doc : userCollection.find()) {
            System.out.println(new User(doc.getString("_id"), doc.getString("name"), doc.getInteger("age"), doc.getString("email"), doc.getString("password")));
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
}
