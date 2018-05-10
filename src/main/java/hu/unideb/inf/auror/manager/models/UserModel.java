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

import hu.unideb.inf.auror.manager.Services.PasswordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Represents a record in the USERS table.
 * Stores the user's name and his/her hashed password.
 */
@Entity
@Table(name = "USERS")
public class UserModel {
    /**
     * SLF4J Logger.
     */
    private final static Logger logger = LoggerFactory.getLogger(UserModel.class);
    /**
     * Stores the record's id.
     */
    @Id
    private int id = -1;
    /**
     * Stores the user's name.
     */
    @Column(nullable = false, unique = true)
    private String name;
    /**
     * Stores the user's hashed password.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Basic constructor.
     */
    public UserModel() {

    }

    /**
     * @return Returns the user's id.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the record's id.
     *
     * @param id The id which will be set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return Returns the user's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the user's name.
     *
     * @param name The username which will be set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the hashed password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Hashes and sets the password for the user.
     *
     * @param password The password which hash will be set.
     */
    public void setPassword(String password) {
        this.password = PasswordService.hashPassword(password, "");
    }


}
