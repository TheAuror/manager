package hu.unideb.inf.auror.manager.DAOs;

/*-
 * #%L
 * Manager
 * %%
 * Copyright (C) 2016 - 2018 Faculty of Informatics, University of Debrecen
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

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
    private UserDAO() {
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
            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("Firebird server");
            entityManager = entityManagerFactory.createEntityManager();
            initialized = true;
            if (GetUsers().isEmpty()) {
                UserModel defaultUser = new UserModel();
                defaultUser.setName("default");
                defaultUser.setPassword("");
                Save(defaultUser);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            initialized = false;
        }
        logger.trace("UserDAO initialized.");
    }

    /**
     * Returns every <code>UserModel</code> from the database.
     *
     * @return Returns a List of <code>UserModel</code>s
     */
    public List<UserModel> GetUsers() {
        logger.trace("UsedDAO.GetUsers()");
        if (!initialized)
            return new ArrayList<>();
        TypedQuery<UserModel> query = entityManager.createQuery("SELECT e FROM UserModel e", UserModel.class);
        return query.getResultList();
    }

    /**
     * @return Returns the maximum id from the USERS table plus 1.
     */
    private int getNextId() {
        logger.trace("UserDAO.getNextId()");
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
        logger.info("New user is created and logged in: "+user.getName());
    }

    /**
     * @return Returns the logged in user.
     */
    UserModel GetCurrentUser() {
        return currentUser;
    }

    /**
     * Sets the logged in user.
     *
     * @param user The user that logs in.
     */
    public void SetCurrentUser(UserModel user) {
        currentUser = user;
        logger.info("User logged in: "+user.getName());
    }
}
