package hu.unideb.inf.auror.manager.Services;

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

import java.util.List;

/**
 * Generates statistics.
 */
public class StatisticsService {
    /**
     * SLF4J Logger.
     */
    private final static Logger logger = LoggerFactory.getLogger(StatisticsService.class);

    /**
     * Sums up the incomes and expenses.
     *
     * @param records List of <code>FinancialRecordModel</code>s
     * @return Returns the sum of every record's amount.
     */
    public static double getTotalSum(List<FinancialRecordModel> records) {
        return getSumOfIncome(records) - getSumOfExpenses(records);
    }

    /**
     * Sums the incomes amount.
     *
     * @param records List of <code>FinancialRecordModel</code>s
     * @return Returns the sum of the incomes.
     */
    public static double getSumOfIncome(List<FinancialRecordModel> records) {
        return records.stream()
                .filter(FinancialRecordModel::getIsIncome)
                .mapToDouble(FinancialRecordModel::getAmount)
                .sum();
    }

    /**
     * Sums the expenses amount.
     *
     * @param records List of <code>FinancialRecordModel</code>s
     * @return Returns the sum of the expenses.
     */
    public static double getSumOfExpenses(List<FinancialRecordModel> records) {
        return records.stream()
                .filter(e -> !e.getIsIncome())
                .mapToDouble(FinancialRecordModel::getAmount)
                .sum();
    }

    /**
     * Calculates the average of the incomes and expenses.
     *
     * @param records List of <code>FinancialRecordModel</code>s
     * @return Returns the average of every record's amount.
     */
    public static double getAverage(List<FinancialRecordModel> records) {
        return records.stream()
                .mapToDouble(FinancialRecordModel::getAmount)
                .average().orElse(0);
    }

    /**
     * Sums the incomes amount.
     *
     * @param records List of <code>FinancialRecordModel</code>s
     * @return Returns the average of the incomes.
     */
    public static double getAverageOfIncome(List<FinancialRecordModel> records) {
        return records.stream()
                .filter(FinancialRecordModel::getIsIncome)
                .mapToDouble(FinancialRecordModel::getAmount)
                .average().orElse(0);
    }

    /**
     * Sums the expenses amount.
     *
     * @param records List of <code>FinancialRecordModel</code>s
     * @return Returns the average of the expenses.
     */
    public static double getAverageOfExpenses(List<FinancialRecordModel> records) {
        return records.stream()
                .filter(e -> !e.getIsIncome())
                .mapToDouble(FinancialRecordModel::getAmount)
                .average().orElse(0);
    }
}
