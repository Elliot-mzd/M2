package org.example.step2;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class TypeDeclarationVisitor extends ASTVisitor {
	List<TypeDeclaration> types = new ArrayList<TypeDeclaration>();
	
	public boolean visit(TypeDeclaration node) {
		types.add(node);
		return super.visit(node);
	}
	
	public List<TypeDeclaration> getTypes() {
		return types;
	}
}
