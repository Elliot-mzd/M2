package org.example.part2;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import java.util.ArrayList;
import java.util.List;

public class MethodDeclarationVisitor extends ASTVisitor {
    List<MethodDeclaration> methodes = new ArrayList<>();

    @Override
    public boolean visit(MethodDeclaration node) {
        methodes.add(node);
        return super.visit(node);
    }

    public List<MethodDeclaration> getMethodes() {
        return methodes;
    }
}
