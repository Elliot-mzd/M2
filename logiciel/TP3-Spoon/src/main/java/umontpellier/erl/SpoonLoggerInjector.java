package umontpellier.erl;

import org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spoon.Launcher;
import spoon.reflect.code.*;
import spoon.reflect.declaration.*;
import spoon.reflect.visitor.filter.TypeFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SpoonLoggerInjector {
    private static final Logger logger = LoggerFactory.getLogger(SpoonLoggerInjector.class);

    public static void main(String[] args) {
        // Définir le chemin d'entrée et de sortie
        String inputPath = "F:\\M2\\M2\\logiciel\\TP3\\src\\main\\java";
        String outputPath = "F:\\M2\\M2\\logiciel\\TP3\\src\\main\\java";

        if (!isValidPath(inputPath)) {
            logger.error("Invalid input path: {}", inputPath);
            return;
        }

        logger.info("Starting Spoon process...");

        // Configurer Spoon
        Launcher launcher = new Launcher();
        launcher.addInputResource(inputPath);
        launcher.setSourceOutputDirectory(new File(outputPath));
        launcher.getEnvironment().setAutoImports(true);

        try {
            launcher.buildModel();
            logger.info("Model built successfully.");

            // Injecter le logger dans chaque classe
            launcher.getModel().getElements(new TypeFilter<>(CtClass.class)).forEach(ctClass -> {
                addLoggerToClass(launcher, ctClass);
            });

            // Injecter les logs dans les méthodes
            launcher.getModel().getElements(new TypeFilter<>(CtMethod.class)).forEach(method -> {
                injectLogsIntoMethod(launcher, method);
            });

            // Sauvegarder les modifications
            launcher.prettyprint();
            logger.info("Files have been written to: {}", outputPath);
        } catch (Exception e) {
            logger.error("Error processing Spoon model", e);
        }
    }

    private static void addLoggerToClass(Launcher launcher, CtClass<?> ctClass) {
        boolean loggerExists = ctClass.getFields().stream()
                .anyMatch(field -> field.getSimpleName().equals("logger"));

        if (!loggerExists) {
            CtField<?> loggerField = launcher.getFactory().createField();
            loggerField.addModifier(ModifierKind.PRIVATE);
            loggerField.addModifier(ModifierKind.STATIC);
            loggerField.addModifier(ModifierKind.FINAL);
            loggerField.setSimpleName("logger");
            loggerField.setType(launcher.getFactory().createCtTypeReference(org.slf4j.Logger.class));
            loggerField.setDefaultExpression(
                    launcher.getFactory().createCodeSnippetExpression(
                            "org.slf4j.LoggerFactory.getLogger(" + ctClass.getQualifiedName() + ".class)"
                    )
            );
            ctClass.addField(loggerField);
            logger.info("Logger added to class: {}", ctClass.getQualifiedName());
        }
    }

    private static void injectLogsIntoMethod(Launcher launcher, CtMethod<?> method) {
        if (method.getDeclaringType().getSimpleName().equals("ProductService")) {
            // Ajouter des variables MDC au début
            CtCodeSnippetStatement mdcSetup = launcher.getFactory().createCodeSnippetStatement(
                    "org.slf4j.MDC.put(\"userId\", UserSession.getInstance().getCurrentUser().getId());" +
                            "org.slf4j.MDC.put(\"action\", \"" + method.getSimpleName() + "\");" +
                            getProductIdMDCStatement(method)
            );

            // Ajouter un log d'entrée
            CtCodeSnippetStatement logEntry = launcher.getFactory().createCodeSnippetStatement(
                    "logger.info(\"Entered method: " + method.getSimpleName() + "\")"
            );
            method.getBody().insertBegin(logEntry);
            method.getBody().insertBegin(mdcSetup);

            // Ajouter un log pour chaque exception levée
            method.getBody().getElements(new TypeFilter<>(CtThrow.class)).forEach(ctThrow -> {
                CtCodeSnippetStatement logError = launcher.getFactory().createCodeSnippetStatement(
                        "logger.error(\"Encountered an error in method: " + method.getSimpleName() +
                                " - Exception: \" + " + ctThrow.getThrownExpression().toString() + ")"
                );
                ctThrow.insertBefore(logError);
            });

            // Ajouter un log de sortie à la fin
            CtCodeSnippetStatement logExit = launcher.getFactory().createCodeSnippetStatement(
                    "logger.info(\"Exited method: " + method.getSimpleName() + "\")"
            );
            method.getBody().insertEnd(logExit);

            // Nettoyer le MDC dans un bloc finally
            CtCodeSnippetStatement mdcClear = launcher.getFactory().createCodeSnippetStatement(
                    "org.slf4j.MDC.clear()"
            );
            method.getBody().insertEnd(mdcClear);
        }
    }
    private static String getProductIdMDCStatement(CtMethod<?> method) {
        // Vérifier si un paramètre s'appelle "product" et contient un ID
        for (CtParameter<?> param : method.getParameters()) {
            if (param.getSimpleName().equals("product")) {
                return "org.slf4j.MDC.put(\"productId\", String.valueOf(product.getId()));";
            }
        }

        // Vérifier si un paramètre s'appelle "id" pour des méthodes comme deleteProduct
        for (CtParameter<?> param : method.getParameters()) {
            if (param.getSimpleName().equals("id")) {
                return "org.slf4j.MDC.put(\"productId\", String.valueOf(id));";
            }
        }

        // Si aucun paramètre approprié n'est trouvé
        return "";
    }
    private static boolean isValidPath(String path) {
        File file = new File(path);
        return file.exists() && file.isDirectory();
    }
}