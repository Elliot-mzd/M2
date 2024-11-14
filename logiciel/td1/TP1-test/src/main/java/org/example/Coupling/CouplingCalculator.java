package org.example.Coupling;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.example.visitor.MethodCallVisitor;

public class CouplingCalculator {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Entrez le nom de la première classe (par défaut: Dump.Dump1): ");
        String class1Name = scanner.nextLine().trim();
        if (class1Name.isEmpty()) {
            class1Name = "org.example.Dump.Dump1";
        } else {
            class1Name = "org.example." + class1Name;
        }

        System.out.println("Entrez le nom de la deuxième classe (par défaut: Dump.Dump2): ");
        String class2Name = scanner.nextLine().trim();
        if (class2Name.isEmpty()) {
            class2Name = "org.example.Dump.Dump2";
        } else {
            class2Name = "org.example." + class2Name;
        }

        String class1Path = "src/main/java/" + class1Name.replace('.', '/') + ".java";
        String class2Path = "src/main/java/" + class2Name.replace('.', '/') + ".java";

        String class1Source = new String(Files.readAllBytes(Paths.get(class1Path)));
        String class2Source = new String(Files.readAllBytes(Paths.get(class2Path)));

        Class<?> class1 = Class.forName(class1Name);
        Class<?> class2 = Class.forName(class2Name);

        int observedRelations = calculateRelations(class1, class2, class1Source, class2Source);
        int totalRelationsPossible = calculateTotalRelations(class1, class2);
        double couplingMetric = (double) observedRelations / totalRelationsPossible;

        System.out.println("Nombre total de relations binaires possibles entre " + class1.getSimpleName() + " et " + class2.getSimpleName() + " : " + totalRelationsPossible);
        System.out.println("Nombre de relations observées entre " + class1.getSimpleName() + " et " + class2.getSimpleName() + " : " + observedRelations);
        System.out.println("Couplage entre " + class1.getSimpleName() + " et " + class2.getSimpleName() + " : " + couplingMetric);
    }

    public static int calculateRelations(Class<?> class1, Class<?> class2, String class1Source, String class2Source) throws Exception {
        Method[] methodsClass1 = class1.getDeclaredMethods();
        Method[] methodsClass2 = class2.getDeclaredMethods();

        int observedRelations = 0;

        observedRelations += countMethodCallsBetweenClasses(methodsClass1, class2Source, class2.getSimpleName());
        observedRelations += countMethodCallsBetweenClasses(methodsClass2, class1Source, class1.getSimpleName());
        return observedRelations;
    }

    public static int calculateTotalRelations(Class<?> class1, Class<?> class2) {
        Method[] methodsClass1 = class1.getDeclaredMethods();
        Method[] methodsClass2 = class2.getDeclaredMethods();

        return methodsClass1.length * methodsClass2.length;
    }

    private static int countMethodCallsBetweenClasses(Method[] methods, String classSource, String classname) {
        List<String> allRelations = new ArrayList<>();

        ASTParser parser = ASTParser.newParser(AST.JLS4);
        parser.setSource(classSource.toCharArray());
        CompilationUnit cu = (CompilationUnit) parser.createAST(null);

        MethodCallVisitor visitor = new MethodCallVisitor();
        cu.accept(visitor);

        List<MethodInvocation> methodInvocations = visitor.getMethodInvocations();

        for (Method method : methods) {
            String methodName = method.getName();

            String methodCall = "." + methodName + "(";

            for (MethodInvocation invocation : methodInvocations) {
                if (invocation.toString().contains(methodCall)) {
                    allRelations.add(methodName);
                }
            }
        }

        return allRelations.size();
    }

    public static double calculateCoupling(String class1Name, String class2Name) throws Exception {
        String class1Path = "src/main/java/org/example/" + class1Name.replace('.', '/') + ".java";
        String class2Path = "src/main/java/org/example/" + class2Name.replace('.', '/') + ".java";



        if (!Files.exists(Paths.get(class1Path))) {
            throw new IOException("File not found: " + class1Path);
        }
        if (!Files.exists(Paths.get(class2Path))) {
            throw new IOException("File not found: " + class2Path);
        }

        String class1Source = new String(Files.readAllBytes(Paths.get(class1Path)));
        String class2Source = new String(Files.readAllBytes(Paths.get(class2Path)));

        Class<?> class1 = Class.forName("org.example." + class1Name);
        Class<?> class2 = Class.forName("org.example." + class2Name);

        int observedRelations = calculateRelations(class1, class2, class1Source, class2Source);
        int totalRelationsPossible = calculateTotalRelations(class1, class2);
        return (double) observedRelations / totalRelationsPossible;
    }
}
