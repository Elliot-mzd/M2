package org.example.CallGraph;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import java.util.ArrayList;
import java.util.List;

class MethodDeclarationVisitor extends ASTVisitor {
    private List<MethodDeclaration> methods = new ArrayList<>();

    @Override
    public boolean visit(MethodDeclaration node) {
        methods.add(node);
        return super.visit(node);
    }

    public List<MethodDeclaration> getMethods() {
        return methods;
    }
}

class MethodInvocationVisitor extends ASTVisitor {
    private List<MethodInvocation> methods = new ArrayList<>();

    @Override
    public boolean visit(MethodInvocation node) {
        methods.add(node);
        return super.visit(node);
    }

    public List<MethodInvocation> getMethods() {
        return methods;
    }
}

class VariableDeclarationFragmentVisitor extends ASTVisitor {
    private List<VariableDeclarationFragment> variables = new ArrayList<>();

    @Override
    public boolean visit(VariableDeclarationFragment node) {
        variables.add(node);
        return super.visit(node);
    }

    public List<VariableDeclarationFragment> getVariables() {
        return variables;
    }
}
