package umontpellier.erl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spoon.Launcher;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.code.CtThrow;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.visitor.filter.TypeFilter;
import java.io.File;

public class SpoonLoggerInjector {
    private static final Logger logger = LoggerFactory.getLogger(SpoonLoggerInjector.class);

    public static void main(String[] args) {
        // Définir le chemin d'entrée en dur
        String inputPath = "F:\\M2\\M2\\logiciel\\TP3\\src\\main\\java";  // Remplacer par ton chemin source
        String outputPath = "F:\\M2\\M2\\logiciel\\TP3\\src\\main\\java";  // Même chemin pour écraser les fichiers sources

        // Vérifier si le chemin d'entrée est valide
        if (!isValidPath(inputPath)) {
            logger.error("Invalid input path: " + inputPath);
            return;
        }

        logger.info("Starting Spoon process...");

        // Créer une instance de Launcher Spoon
        Launcher launcher = new Launcher();
        launcher.addInputResource(inputPath);

        // Spécifier le répertoire de sortie pour écrire directement
        launcher.setSourceOutputDirectory(new File(outputPath));

        launcher.getEnvironment().setAutoImports(true); // Gérer automatiquement les imports

        try {
            // Charger et analyser le modèle des classes
            launcher.buildModel();
            logger.info("Model built successfully.");

            // Ajouter un logger SLF4J à toutes les classes
            launcher.getModel().getElements(new TypeFilter<>(CtClass.class)).forEach(ctClass -> {
                boolean loggerExists = ctClass.getFields().stream()
                        .anyMatch(field -> ((CtField<?>) field).getSimpleName().equals("logger"));

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
                    logger.info("Logger added to class: " + ctClass.getQualifiedName());
                }
            });

            // Injecter des logs dans les méthodes
            launcher.getModel().getElements(new TypeFilter<>(CtMethod.class)).forEach(method -> {
                // Ajouter un log info au début de chaque méthode publique
                if (method.getModifiers().contains(ModifierKind.PUBLIC)) {
                    CtCodeSnippetStatement logStatement = launcher.getFactory().createCodeSnippetStatement(
                            "logger.info(\"Entering method: " + method.getSignature() + "\")"
                    );
                    method.getBody().insertBegin(logStatement);
                    logger.debug("Log info added for method: " + method.getSignature());
                }

                // Ajouter un log d'erreur avant chaque levée d'exception
                method.getBody().getElements(new TypeFilter<>(CtThrow.class)).forEach(ctThrow -> {
                    CtCodeSnippetStatement errorLog = launcher.getFactory().createCodeSnippetStatement(
                            "logger.error(\"Exception thrown in method: " + method.getSignature() +
                                    " - \" + " + ctThrow.getThrownExpression().toString() + ")"
                    );
                    ctThrow.insertBefore(errorLog); // Insérer avant l'instruction `throw`
                    logger.debug("Error log added for exception in method: " + method.getSignature());
                });
            });

            // Sauvegarder les modifications directement dans les fichiers sources
            launcher.prettyprint(); // Cela écrasera directement les fichiers sources

            logger.info("Files have been written to: " + outputPath); // Les fichiers sont directement écrasés

        } catch (Exception e) {
            logger.error("Error processing Spoon model", e);
        }
    }

    // Méthode pour vérifier si un chemin est valide
    private static boolean isValidPath(String path) {
        File file = new File(path);
        if (!file.exists()) {
            logger.error("Path does not exist: " + path);
            return false;
        }
        if (!file.isDirectory()) {
            logger.error("Path is not a directory: " + path);
            return false;
        }
        return true;
    }
}