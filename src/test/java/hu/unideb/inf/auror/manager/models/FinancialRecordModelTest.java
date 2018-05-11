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

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

import static org.junit.Assert.*;

public class FinancialRecordModelTest {
    private FinancialRecordModel record;
    private UserModel user;

    @Before
    public void setUp() {
        record = new FinancialRecordModel();
        record.setId(5);
        record.setName("test");
        record.setAmount(100);
        record.setIsIncome(false);
        record.setPeriod(Period.ofDays(1));
        user = new UserModel();
        user.setName("testUser");
        user.setPassword("password");
        record.setUser(user);
        record.setDateOfCreation(LocalDate.of(2000,1,20).atStartOfDay());
    }

    @Test
    public void getId() {
        assert record.getId() == 5;
    }

    @Test
    public void setId() {
        record.setId(6);
        assert record.getId() == 6;
    }

    @Test
    public void getName() {
        assert record.getName().equals("test");
    }

    @Test
    public void setName() {
        record.setName("newName");
        assert record.getName().equals("newName");
    }

    @Test
    public void getIsIncome() {
        assert !record.getIsIncome();
    }

    @Test
    public void setIsIncome() {
        record.setIsIncome(true);
        assert record.getIsIncome();
    }

    @Test
    public void getAmount() {
        assert record.getAmount() == 100;
    }

    @Test
    public void setAmount() {
        record.setAmount(200);
        assert record.getAmount() == 200;
    }

    @Test
    public void getDateOfCreation() {
        assert record.getDateOfCreation().isEqual(LocalDate.of(2000,1,20).atStartOfDay());
    }

    @Test
    public void setDateOfCreation() {
        record.setDateOfCreation(LocalDate.of(2010,2,1).atStartOfDay());
        assert record.getDateOfCreation().isEqual(LocalDate.of(2010,2,1).atStartOfDay());
    }

    @Test
    public void getIsRecurring() {
        assert record.getIsRecurring();
    }

    @Test
    public void getPeriod() {
        assert record.getPeriod().equals(Period.ofDays(1));
    }

    @Test
    public void setPeriod() {
        record.setPeriod(Period.ofYears(1));
        assert record.getPeriod().equals(Period.ofYears(1));
    }

    @Test
    public void getUser() {
        assert record.getUser() == user;
    }

    @Test
    public void setUser() {
        UserModel defaultUser = new UserModel();
        record.setUser(defaultUser);
        assert record.getUser().equals(defaultUser);
    }

    @Test
    public void toStringTest() {
        String output = record.toString();
        String expectedOutput =  "Id: 5\n" +
                "IsIncome: false\n" +
                "Amount: 100.0\n" +
                "Date of creation: " + record.getDateOfCreation().toString() + "\n";
        Assert.assertEquals(output, expectedOutput);
    }
}
