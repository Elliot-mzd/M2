package umontpellier.erl;
import org.slf4j.Logger;
public class Main {
    public static void main(String[] args) {
        logger.info("Entering method: main(java.lang.String[])");
        System.out.println("Hello world!");
    }

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(umontpellier.erl.Main.class);
}
