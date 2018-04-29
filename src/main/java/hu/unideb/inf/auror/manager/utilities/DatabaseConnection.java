package hu.unideb.inf.auror.manager.utilities;

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

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DatabaseConnection {
    final static Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);
    private static boolean initialized = false;
    private static DatabaseConnection databaseConnection;

    private Connection connection;

    public DatabaseConnection()
    {
        Initialize();
    }

    public static DatabaseConnection getInstance() {
        if(!initialized)
            databaseConnection = new DatabaseConnection();
        return databaseConnection;
    }

    private boolean Initialize()
    {
        try {
            Class.forName("org.firebirdsql.jdbc.FBDriver");
            Properties properties = new Properties();
            properties.put("user","sysdba");
            properties.put("password","admin");
            properties.put("useUnicode", "yes");
            properties.put("characterEncoding", "utf-8");
            properties.put("charSet", "utf-8");
            connection = DriverManager
                    .getConnection(
                            "jdbc:firebirdsql://localhost:3050/C:/Users/Auror/Desktop/DATABASE.FDB", properties);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        initialized = connection != null;
        return initialized;
    }
}
