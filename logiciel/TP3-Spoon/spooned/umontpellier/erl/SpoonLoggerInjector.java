package umontpellier.erl;
import org.slf4j.Logger;
import spoon.Launcher;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.code.CtThrow;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.visitor.filter.TypeFilter;
public class SpoonLoggerInjector {
    public static void main(String[] args) {
        logger.info("Entering method: main(java.lang.String[])");
        Launcher launcher = new Launcher();
        launcher.addInputResource("src/main/java");// Chemin vers vos classes

        launcher.getEnvironment().setAutoImports(true);
        // Charger le modèle des classes
        launcher.buildModel();
        // Ajouter un logger SLF4J à toutes les classes
        launcher.getModel().getElements(new TypeFilter<>(CtClass.class)).forEach(ctClass -> {
            // Vérifier si un champ nommé "logger" existe
            boolean loggerExists = ctClass.getFields().stream().anyMatch(field -> ((CtField<?>) (field)).getSimpleName().equals("logger"));// Cast explicite ici

            if (!loggerExists) {
                // Ajouter le logger si absent
                CtField<?> loggerField = launcher.getFactory().createField();
                loggerField.addModifier(ModifierKind.PRIVATE);
                loggerField.addModifier(ModifierKind.STATIC);
                loggerField.addModifier(ModifierKind.FINAL);
                loggerField.setSimpleName("logger");
                loggerField.setType(launcher.getFactory().createCtTypeReference(Logger.class));
                loggerField.setDefaultExpression(launcher.getFactory().createCodeSnippetExpression(("org.slf4j.LoggerFactory.getLogger(" + ctClass.getQualifiedName()) + ".class)"));
                ctClass.addField(loggerField);
            }
        });
        // Injecter des logs dans les méthodes
        launcher.getModel().getElements(new TypeFilter<>(CtMethod.class)).forEach(method -> {
            // Ajouter un log info au début de chaque méthode publique
            if (method.getModifiers().contains(ModifierKind.PUBLIC)) {
                CtCodeSnippetStatement logStatement = launcher.getFactory().createCodeSnippetStatement(("logger.info(\"Entering method: " + method.getSignature()) + "\")");
                method.getBody().insertBegin(logStatement);
            }
            // Ajouter un log d'erreur avant chaque levée d'exception
            method.getBody().getElements(new TypeFilter<>(CtThrow.class)).forEach(ctThrow -> {
                CtCodeSnippetStatement errorLog = launcher.getFactory().createCodeSnippetStatement(((("logger.error(\"Exception thrown in method: " + method.getSignature()) + " - \" + ") + ctThrow.getThrownExpression().toString()) + ")");
                ctThrow.insertBefore(errorLog);// Insérer avant l'instruction `throw`

            });
        });
        // Sauvegarder les modifications dans les fichiers sources
        launcher.prettyprint();
    }

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(umontpellier.erl.SpoonLoggerInjector.class);
}
