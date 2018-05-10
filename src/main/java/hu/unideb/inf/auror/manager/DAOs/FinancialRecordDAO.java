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

import hu.unideb.inf.auror.manager.models.FinancialRecordModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Data Access Object for the <code>FinancialRecordModel</code> class.
 */
public class FinancialRecordDAO {
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
    private static FinancialRecordDAO financialRecordDAO;
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
    private FinancialRecordDAO() {
        Initialize();
    }

    /**
     * @return Returns or creates the only FinancialRecordDAO object.
     */
    public static FinancialRecordDAO getInstance() {
        if (!initialized)
            financialRecordDAO = new FinancialRecordDAO();
        return financialRecordDAO;
    }

    /**
     * Initializes the DAO, creating an <code>EntityManager</code>.
     */
    private void Initialize() {
        try {
            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("AppPU");
            entityManager = entityManagerFactory.createEntityManager();
            initialized = true;
        } catch (Exception e) {
            initialized = false;
        }
    }

    /**
     * @return Returns true if the DAO is initialized.
     */
    public boolean IsInitialized(){
        return initialized;
    }

    /**
     * @return Returns every record of the logged in user.
     */
    public List<FinancialRecordModel> GetRecordsForUser() {
        return GetAllRecords().stream()
                .filter(e -> e.getUser().getId() == UserDAO.getInstance().GetCurrentUser().getId())
                .collect(Collectors.toList());
    }

    /**
     * Returns every <code>FinancialRecordModel</code> from the database.
     * Calls <code>createRecurringTransactions()</code>
     *
     * @return Returns a List of <code>FinancialRecordModel</code>s
     */
    public List<FinancialRecordModel> GetAllRecords() {
        if(!initialized)
            return new ArrayList<>();
        TypedQuery<FinancialRecordModel> query = entityManager.createQuery("SELECT e FROM FinancialRecordModel e", FinancialRecordModel.class);
        createRecurringTransactions(query.getResultList());
        return query.getResultList();
    }

    /**
     * @return Returns the maximum id from the FINANCIAL_RECORDS table plus 1.
     */
    private int getNextId() {
        Optional<Integer> maxId = GetAllRecords().stream().map(FinancialRecordModel::getId).max(Integer::compareTo);
        maxId.ifPresent(integer -> nextRecordId = integer + 1);
        return nextRecordId;
    }

    /**
     * Saves or edits the given record based on its id.
     * If the id is not -1 it will be edited,
     * because every new record is created with an id -1.
     *
     * @param record The record which will be saved or edited.
     */
    public void Save(FinancialRecordModel record) {
        if (record.getDateOfCreation() == null)
            record.setDateOfCreation(LocalDateTime.now());
        if (record.getUser() == null)
            record.setUser(UserDAO.getInstance().GetCurrentUser());
        if (record.getId() == -1) {
            record.setId(getNextId());
            entityManager.getTransaction().begin();
            entityManager.persist(record);
            entityManager.getTransaction().commit();
        } else {
            entityManager.getTransaction().begin();
            entityManager.merge(record);
            entityManager.getTransaction().commit();
        }
    }

    /**
     * Deletes a given record from the database.
     *
     * @param record The record which will be deleted.
     */
    public void Delete(FinancialRecordModel record) {
        entityManager.getTransaction().begin();
        entityManager.remove(record);
        entityManager.getTransaction().commit();
    }

    /**
     * Selects the recurring records from the given list and calls
     * the <code>createRecurringTransaction</code> on each of them.
     *
     * @param recordModels List of <code>FinancialRecordModel</code>s
     */
    private void createRecurringTransactions(List<FinancialRecordModel> recordModels) {
        List<FinancialRecordModel> records = recordModels.stream()
                .filter(FinancialRecordModel::getIsRecurring).collect(Collectors.toCollection(ArrayList::new));
        for (FinancialRecordModel record : records) {
            createRecurringTransaction(record);
        }
    }

    /**
     * Creates new financial records based on the given parameter.
     * Recursive
     *
     * @param record Periodical financial record
     */
    private void createRecurringTransaction(FinancialRecordModel record) {
        if (record.getDateOfCreation().plus(record.getPeriod()).isBefore(LocalDateTime.now())) {
            FinancialRecordModel newRecord = new FinancialRecordModel();
            newRecord.setName(record.getName());
            newRecord.setAmount(record.getAmount());
            newRecord.setDateOfCreation(record.getDateOfCreation().plus(record.getPeriod()));
            newRecord.setIsIncome(record.getIsIncome());
            newRecord.setPeriod(record.getPeriod());
            newRecord.setUser(record.getUser());
            record.setPeriod(null);
            Save(record);
            Save(newRecord);
            createRecurringTransaction(newRecord);
        }
    }
}
