package hu.unideb.inf.auror.manager.models;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.Period;


/**
 * A model that stores the data for a financial transaction.
 */
@Entity
@Table(name = "FINANCIAL_RECORDS")
public class FinancialRecordModel {
    /**
     * SLF4J Logger.
     */
    private final static Logger logger = LoggerFactory.getLogger(FinancialRecordModel.class);

    /**
     * Stores the id of the record.
     */
    @Id
    private int id = -1;
    /**
     * Stores the name of the financial transaction.
     */
    @Column
    private String name;
    /**
     * Indicates if the transaction was an income.
     */
    @Column(nullable = false)
    private boolean isIncome;
    /**
     * Stores the amount of the financial transaction.
     */
    @Column(nullable = false)
    private double amount;
    /**
     * Stores the date of the records creation.
     */
    @GeneratedValue
    @Column(nullable = false)
    private LocalDateTime dateOfCreation;
    /**
     * Indicates if the record is a recurring one.
     */
    @Column(nullable = false)
    private boolean isRecurring = false;
    /**
     * A period field which will be stored in the database in binary.
     */
    @Column
    private Period period;
    /**
     * A foreign key for the USERS table.
     */
    @ManyToOne
    private UserModel user;

    /**
     * Basic constructor.
     */
    public FinancialRecordModel() {
    }

    /**
     * @return Returns the id field of the model.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id of the model.
     *
     * @param id The id which will be set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return Returns the name field of the model.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name field of the model.
     *
     * @param name The string which will be set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the isIncome field of the model.
     */
    public boolean getIsIncome() {
        return isIncome;
    }

    /**
     * Sets the isIncome field of the model.
     *
     * @param income The boolean value which will be set.
     */
    public void setIsIncome(boolean income) {
        isIncome = income;
    }

    /**
     * @return Returns the amount field of the model.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Sets the amount field of the model.
     *
     * @param amount The amount which will be set.
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * @return Returns the dateOfCreation field of the model.
     */
    public LocalDateTime getDateOfCreation() {
        return dateOfCreation;
    }

    /**
     * Sets the dateOfCreation field of the model.
     * If the given parameter is null the LocalDateTime.now() will be set.
     *
     * @param date The LocalDateTime which will be set.
     */
    public void setDateOfCreation(LocalDateTime date) {
        if (date == null)
            date = LocalDateTime.now();
        this.dateOfCreation = date;
    }

    /**
     * @return Returns the isRecurring field of the model.
     */
    public boolean getIsRecurring() {
        return isRecurring;
    }

    /**
     * Sets the isRecurring field of the model.
     *
     * @param recurring The boolean value which will be set.
     */
    private void setIsRecurring(boolean recurring) {
        isRecurring = recurring;
    }

    /**
     * @return Returns the period field of the model.
     */
    public Period getPeriod() {
        return period;
    }

    /**
     * Sets the period field of the model.
     * The incoming period must be a greater than zero time period in order to be set.
     * Sets the isRecurring field.
     *
     * @param period The period which will be set.
     */
    public void setPeriod(Period period) {
        if (period == null || period.isZero())
            setIsRecurring(false);
        else
            setIsRecurring(true);
        if (period != null && period.isNegative()) {
            logger.error("Negative period");
            return;
        }
        this.period = period;
    }

    /**
     * @return Returns the user field of the model.
     */
    public UserModel getUser() {
        return user;
    }

    /**
     * Sets the user field of the model.
     *
     * @param user The user model which will be set.
     */
    public void setUser(UserModel user) {
        this.user = user;
    }

    /**
     * @return Returns a string representation of the object.
     */
    @Override
    public String toString() {
        String result = "";
        result += "Id: " + this.getId() + "\n";
        result += "IsIncome: " + this.getIsIncome() + "\n";
        result += "Amount: " + this.getAmount() + "\n";
        result += "Date of creation: " + this.getDateOfCreation() + "\n";

        return result;
    }
}
