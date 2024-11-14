// src/main/java/org/example/Coupling/CouplingGraph.java
package org.example.Coupling;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CouplingGraph {
    public static void main(String[] args) throws Exception {
        // Retrieve all Java classes in the project directory
        Set<String> classes = getAllClasses("src/main/java");
        double totalCoupling = 0;
        Set<String> processedPairs = new HashSet<>();

        // Calculate and display coupling for all combinations of two classes
        for (String class1 : classes) {
            for (String class2 : classes) {
                if (!class1.equals(class2)) {
                    String pair1 = class1 + "->" + class2;
                    String pair2 = class2 + "->" + class1;

                    if (!processedPairs.contains(pair1) && !processedPairs.contains(pair2)) {
                        String formattedClass1 = convertToFullyQualifiedName(class1);
                        String formattedClass2 = convertToFullyQualifiedName(class2);

                        double couplingMetric = CouplingCalculator.calculateCoupling(formattedClass1, formattedClass2);
                        totalCoupling += couplingMetric;
                        if (couplingMetric > 0) {
                            System.out.printf("%s -> %s [coupling=%.4f]%n", formattedClass1, formattedClass2, couplingMetric);
                        }

                        processedPairs.add(pair1);
                        processedPairs.add(pair2);
                    }
                }
            }
        }

        System.out.println("Total coupling : " + totalCoupling);
    }

    private static Set<String> getAllClasses(String rootPath) throws IOException {
        try (Stream<String> paths = Files.walk(Paths.get(rootPath)).filter(Files::isRegularFile).map(path -> path.toString())) {
            return paths.filter(path -> path.endsWith(".java"))
                        .map(path -> path.replace(rootPath + "/", "").replace("/", ".").replace(".java", ""))
                        .collect(Collectors.toSet());
        }
    }

    private static String convertToFullyQualifiedName(String classPath) {
        return classPath.replace("src\\main\\java\\org\\example\\", "").replace("\\", ".").replace(".java", "");
    }
}