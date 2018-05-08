package hu.unideb.inf.auror.manager.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

public class PasswordUtil {
    private final static Logger logger = LoggerFactory.getLogger(PasswordUtil.class);

    public static boolean comparePassword(String hash, String password) {
        String salt = hash.substring(hash.lastIndexOf("#") + 1);
        String hashedPassword = hashPassword(password, salt);
        return hash.equals(hashedPassword);
    }

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
