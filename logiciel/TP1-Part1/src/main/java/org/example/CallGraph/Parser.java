package org.example.CallGraph;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;
import org.example.step2.MethodDeclarationVisitor;
import org.example.step2.MethodInvocationVisitor;
import org.example.step2.VariableDeclarationFragmentVisitor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class Parser {

    public static final String projectPath = "/home/e20190006130/Documents/M2/logiciel/td1/TP1-test/";
    public static final String projectSourcePath = projectPath + "/src";
    public static final String jrePath = "/home/e20190006130/.jdks/corretto-18.0.2";
    private static CallGraph callGraph = new CallGraph();

    public static void main(String[] args) throws IOException {
        // read java files
        final File folder = new File(projectSourcePath);
        ArrayList<File> javaFiles = listJavaFilesForFolder(folder);

        for (File fileEntry : javaFiles) {
            String content = FileUtils.readFileToString(fileEntry);
            CompilationUnit parse = parse(content.toCharArray());

            // print methods info
            printMethodInfo(parse);

            // print variables info
            printVariableInfo(parse);

            // print method invocations
            printMethodInvocationInfo(parse);
        }

        // Print the call graph
        printCallGraph();
        callGraph.writeDotFile("callgraph.dot");
    }

    // read all java files from specific folder
    public static ArrayList<File> listJavaFilesForFolder(final File folder) {
        ArrayList<File> javaFiles = new ArrayList<File>();
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                javaFiles.addAll(listJavaFilesForFolder(fileEntry));
            } else if (fileEntry.getName().contains(".java")) {
                javaFiles.add(fileEntry);
            }
        }
        return javaFiles;
    }

    // create AST
    private static CompilationUnit parse(char[] classSource) {
        ASTParser parser = ASTParser.newParser(AST.JLS4); // java +1.6
        parser.setResolveBindings(true);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);

        parser.setBindingsRecovery(true);

        Map options = JavaCore.getOptions();
        parser.setCompilerOptions(options);

        parser.setUnitName("");

        String[] sources = { projectSourcePath };
        String[] classpath = {jrePath};

        parser.setEnvironment(classpath, sources, new String[] { "UTF-8"}, true);
        parser.setSource(classSource);

        return (CompilationUnit) parser.createAST(null); // create and parse
    }

    // navigate method information
    public static void printMethodInfo(CompilationUnit parse) {
        MethodDeclarationVisitor visitor = new MethodDeclarationVisitor();
        parse.accept(visitor);

        for (MethodDeclaration method : visitor.getMethods()) {
            String methodName = method.getName().toString();
            String returnType = method.getReturnType2() != null ? method.getReturnType2().toString() : "void";
            callGraph.addMethod(methodName, returnType);
            System.out.println("Method name: " + methodName + " Return type: " + returnType);
        }
    }

    // navigate variables inside method
    public static void printVariableInfo(CompilationUnit parse) {
        MethodDeclarationVisitor visitor1 = new MethodDeclarationVisitor();
        parse.accept(visitor1);
        for (MethodDeclaration method : visitor1.getMethods()) {
            VariableDeclarationFragmentVisitor visitor2 = new VariableDeclarationFragmentVisitor();
            method.accept(visitor2);

            for (VariableDeclarationFragment variableDeclarationFragment : visitor2.getVariables()) {
                String variableName = variableDeclarationFragment.getName().toString();
                String variableType = variableDeclarationFragment.resolveBinding() != null ? variableDeclarationFragment.resolveBinding().getType().getName() : "Unknown";
                System.out.println("Variable name: " + variableName + " Variable type: " + variableType + " Variable initializer: " + variableDeclarationFragment.getInitializer());
            }
        }
    }

    // navigate method invocations inside method
    public static void printMethodInvocationInfo(CompilationUnit parse) {
        MethodDeclarationVisitor visitor1 = new MethodDeclarationVisitor();
        parse.accept(visitor1);
        for (MethodDeclaration method : visitor1.getMethods()) {
            String methodName = method.getName().toString();
            String callerType = method.resolveBinding().getDeclaringClass().getName();
            MethodInvocationVisitor visitor2 = new MethodInvocationVisitor();
            method.accept(visitor2);

            for (MethodInvocation methodInvocation : visitor2.getMethods()) {
                String invokedMethodName = methodInvocation.getName().toString();
                IMethodBinding methodBinding = methodInvocation.resolveMethodBinding();
                if (methodBinding != null) {
                    String invokedMethodType = methodBinding.getDeclaringClass().getName();
                    System.out.println("Method " + methodName + " (type: " + callerType + ") invokes method " + invokedMethodName + " (type: " + invokedMethodType + ")");
                    callGraph.addCall(methodName, invokedMethodName, callerType, invokedMethodType);
                } else {
                    System.out.println("Method " + methodName + " (type: " + callerType + ") invokes method " + invokedMethodName + " but the method binding could not be resolved.");
                    callGraph.addCall(methodName, invokedMethodName, callerType, "Unknown");
                }
            }
        }
    }

    // Print the call graph
    public static void printCallGraph() {
        System.out.println("Methods:");
        for (String method : callGraph.getMethods()) {
            System.out.println(method + " (type: " + callGraph.getMethodType(method) + ")");
        }

        System.out.println("Calls:");
        for (Map.Entry<String, Set<String>> entry : callGraph.getCalls().entrySet()) {
            String caller = entry.getKey();
            String callerType = callGraph.getMethodType(caller);
            for (String callee : entry.getValue()) {
                String calleeType = callGraph.getMethodType(callee);
                System.out.println(caller + " (type: " + callerType + ") -> " + callee + " (type: " + calleeType + ")");
            }
        }
    }
}