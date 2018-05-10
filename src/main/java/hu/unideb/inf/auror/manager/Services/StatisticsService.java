package hu.unideb.inf.auror.manager.Services;

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

}
