package hu.unideb.inf.auror.manager.Services;

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
