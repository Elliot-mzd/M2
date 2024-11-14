package umontpellier.erl;

import java.time.LocalDate;
import java.util.Scanner;

public class ApplicationCLI {
    private static ProductService productService = new ProductService();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Menu:");
            System.out.println("1. Ajouter un produit");
            System.out.println("2. Afficher les produits");
            System.out.println("3. Rechercher un produit par ID");
            System.out.println("4. Supprimer un produit par ID");
            System.out.println("5. Mettre à jour un produit");
            System.out.println("6. Quitter");
            System.out.print("Choisissez une option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (choice) {
                    case 1:
                        addProduit(scanner);
                        break;
                    case 2:
                        productService.displayProducts();
                        break;
                    case 3:
                        searchProductById(scanner);
                        break;
                    case 4:
                        deleteProductById(scanner);
                        break;
                    case 5:
                        updateProductById(scanner);
                        break;
                    case 6:
                        exit = true;
                        break;
                    default:
                        System.out.println("Option non valide.");
                }
            } catch (Exception e) {
                System.out.println("Erreur: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static void addProduit(Scanner scanner) {
        System.out.print("Entrez l'ID du produit: ");
        String id = scanner.nextLine();
        System.out.print("Entrez le nom du produit: ");
        String name = scanner.nextLine();
        System.out.print("Entrez le prix du produit: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // Consomme la nouvelle ligne
        System.out.print("Entrez la date d'expiration du produit (YYYY-MM-DD): ");
        String expirationDateStr = scanner.nextLine();
        LocalDate expirationDate = LocalDate.parse(expirationDateStr);

        Product product = new Product(id, name, price, expirationDate);
        try {
            productService.addProduct(product);
            System.out.println("Produit ajouté avec succès.");
        } catch (Exception e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    private static void searchProductById(Scanner scanner) {
        System.out.print("Entrez l'ID du produit: ");
        String id = scanner.nextLine();
        try {
            Product product = productService.getProduct(id);
            System.out.println("Produit trouvé: " + product);
        } catch (Exception e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    private static void deleteProductById(Scanner scanner) {
        System.out.print("Entrez l'ID du produit: ");
        String id = scanner.nextLine();
        try {
            productService.deleteProduct(id);
            System.out.println("Produit supprimé avec succès.");
        } catch (Exception e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    private static void updateProductById(Scanner scanner) {
    System.out.print("Entrez l'ID du produit: ");
    String id = scanner.nextLine();
    try {
        Product product = productService.getProduct(id);
        System.out.print("Entrez le nouveau nom du produit: ");
        String name = scanner.nextLine();
        System.out.print("Entrez le nouveau prix du produit: ");
        double price = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Entrez la nouvelle date d'expiration du produit (YYYY-MM-DD): ");
        String expirationDateStr = scanner.nextLine();
        LocalDate expirationDate = LocalDate.parse(expirationDateStr);

        product.setName(name);
        product.setPrice(price);
        product.setExpirationDate(expirationDate);

        productService.updateProduct(product);
        System.out.println("Produit mis à jour avec succès.");
    } catch (Exception e) {
        System.out.println("Erreur: " + e.getMessage());
    }
}
}