package org.example.visitor;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class MethodCallVisitor extends ASTVisitor {
    private List<MethodInvocation> methodInvocations = new ArrayList<>();

    @Override
    public boolean visit(MethodInvocation node) {
        methodInvocations.add(node);
        return super.visit(node);
    }

    public List<MethodInvocation> getMethodInvocations() {
        return methodInvocations;
    }
}