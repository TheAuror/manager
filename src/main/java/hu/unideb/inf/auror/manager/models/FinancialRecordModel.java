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


@Entity
@Table(name = "FINANCIAL_RECORDS")
public class FinancialRecordModel {
    private final static Logger logger = LoggerFactory.getLogger(FinancialRecordModel.class);

    @Id
    private int id;
    @Column
    private String name;
    @Column(nullable = false)
    private boolean isIncome;
    @Column(nullable = false)
    private double amount;
    @GeneratedValue
    @Column(nullable = false)
    private LocalDateTime dateOfCreation;
    @Column(nullable = false)
    private boolean isRecurring = false;
    @Column
    private Period period;

    public FinancialRecordModel() {
    }

    //Getters
    public int getId() {
        return id;
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsIncome() {
        return isIncome;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getDateOfCreation() {
        return dateOfCreation;
    }

    public boolean getIsRecurring() {
        return isRecurring;
    }

    private void setIsRecurring(boolean recurring) {
        isRecurring = recurring;
    }

    public void setIsIncome(boolean income) {
        isIncome = income;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDateOfCreation(LocalDateTime date) {
        if (date == null)
            date = LocalDateTime.now();
        this.dateOfCreation = date;
    }

    public Period getPeriod() {
        return period;
    }

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

    //Withs
    public FinancialRecordModel withId(int id) {
        setId(id);
        return this;
    }

    public FinancialRecordModel withName(String name) {
        setName(name);
        return this;
    }

    public FinancialRecordModel withIsIncome(boolean isIncome) {
        setIsIncome(isIncome);
        return this;
    }

    public FinancialRecordModel withAmount(double amount) {
        setAmount(amount);
        return this;
    }

    public FinancialRecordModel withDateOfCreation(LocalDateTime dateOfCreation) {
        setDateOfCreation(dateOfCreation);
        return this;
    }

    private FinancialRecordModel withIsRecurring(boolean isRecurring) {
        setIsRecurring(isRecurring);
        return this;
    }

    public FinancialRecordModel withPeriod(Period period) {
        setPeriod(period);
        return this;
    }

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
