package umontpellier.erl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class UserSession {
    private static UserSession instance;

    private User currentUser;

    private UserSession() {
    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    private static final Logger logger = LoggerFactory.getLogger(UserSession.class);
}
