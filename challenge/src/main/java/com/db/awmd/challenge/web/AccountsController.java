package com.db.awmd.challenge.web;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.Transfer;
import com.db.awmd.challenge.exception.BalanceFailedException;
import com.db.awmd.challenge.exception.BalanceSuccessException;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.service.AccountsService;
import javax.validation.Valid;

import com.db.awmd.challenge.service.TransferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/accounts")
@Slf4j
public class AccountsController {

  private final AccountsService accountsService;

  private final TransferService transferService;

  @Autowired
  public AccountsController(AccountsService accountsService, TransferService transferService) {
    this.accountsService = accountsService;
    this.transferService = transferService;
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> createAccount(@RequestBody @Valid Account account) {

    log.info("Creating account {}", account);

    try {
    this.accountsService.createAccount(account);
    } catch (DuplicateAccountIdException daie) {
      return new ResponseEntity<>(daie.getMessage(), HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetMapping(path = "/{accountId}")
  public Account getAccount(@PathVariable String accountId) {
    log.info("Retrieving account for id {}", accountId);
    return this.accountsService.getAccount(accountId);
  }

  /**
   * Rest API end point to transfer amount
   * This method maps the custom exception thrown to the response entity
   *
   * @param - transfer - Domain object holding the from AccountId, to AccountId and balance
   * @return - ResponseEntity - Displays error message thrown from the custom exception
   *
   */
  @PostMapping(path = "/transferAmount")
  @ResponseBody
  public ResponseEntity<Object> transferAmount(@RequestBody @Valid Transfer transfer) {
    log.info("Method transferAmount started");
    ResponseEntity<Object> response = null;
    try {
      this.transferService.transferAmount(transfer);
    } catch (BalanceFailedException exception) {
      response =  new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (BalanceSuccessException exception) {
      response =  new ResponseEntity<>(exception.getMessage(), HttpStatus.OK);
    }
    log.info("Method transferAmount ended");
   return response;
  }

}
