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

public class FinancialRecordDAO {
    private final static Logger logger = LoggerFactory.getLogger(FinancialRecordDAO.class);
    private static boolean initialized = false;
    private static FinancialRecordDAO financialRecordDAO;

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private int nextRecordId = 0;

    private FinancialRecordDAO() {
        Initialize();
    }

    public static FinancialRecordDAO getInstance() {
        if (!initialized)
            financialRecordDAO = new FinancialRecordDAO();
        return financialRecordDAO;
    }

    private boolean Initialize() {
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("AppPU");
            entityManager = entityManagerFactory.createEntityManager();
            initialized = true;
        } catch (Exception e) {
            initialized = false;
        }
        return initialized;
    }

    public boolean IsInitialized(){
        return initialized;
    }

    public List<FinancialRecordModel> GetRecordsForUser() {
        return GetAllRecords().stream()
                .filter(e -> e.getUser().getId() == UserDAO.getInstance().GetCurrentUser().getId())
                .collect(Collectors.toList());
    }

    public List<FinancialRecordModel> GetAllRecords() {
        if(!initialized)
            return new ArrayList<>();
        TypedQuery<FinancialRecordModel> query = entityManager.createQuery("SELECT e FROM FinancialRecordModel e", FinancialRecordModel.class);
        createRecurringTransactions(query.getResultList());
        return query.getResultList();
    }

    private int getNextId() {
        Optional<Integer> maxId = GetAllRecords().stream().map(FinancialRecordModel::getId).max(Integer::compareTo);
        maxId.ifPresent(integer -> nextRecordId = integer + 1);
        return nextRecordId;
    }

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

    public void Delete(FinancialRecordModel record) {
        entityManager.getTransaction().begin();
        entityManager.remove(record);
        entityManager.getTransaction().commit();
    }

    private boolean createRecurringTransactions(List<FinancialRecordModel> recordModels) {
        boolean addedRecord = false;
        List<FinancialRecordModel> records = recordModels.stream()
                .filter(FinancialRecordModel::getIsRecurring).collect(Collectors.toCollection(ArrayList::new));
        for (FinancialRecordModel record : records) {
            addedRecord |= createRecurringTransaction(record);
        }
        return addedRecord;
    }

    private boolean createRecurringTransaction(FinancialRecordModel record) {
        if (record.getDateOfCreation().plus(record.getPeriod()).isBefore(LocalDateTime.now())) {
            FinancialRecordModel newRecord = new FinancialRecordModel()
                    .withName(record.getName())
                    .withAmount(record.getAmount())
                    .withDateOfCreation(record.getDateOfCreation().plus(record.getPeriod()))
                    .withIsIncome(record.getIsIncome())
                    .withPeriod(record.getPeriod())
                    .withUser(record.getUser());
            record.setPeriod(null);
            Save(record);
            Save(newRecord);
            createRecurringTransaction(newRecord);
            return true;
        }
        return false;
    }
}
