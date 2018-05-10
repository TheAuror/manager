package hu.unideb.inf.auror.manager.DAOs;

import hu.unideb.inf.auror.manager.models.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for the <code>UserModel</code> class.
 */
public class UserDAO {
    /**
     * SLF4J logger.
     */
    private final static Logger logger = LoggerFactory.getLogger(FinancialRecordDAO.class);
    /**
     * Boolean that indicates whether an instance has been initialized yet.
     */
    private static boolean initialized = false;
    /**
     * Static instance of the DAO.
     */
    private static UserDAO userDAO;
    /**
     * Logged in user.
     */
    private static UserModel currentUser;

    /**
     * The <code>EntityManager</code> which provides the connection to the database.
     */
    private EntityManager entityManager;
    /**
     * The maximum id plus 1 of the FinancialRecordModels.
     */
    private int nextRecordId = 0;

    /**
     * Basic constructor.
     * Calls <code>Initialize()</code>
     */
    public UserDAO() {
        Initialize();
    }

    /**
     * @return Returns or creates the only UserDAO object.
     */
    public static UserDAO getInstance() {
        if (!initialized)
            userDAO = new UserDAO();
        return userDAO;
    }

    /**
     * Initializes the DAO, creating an <code>EntityManager</code>.
     */
    private void Initialize() {
        try {
            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("AppPU");
            entityManager = entityManagerFactory.createEntityManager();
            initialized = true;
            if (GetUsers().isEmpty()) {
                UserModel defaultUser = new UserModel();
                defaultUser.setName("default");
                defaultUser.setPassword("");
                Save(defaultUser);
            }
        } catch (Exception e) {
            initialized = false;
        }
    }

    /**
     * Returns every <code>UserModel</code> from the database.
     *
     * @return Returns a List of <code>UserModel</code>s
     */
    public List<UserModel> GetUsers() {
        if (!initialized)
            return new ArrayList<>();
        TypedQuery<UserModel> query = entityManager.createQuery("SELECT e FROM UserModel e", UserModel.class);
        return query.getResultList();
    }

    /**
     * @return Returns the maximum id from the USERS table plus 1.
     */
    private int getNextId() {
        Optional<Integer> maxId = GetUsers().stream().map(UserModel::getId).max(Integer::compareTo);
        maxId.ifPresent(integer -> nextRecordId = integer + 1);
        return nextRecordId;
    }

    /**
     * Saves a <code>UserModel</code>.
     *
     * @param user The <code>UserModel</code> which will be saved.
     */
    public void Save(UserModel user) {
        if (user.getId() == -1) {
            user.setId(getNextId());
            entityManager.getTransaction().begin();
            entityManager.persist(user);
            entityManager.getTransaction().commit();
        }
        currentUser = user;
    }

    /**
     * @return Returns the logged in user.
     */
    public UserModel GetCurrentUser() {
        return currentUser;
    }

    /**
     * Sets the logged in user.
     *
     * @param user The user that logs in.
     */
    public void SetCurrentUser(UserModel user) {
        currentUser = user;
    }
}
