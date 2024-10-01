package org.example.part2;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Parser {

    public static final String projectPath = "/home/e20190006130/Documents/M2/logiciel/td1/TP1-test/";
    public static final String projectSourcePath = projectPath + "/src";
    public static final String jrePath = "/home/e20190006130/.jdks/corretto-18.0.2";

    public static void main(String[] args) throws IOException {

        // Lire les fichiers Java
        final File dossier = new File(projectSourcePath);
        ArrayList<File> fichiersJava = listerFichiersJava(dossier);

        int nombreDeClasses = 0;
        int nombreTotalDeLignesDeCode = 0;
        int nombreTotalDeMethodes = 0;
        int nombreTotalDAttributs = 0;
        Set<String> packagesSet = new HashSet<>();
        Map<String, Integer> methodesParClasse = new HashMap<>(); // Suivi des méthodes par classe
        Map<String, Integer> attributsParClasse = new HashMap<>(); // Suivi des attributs par classe

        for (File fichier : fichiersJava) {
            String contenu = FileUtils.readFileToString(fichier, "UTF-8");
            CompilationUnit parse = parse(contenu.toCharArray());

            // Compter les classes, méthodes et attributs
            ClassMethodCounterVisitor visiteur = new ClassMethodCounterVisitor();
            parse.accept(visiteur);
            for (Map.Entry<String, Integer> entry : visiteur.getMethodesParClasse().entrySet()) {
                methodesParClasse.put(entry.getKey(), entry.getValue());
                nombreDeClasses++;
                nombreTotalDeMethodes += entry.getValue();
            }
            for (Map.Entry<String, Integer> entry : visiteur.getAttributsParClasse().entrySet()) {
                attributsParClasse.put(entry.getKey(), entry.getValue());
                nombreTotalDAttributs += entry.getValue();
            }

            // Compter les lignes de code
            nombreTotalDeLignesDeCode += compterLesLignesDeCode(contenu);

            // Compter les packages
            packagesSet.addAll(compterLesPackages(parse));
        }

        // Calcul du nombre moyen de méthodes par classe
        double nombreMoyenDeMethodesParClasse = (nombreDeClasses > 0) ? (double) nombreTotalDeMethodes / nombreDeClasses : 0.0;

        // Calcul du nombre moyen d'attributs par classe
        double nombreMoyenDAttributsParClasse = (nombreDeClasses > 0) ? (double) nombreTotalDAttributs / nombreDeClasses : 0.0;

        // Calcul du nombre moyen de lignes de code par méthode
        double nombreMoyenDeLignesParMethode = (nombreTotalDeMethodes > 0) ? (double) nombreTotalDeLignesDeCode / nombreTotalDeMethodes : 0.0;

        // Affichage des résultats
        System.out.println("\n======= Résumé du projet =======");
        System.out.println("Chemin du projet : " + projectPath);
        System.out.println("Nombre total de fichiers Java analysés : " + fichiersJava.size());
        System.out.println("-------------------------------------");
        System.out.println("Nombre total de classes : " + nombreDeClasses);
        System.out.println("Nombre total de lignes de code : " + nombreTotalDeLignesDeCode);
        System.out.println("Nombre total de méthodes : " + nombreTotalDeMethodes);
        System.out.println("Nombre total de packages : " + packagesSet.size());
        System.out.println("Nombre moyen de méthodes par classe : " + String.format("%.2f", nombreMoyenDeMethodesParClasse));
        System.out.println("Nombre moyen d'attributs par classe : " + String.format("%.2f", nombreMoyenDAttributsParClasse));
        System.out.println("Nombre moyen de lignes de code par méthode : " + String.format("%.2f", nombreMoyenDeLignesParMethode));
        System.out.println("=====================================");

        // Affichage des 10% des classes ayant le plus grand nombre de méthodes
        List<String> top10PourcentMethodes = afficherTop10PourcentClasses(methodesParClasse, "méthodes");

        // Affichage des 10% des classes ayant le plus grand nombre d'attributs
        List<String> top10PourcentAttributs = afficherTop10PourcentClasses(attributsParClasse, "attributs");

        // Affichage des classes qui font partie des deux listes
        afficherClassesCommunes(top10PourcentMethodes, top10PourcentAttributs);
    }

    // Lire tous les fichiers Java dans le dossier spécifié
    public static ArrayList<File> listerFichiersJava(final File dossier) {
        ArrayList<File> fichiersJava = new ArrayList<>();
        File[] fichiers = dossier.listFiles();
        if (fichiers != null) {
            for (File fichier : fichiers) {
                if (fichier.isDirectory()) {
                    fichiersJava.addAll(listerFichiersJava(fichier));
                } else if (fichier.getName().endsWith(".java")) {
                    fichiersJava.add(fichier);
                }
            }
        } else {
            System.err.println("Le dossier " + dossier.getAbsolutePath() + " n'existe pas ou n'est pas un répertoire.");
        }
        return fichiersJava;
    }

    // Créer l'AST
    private static CompilationUnit parse(char[] sourceClasse) {
        ASTParser parser = ASTParser.newParser(AST.JLS4); // Support pour Java 1.6+
        parser.setResolveBindings(true);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);

        parser.setBindingsRecovery(true);

        Map options = JavaCore.getOptions();
        parser.setCompilerOptions(options);

        parser.setUnitName("");

        String[] sources = { projectSourcePath };
        String[] classpath = { jrePath };

        parser.setEnvironment(classpath, sources, new String[] { "UTF-8" }, true);
        parser.setSource(sourceClasse);

        return (CompilationUnit) parser.createAST(null); // Créer et parser l'AST
    }

    // Afficher les 10% des classes avec le plus grand nombre de méthodes ou d'attributs
    private static List<String> afficherTop10PourcentClasses(Map<String, Integer> elementsParClasse, String type) {
        // Trier les classes par nombre d'éléments (méthodes ou attributs)
        List<Map.Entry<String, Integer>> listeTriee = new ArrayList<>(elementsParClasse.entrySet());
        listeTriee.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        // Calculer les 10% des classes
        int top10Pourcent = (int) Math.ceil(listeTriee.size() * 0.10);

        System.out.println("\n===== Top 10% des classes avec le plus grand nombre de " + type + " =====");
        List<String> topClasses = new ArrayList<>();
        for (int i = 0; i < top10Pourcent; i++) {
            Map.Entry<String, Integer> entry = listeTriee.get(i);
            System.out.println("Classe : " + entry.getKey() + " | Nombre de " + type + " : " + entry.getValue());
            topClasses.add(entry.getKey());
        }
        return topClasses;
    }

    // Afficher les classes qui font partie des deux listes
    private static void afficherClassesCommunes(List<String> topMethodes, List<String> topAttributs) {
        Set<String> communes = new HashSet<>(topMethodes);
        communes.retainAll(topAttributs);

        System.out.println("\n===== Classes dans le top 10% pour les méthodes et les attributs =====");
        for (String classe : communes) {
            System.out.println("Classe : " + classe);
        }
    }

    // Compter les déclarations d'attributs
    private static int compterLesAttributs(CompilationUnit parse) {
        FieldDeclarationVisitor visiteur = new FieldDeclarationVisitor();
        parse.accept(visiteur);
        return visiteur.getAttributs().size();
    }

    // Compter les déclarations de package
    private static Set<String> compterLesPackages(CompilationUnit parse) {
        PackageDeclarationVisitor visiteur = new PackageDeclarationVisitor();
        parse.accept(visiteur);
        return visiteur.getPackages();
    }

    // Compter les lignes de code d'un fichier source
    private static int compterLesLignesDeCode(String contenu) {
        // Diviser le contenu du fichier en lignes
        String[] lignes = contenu.split("\n");

        int nombreDeLignes = 0;
        for (String ligne : lignes) {
            // Supprimer les espaces blancs et vérifier si la ligne est vide ou un commentaire
            String ligneTrimmee = ligne.trim();
            if (!ligneTrimmee.isEmpty() && !ligneTrimmee.startsWith("//") && !ligneTrimmee.startsWith("/*") && !ligneTrimmee.startsWith("*")) {
                nombreDeLignes++;
            }
        }
        return nombreDeLignes;
    }
}