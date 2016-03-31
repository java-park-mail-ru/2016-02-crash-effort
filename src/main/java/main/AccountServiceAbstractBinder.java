package main;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

/**
 * Created by vladislav on 30.03.16.
 */
public class AccountServiceAbstractBinder extends AbstractBinder {
    private final AccountService accountService;

    public AccountServiceAbstractBinder(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    protected void configure() {
        bind(accountService).to(AccountService.class);
    }
}
