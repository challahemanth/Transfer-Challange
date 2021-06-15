package com.db.awmd.challenge.service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.Transfer;
import com.db.awmd.challenge.exception.BalanceFailedException;
import com.db.awmd.challenge.exception.BalanceSuccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class TransferService {

    @Autowired
    private final EmailNotificationService emailNotificationService;
    private final AccountsService accountsService;
    private static ReentrantLock lock = new ReentrantLock();

    public TransferService(AccountsService accountsService, EmailNotificationService emailNotificationService) {
        this.accountsService = accountsService;
        this.emailNotificationService = emailNotificationService;
    }

    /**
     * Rest API end point to transfer amount
     * This method maps the custom exception thrown to the response entity
     *
     * @param - transfer - Domain object holding the from AccountId, to AccountId and balance
     * @return - String - Throws the custom exceptions for both success and failed case scenarios
     *
     */
    public String transferAmount(Transfer transfer) throws BalanceFailedException, BalanceSuccessException {
        lock.lock();
        String fromAccountNumber = transfer.getFromAccountId();
        String toAccountNumber = transfer.getToAccountId();
        if(StringUtils.isEmpty(fromAccountNumber)) {
            lock.unlock();
            throw new BalanceFailedException("Cannot transfer the amount as the entered from AccountId "+ fromAccountNumber +" is invalid");
        } else if(StringUtils.isEmpty(toAccountNumber)) {
            lock.unlock();
            throw new BalanceFailedException("Cannot transfer the amount as the entered to AccountId "+ toAccountNumber +" is invalid");
        }
        Account fromAccount = this.accountsService.getAccount(transfer.getFromAccountId());
        Account toAccount = this.accountsService.getAccount(transfer.getToAccountId());
        if(null == fromAccount) {
            lock.unlock();
            throw new BalanceFailedException("Cannot find a account linked to the from AccountId "+ fromAccountNumber);
        } else if( null == toAccount) {
            lock.unlock();
            throw new BalanceFailedException("Cannot find a account linked to the to AccountId "+ fromAccountNumber);
        }

        BigDecimal amount = transfer.getAmount();

        if(amount.compareTo(BigDecimal.ZERO) == 0) {
            lock.unlock();
            throw new BalanceFailedException("Cannot transfer the amount as the amount entered should be greater than 0");
        }

        if(amount.compareTo(BigDecimal.ZERO) != 0 && fromAccount.getBalance().compareTo(BigDecimal.ZERO) > 0
                && fromAccount.getBalance().compareTo(amount) >= 0) {
            fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
            this.accountsService.saveAccount(fromAccount);
            toAccount.setBalance(toAccount.getBalance().add(amount));
            this.accountsService.saveAccount(toAccount);

            //Sends notification when transfer is done successfully to both the accounts
            emailNotificationService.notifyAboutTransfer(toAccount, "Amount received From " + fromAccountNumber + " Amount received is "+amount);
            emailNotificationService.notifyAboutTransfer(fromAccount, "Amount transferred to " + toAccountNumber + " Amount Transferred is "+amount);
            lock.unlock();
            throw new BalanceSuccessException("Transferred Successfully from "+ fromAccountNumber + " to Account " + toAccountNumber + " and remaining balance is " + fromAccount.getBalance());
        } else {
            lock.unlock();
           throw new BalanceFailedException("Transfer is failed due to insufficient balance");
        }
    }

}
