// src/main/java/org/example/Coupling/Parser.java
package org.example.Coupling;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.example.visitor.ClassDeclarationVisitor;
import org.example.visitor.MethodDeclarationVisitor;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class Parser {
    protected String projectPath;
    protected String jrePath;
    protected ASTParser parser;
    protected int X;

    public Parser(String projectPath, int X) {
        setProjectPath(projectPath);
        setJREPath(System.getProperty("java.home"));
        this.X = X;
        configure();
    }

    public String getProjectPath() {
        return projectPath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    public String getJREPath() {
        return jrePath;
    }

    public void setJREPath(String jrePath) {
        this.jrePath = jrePath;
    }

    public void configure() {
        parser = ASTParser.newParser(AST.JLS4);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setResolveBindings(true);
        parser.setBindingsRecovery(true);
        parser.setCompilerOptions(JavaCore.getOptions());
        parser.setUnitName("");
        parser.setEnvironment(new String[]{getJREPath()}, new String[]{getProjectPath()}, new String[]{"UTF-8"}, true);
    }

    public CompilationUnit parse(File sourceFile) throws IOException {
        parser.setSource(FileUtils.readFileToString(sourceFile, (Charset) null).toCharArray());
        return (CompilationUnit) parser.createAST(null);
    }

    public static ArrayList<File> listJavaFilesForFolder(final File folder) {
        ArrayList<File> javaFiles = new ArrayList<>();
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File fileEntry : files) {
                    if (fileEntry.isDirectory()) {
                        javaFiles.addAll(listJavaFilesForFolder(fileEntry));
                    } else if (fileEntry.getName().endsWith(".java")) {
                        javaFiles.add(fileEntry);
                    }
                }
            } else {
                throw new IllegalArgumentException("Le dossier est vide");
            }
        } else {
            throw new IllegalArgumentException("Aucune fichier src trouvé");
        }
        return javaFiles;
    }

    public void printCount() throws IOException {
        final File folder = new File(projectPath);
        ArrayList<File> javaFiles = listJavaFilesForFolder(folder);

        int classCount = countClasses(javaFiles);
        int methodCount = countMethods(javaFiles);

        System.out.println("Nombre de classes : " + classCount);
        System.out.println("Nombre de méthodes : " + methodCount);
    }

    public int countClasses(ArrayList<File> javaFiles) throws IOException {
        int classCount = 0;
        for (File fileEntry : javaFiles) {
            CompilationUnit parse = parse(fileEntry);
            ClassDeclarationVisitor classVisitor = new ClassDeclarationVisitor();
            parse.accept(classVisitor);
            classCount += classVisitor.getClasses().size();
        }
        return classCount;
    }

    public int countMethods(ArrayList<File> javaFiles) throws IOException {
        int methodCount = 0;
        for (File fileEntry : javaFiles) {
            CompilationUnit parse = parse(fileEntry);
            MethodDeclarationVisitor methodVisitor = new MethodDeclarationVisitor();
            parse.accept(methodVisitor);
            methodCount += methodVisitor.getMethods().size();
        }
        return methodCount;
    }
}