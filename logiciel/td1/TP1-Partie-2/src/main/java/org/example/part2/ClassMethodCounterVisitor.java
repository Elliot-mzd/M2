package org.example.part2;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.HashMap;
import java.util.Map;

public class ClassMethodCounterVisitor extends ASTVisitor {
    private Map<String, Integer> methodesParClasse = new HashMap<>();
    private Map<String, Integer> attributsParClasse = new HashMap<>();
    private String currentClassName;

    @Override
    public boolean visit(TypeDeclaration node) {
        currentClassName = node.getName().getIdentifier();
        methodesParClasse.put(currentClassName, 0); // Initialiser le compteur de méthodes à 0
        attributsParClasse.put(currentClassName, 0); // Initialiser le compteur d'attributs à 0
        return super.visit(node);
    }

    @Override
    public boolean visit(MethodDeclaration node) {
        // Incrémenter le nombre de méthodes pour la classe courante
        methodesParClasse.put(currentClassName, methodesParClasse.get(currentClassName) + 1);
        return super.visit(node);
    }

    @Override
    public boolean visit(FieldDeclaration node) {
        // Incrémenter le nombre d'attributs pour la classe courante
        attributsParClasse.put(currentClassName, attributsParClasse.get(currentClassName) + 1);
        return super.visit(node);
    }

    public Map<String, Integer> getMethodesParClasse() {
        return methodesParClasse;
    }

    public Map<String, Integer> getAttributsParClasse() {
        return attributsParClasse;
    }
}