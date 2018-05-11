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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

/**
 * Generates hashed passwords and compares them.
 */
public class PasswordService {
    /**
     * SLF4J Logger.
     */
    private final static Logger logger = LoggerFactory.getLogger(PasswordService.class);

    /**
     * Compares the given hash and a passwords hash.
     *
     * @param hash     Hashed password from the database.
     * @param password The password which hash will be compared to the hash.
     * @return Returns true if the compared hashes match.
     */
    public static boolean comparePassword(String hash, String password) {
        String salt = hash.substring(hash.lastIndexOf("#") + 1);
        String hashedPassword = hashPassword(password, salt);
        return hash.equals(hashedPassword);
    }

    /**
     * Hashes a given password with the given salt.
     * If the salt is empty, the function will generate one.
     *
     * @param password The password which will be hashed.
     * @param salt     The salt for the hashing.
     * @return Returns the hashed password.
     */
    public static String hashPassword(String password, String salt) {
        if (salt.isEmpty())
            salt = UUID.randomUUID().toString().subSequence(0, 8).toString();
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            logger.error("Can't create SHA-256 algorithm");
        }
        byte[] hash = digest.digest((password + salt).getBytes(StandardCharsets.UTF_8));
        String encoded = Base64.getEncoder().encodeToString(hash);
        return encoded + "#" + salt;
    }
}
