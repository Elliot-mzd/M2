package org.example.part2;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.PackageDeclaration;

import java.util.HashSet;
import java.util.Set;

public class PackageDeclarationVisitor extends ASTVisitor {
    Set<String> packages = new HashSet<>();

    @Override
    public boolean visit(PackageDeclaration node) {
        packages.add(node.getName().getFullyQualifiedName());
        return super.visit(node);
    }

    public Set<String> getPackages() {
        return packages;
    }
}
