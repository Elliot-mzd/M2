package umontpellier.erl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class UserProfile {
    private static final String PRODUCTS_COLLECTION = "Product";
    private static final String USERS_COLLECTION = "User";


    public static class UserStats {
        public int readCount = 0;
        public int writeCount = 0;
        public List<Integer> searchedProducts = new ArrayList<>();
    }

    private static MongoCollection<Document> getCollection(String collectionName) {
        MongoDatabase database = MongoDBConnection.getDatabase();
        return database.getCollection(collectionName);
    }

    public static Map<String, UserStats> analyzeLogs(Path logFilePath) throws IOException {
        Map<String, UserStats> userProfiles = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        List<String> lines = Files.readAllLines(logFilePath);

        for (String line : lines) {
            JsonNode logEntry = mapper.readTree(line);
            JsonNode messageNode = logEntry.get("message");
            JsonNode userIdNode = logEntry.get("userId");
            JsonNode actionNode = logEntry.get("action");
            JsonNode productIdNode = logEntry.get("productId");

            if (messageNode != null && userIdNode != null && actionNode != null) {
                String userId = userIdNode.asText();
                String action = actionNode.asText();

                UserStats stats = userProfiles.computeIfAbsent(userId, k -> new UserStats());

                if (action.equals("getProduct") && productIdNode != null) {
                    stats.readCount++;
                    int productId = productIdNode.asInt();

                    // Récupération du produit depuis MongoDB
                    Document productDoc = getCollection(PRODUCTS_COLLECTION).find(Filters.eq("_id", productId)).first();
                    if (productDoc != null) {
                        double price = productDoc.getDouble("price");
                        System.out.printf("Found Product ID: %d with Price: %.2f%n", productId, price);

                        // Ajout du produit aux produits recherchés si son prix est > 1000
                        if (price > 1000) {
                            stats.searchedProducts.add(productId);
                        }
                    } else {
                        System.out.printf("Product ID: %d not found in the database%n", productId);
                    }

            } else if (action.equals("displayProducts")) {
                    stats.readCount++;
                } else if (action.equals("addProduct") || action.equals("updateProduct") || action.equals("deleteProduct")) {
                    stats.writeCount++;
                }
            }
        }

        return userProfiles;
    }

    // Récupérer des informations sur l'utilisateur depuis MongoDB
    private static String getUserInfo(String userId) {
        MongoCollection<Document> usersCollection = getCollection(USERS_COLLECTION);
        Document userDoc = usersCollection.find(Filters.eq("_id", userId)).first();

        if (userDoc != null) {
            String name = userDoc.getString("name");
            String email = userDoc.getString("email");
            return String.format("Name: %s, Email: %s", name, email);
        }

        return "User info not found";
    }

    // Récupérer les produits les plus chers depuis MongoDB
    private static List<String> getExpensiveProducts(List<Integer> productIds) {
        MongoCollection<Document> productsCollection = getCollection(PRODUCTS_COLLECTION);
        List<String> expensiveProducts = new ArrayList<>();

        for (Integer productId : productIds) {
            Document productDoc = productsCollection.find(Filters.eq("_id", productId)).first();
            if (productDoc != null) {
                String productName = productDoc.getString("name");
                double price = productDoc.getDouble("price");

                if (price > 1000) {  // Critère pour les produits chers, à ajuster
                    expensiveProducts.add(String.format("Product: %s, Price: %.2f", productName, price));
                }
            }
        }
        return expensiveProducts;
    }

    // Profil : utilisateurs qui ont principalement effectué des lectures
    public static List<String> getMostlyReadUsers(Map<String, UserStats> userProfiles) {
        return userProfiles.entrySet().stream()
                .filter(entry -> entry.getValue().readCount > entry.getValue().writeCount)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    // Profil : utilisateurs qui ont principalement effectué des écritures
    public static List<String> getMostlyWriteUsers(Map<String, UserStats> userProfiles) {
        return userProfiles.entrySet().stream()
                .filter(entry -> entry.getValue().writeCount > entry.getValue().readCount)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    // Profil : utilisateurs qui ont cherché les produits les plus chers

    public static List<String> getUsersSearchedExpensiveProducts(Map<String, UserStats> userProfiles) {
        MongoCollection<Document> productsCollection = getCollection(PRODUCTS_COLLECTION);
        List<String> usersWithExpensiveSearches = new ArrayList<>();

        for (Map.Entry<String, UserStats> entry : userProfiles.entrySet()) {
            String userId = entry.getKey();
            UserStats stats = entry.getValue();

            // Récupérer les produits dans une seule requête en batch pour éviter les multiples appels à MongoDB
            List<Integer> productIds = stats.searchedProducts;
            List<Document> expensiveProducts = productsCollection
                    .find(Filters.and(
                            Filters.in("_id", productIds),
                            Filters.gt("price", 1000))) // Filtrer les produits chers
                    .into(new ArrayList<>());

            if (!expensiveProducts.isEmpty()) {
                List<String> productDetails = expensiveProducts.stream()
                        .map(doc -> String.format("Product: %s, Price: %.2f",
                                doc.getString("name"), doc.getDouble("price")))
                        .collect(Collectors.toList());

                String userInfo = getUserInfo(userId);
                usersWithExpensiveSearches.add(String.format("User ID: %s, %s, Searched Products: %s",
                        userId, userInfo, productDetails));
            }
        }

        return usersWithExpensiveSearches;
    }


    // Méthode pour afficher les profils
    public static void printUserProfiles(Map<String, UserStats> userProfiles) {
        for (Map.Entry<String, UserStats> entry : userProfiles.entrySet()) {
            String userId = entry.getKey();
            UserStats stats = entry.getValue();
            String userInfo = getUserInfo(userId);
            System.out.printf("User %s: %s, Reads=%d, Writes=%d, SearchedProducts=%s%n",
                    userId, userInfo, stats.readCount, stats.writeCount, stats.searchedProducts);
        }
    }

    public static void main(String[] args) throws IOException {
        // Specify the correct path to the log file
        Path logFilePath = Paths.get("logs/application.json");
        if (!Files.exists(logFilePath)) {
            throw new FileNotFoundException("Log file 'application.json' not found in logs directory");
        }

        // Analyze the logs and print user profiles
        Map<String, UserStats> userProfiles = analyzeLogs(logFilePath);
        printUserProfiles(userProfiles);

        // Retrieve and print users who searched for expensive products
        List<String> usersWithExpensiveProducts = getUsersSearchedExpensiveProducts(userProfiles);
        System.out.println("Users who searched for expensive products:");
        usersWithExpensiveProducts.forEach(System.out::println);

        // Retrieve and print users who mostly performed read operations
        List<String> mostlyReadUsers = getMostlyReadUsers(userProfiles);
        System.out.println("Users who mostly performed read operations:");
        mostlyReadUsers.forEach(System.out::println);

        // Retrieve and print users who mostly performed write operations
        List<String> mostlyWriteUsers = getMostlyWriteUsers(userProfiles);
        System.out.println("Users who mostly performed write operations:");
        mostlyWriteUsers.forEach(System.out::println);
    }
}