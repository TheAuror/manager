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

public class UserDAO {
    private final static Logger logger = LoggerFactory.getLogger(FinancialRecordDAO.class);
    private static boolean initialized = false;
    private static UserDAO userDAO;
    private static UserModel currentUser;

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private int nextRecordId = 0;

    public UserDAO() {
        Initialize();
    }

    public static UserDAO getInstance() {
        if (!initialized)
            userDAO = new UserDAO();
        return userDAO;
    }

    private boolean Initialize() {
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("AppPU");
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
        return initialized;
    }

    public boolean IsInitialized() {
        return initialized;
    }

    public List<UserModel> GetUsers() {
        if (!initialized)
            return new ArrayList<>();
        TypedQuery<UserModel> query = entityManager.createQuery("SELECT e FROM UserModel e", UserModel.class);
        return query.getResultList();
    }

    private int getNextId() {
        Optional<Integer> maxId = GetUsers().stream().map(UserModel::getId).max(Integer::compareTo);
        maxId.ifPresent(integer -> nextRecordId = integer + 1);
        return nextRecordId;
    }

    public void Save(UserModel user) {
        if (user.getId() == -1) {
            user.setId(getNextId());
            entityManager.getTransaction().begin();
            entityManager.persist(user);
            entityManager.getTransaction().commit();
        }
        currentUser = user;
    }

    public UserModel GetCurrentUser() {
        return currentUser;
    }

    public void SetCurrentUser(UserModel user) {
        currentUser = user;
    }
}
