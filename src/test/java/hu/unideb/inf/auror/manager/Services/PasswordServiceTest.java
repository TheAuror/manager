package hu.unideb.inf.auror.manager.Services;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PasswordServiceTest {

    @Test
    public void comparePassword() {
        String pw = PasswordService.hashPassword("password", "");
        assert PasswordService.comparePassword(pw, "password");
        assert !PasswordService.comparePassword(pw, "pword");
    }

    @Test
    public void hashPassword() {
        String pw1 = PasswordService.hashPassword("password", "");
        String pw2 = PasswordService.hashPassword("password", "");
        assert !pw1.equals(pw2);
        pw1 = PasswordService.hashPassword("password", "salt");
        pw2 = PasswordService.hashPassword("password", "salt");
        assert pw1.equals(pw2);
    }
}