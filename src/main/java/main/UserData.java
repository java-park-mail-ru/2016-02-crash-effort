package main;

import main.AccountService;
import javax.inject.Singleton;

/**
 * Created by vladislav on 28.02.16.
 */
@Singleton
public class UserData {
    private static AccountService accService = new AccountService();

    public static AccountService getAccountService() {
        return accService;
    }
}
