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

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "FINANCIAL_RECORDS")
public class FinancialRecordModel {
    @Id
    private int id;
    @Column(nullable = false)
    private boolean isIncome;
    @Column(nullable = false)
    private double amount;
    @GeneratedValue
    private LocalDateTime dateOfCreation;

    public FinancialRecordModel() {
    }

    public FinancialRecordModel(int id, boolean isIncome, double amount, LocalDateTime dateOfCreation) {
        setId(id);
        setIsIncome(isIncome);
        setAmount(amount);
        setDateOfCreation(dateOfCreation);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getIsIncome() {
        return isIncome;
    }

    public void setIsIncome(boolean income) {
        isIncome = income;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(LocalDateTime date) {
        if (date == null)
            date = LocalDateTime.now();
        this.dateOfCreation = date;
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
