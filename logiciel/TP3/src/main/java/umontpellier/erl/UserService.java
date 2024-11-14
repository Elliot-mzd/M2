package umontpellier.erl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class UserService {
    private MongoCollection<Document> userCollection;

    public UserService() {
        MongoDatabase database = MongoDBConnection.getDatabase();
        userCollection = database.getCollection("users");
    }

    public void addUser(User user) throws Exception {
        Document doc = new Document("id", user.getId())
                .append("name", user.getName())
                .append("age", user.getAge())
                .append("email", user.getEmail())
                .append("password", user.getPassword());
        userCollection.insertOne(doc);
    }

    public User getUser(String id) throws Exception {
        Document query = new Document("id", id);
        Document doc = userCollection.find(query).first();
        if (doc == null) {
            throw new Exception("Utilisateur non trouvé.");
        }
        return new User(doc.getString("id"), doc.getString("name"), doc.getInteger("age"), doc.getString("email"), doc.getString("password"));
    }

    public void deleteUser(String id) throws Exception {
        Document query = new Document("id", id);
        if (userCollection.deleteOne(query).getDeletedCount() == 0) {
            throw new Exception("Utilisateur non trouvé.");
        }
    }

    public void updateUser(User user) throws Exception {
        Document query = new Document("id", user.getId());
        Document update = new Document("$set", new Document("name", user.getName())
                .append("age", user.getAge())
                .append("email", user.getEmail())
                .append("password", user.getPassword()));
        if (userCollection.updateOne(query, update).getMatchedCount() == 0) {
            throw new Exception("Utilisateur non trouvé.");
        }
    }

    public void displayUsers() {
        for (Document doc : userCollection.find()) {
            System.out.println(new User(doc.getString("id"), doc.getString("name"), doc.getInteger("age"), doc.getString("email"), doc.getString("password")));
        }
    }
}