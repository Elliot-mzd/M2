package org.example.part2;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ClassDeclarationVisitor extends ASTVisitor {
    private final List<TypeDeclaration> classes = new ArrayList<>();

    @Override
    public boolean visit(TypeDeclaration node) {
        classes.add(node);
        return super.visit(node);
    }

    public List<TypeDeclaration> getClasses() {
        return classes;
    }
}