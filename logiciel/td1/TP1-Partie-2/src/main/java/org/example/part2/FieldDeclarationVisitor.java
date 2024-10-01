package org.example.part2;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;

import java.util.ArrayList;
import java.util.List;

public class FieldDeclarationVisitor extends ASTVisitor {

    private List<FieldDeclaration> attributs = new ArrayList<>();

    @Override
    public boolean visit(FieldDeclaration node) {
        attributs.add(node);
        return super.visit(node);
    }

    public List<FieldDeclaration> getAttributs() {
        return attributs;
    }
}
