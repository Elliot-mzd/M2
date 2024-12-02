package umontpellier.erl;
import org.slf4j.Logger;
public class User {
    private String id;

    private String name;

    private int age;

    private String email;

    private String password;

    public User(String id, String name, int age, String email, String password) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.email = email;
        this.password = password;
    }

    public String getId() {
        logger.info("Entering method: getId()");
        return id;
    }

    public void setId(String id) {
        logger.info("Entering method: setId(java.lang.String)");
        this.id = id;
    }

    public String getName() {
        logger.info("Entering method: getName()");
        return name;
    }

    public void setName(String name) {
        logger.info("Entering method: setName(java.lang.String)");
        this.name = name;
    }

    public int getAge() {
        logger.info("Entering method: getAge()");
        return age;
    }

    public void setAge(int age) {
        logger.info("Entering method: setAge(int)");
        this.age = age;
    }

    public String getEmail() {
        logger.info("Entering method: getEmail()");
        return email;
    }

    public void setEmail(String email) {
        logger.info("Entering method: setEmail(java.lang.String)");
        this.email = email;
    }

    public String getPassword() {
        logger.info("Entering method: getPassword()");
        return password;
    }

    public void setPassword(String password) {
        logger.info("Entering method: setPassword(java.lang.String)");
        this.password = password;
    }

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(umontpellier.erl.User.class);
}
