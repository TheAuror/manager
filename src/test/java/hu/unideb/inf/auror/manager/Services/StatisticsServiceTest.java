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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class StatisticsServiceTest {
    private List<FinancialRecordModel> records;

    @Before
    public void setUp() {
        records = new ArrayList<>();
        records.add(new FinancialRecordModel(true,90));
        records.add(new FinancialRecordModel(true,110));
        records.add(new FinancialRecordModel(false,170));
        records.add(new FinancialRecordModel(false,30));
        records.add(new FinancialRecordModel(true,298));
        records.add(new FinancialRecordModel(true,2));
        records.add(new FinancialRecordModel(false,100));
        records.add(new FinancialRecordModel(false,100));
        records.add(new FinancialRecordModel(true,100));
    }

    @Test
    public void getSumOfIncome() {
        double sum = StatisticsService.getSumOfIncome(records);
        Assert.assertEquals(600, sum, 0);
        assert StatisticsService.getSumOfIncome(new ArrayList<>())==0;
    }

    @Test
    public void getSumOfExpenses() {
        double sum = StatisticsService.getSumOfExpenses(records);
        Assert.assertEquals(400, sum, 0);
        assert StatisticsService.getSumOfExpenses(new ArrayList<>())==0;
    }

    @Test
    public void getTotalSum() {
        double sum = StatisticsService.getTotalSum(records);
        Assert.assertEquals(200, sum, 0);
        assert StatisticsService.getTotalSum(new ArrayList<>())==0;
    }

    @Test
    public void getAverage() {
        double sum = StatisticsService.getAverage(records);
        Assert.assertEquals(22, sum, 0.3);
        assert StatisticsService.getAverage(new ArrayList<>())==0;
    }

    @Test
    public void getAverageOfIncome() {
        double sum = StatisticsService.getAverageOfIncome(records);
        Assert.assertEquals(120, sum, 0);
        assert StatisticsService.getAverageOfIncome(new ArrayList<>())==0;
    }

    @Test
    public void getAverageOfExpenses() {
        double sum = StatisticsService.getAverageOfExpenses(records);
        Assert.assertEquals(100, sum, 0);
        assert StatisticsService.getAverageOfExpenses(new ArrayList<>())==0;
    }
}
