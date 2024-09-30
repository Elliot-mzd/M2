package org.example.part2;
import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class Parser {

    public static final String projectPath = "/home/e20190006130/Documents/M2/logiciel/td1/TP1-test/";
    public static final String projectSourcePath = projectPath + "/src";
    public static final String jrePath = "/home/e20190006130/.jdks/corretto-18.0.2";

    public static void main(String[] args) throws IOException {

        // read java files
        final File folder = new File(projectSourcePath);
        ArrayList<File> javaFiles = listJavaFilesForFolder(folder);

        int classCount = 0;

        for (File fileEntry : javaFiles) {
            String content = FileUtils.readFileToString(fileEntry, "UTF-8");
            CompilationUnit parse = parse(content.toCharArray());

            // count classes
            classCount += countClasses(parse);
        }

        System.out.println("Number of classes: " + classCount);
    }

    // read all java files from specific folder
    public static ArrayList<File> listJavaFilesForFolder(final File folder) {
        ArrayList<File> javaFiles = new ArrayList<>();
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
            System.err.println("The folder " + folder.getAbsolutePath() + " does not exist or is not a directory.");
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
        String[] classpath = { jrePath };

        parser.setEnvironment(classpath, sources, new String[] { "UTF-8" }, true);
        parser.setSource(classSource);

        return (CompilationUnit) parser.createAST(null); // create and parse
    }

    // count class declarations
    private static int countClasses(CompilationUnit parse) {
        ClassDeclarationVisitor visitor = new ClassDeclarationVisitor();
        parse.accept(visitor);
        return visitor.getClasses().size();
    }
}