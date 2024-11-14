package org.example.visitor;


import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.ArrayList;
import java.util.List;

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