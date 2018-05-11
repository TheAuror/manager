package hu.unideb.inf.auror.manager.Services;

import hu.unideb.inf.auror.manager.models.FinancialRecordModel;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class StatisticsServiceTest {
    private List<FinancialRecordModel> records;

    @Before
    public void setUp() throws Exception {
        records = new ArrayList<>();
        FinancialRecordModel record = new FinancialRecordModel();
        record.setIsIncome(true);
        record.setAmount(140);
        records.add(record);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getSumOfIncome() {
        double sum = StatisticsService.getSumOfIncome(records);
        Assert.assertEquals(140, sum, 0);
    }

    @Test
    public void getSumOfExpenses() {
        double sum = StatisticsService.getSumOfExpenses(records);
        Assert.assertEquals(0, sum, 0);
    }
}