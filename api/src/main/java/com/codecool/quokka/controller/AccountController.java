package com.codecool.quokka.controller;

import com.codecool.quokka.model.account.Account;

import com.codecool.quokka.model.account.AccountDto;
import com.codecool.quokka.service.account.AccountService;
import io.prometheus.client.Counter;
import io.prometheus.client.Histogram;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/user")
@RestController
public class AccountController {
    private AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity addUser(@RequestBody Account account) {
        if (accountService.getAccountByEmail(account.getEmailAddress())) {
            return new ResponseEntity<>("Email occupied", HttpStatus.BAD_REQUEST);
        }
        if (accountService.getAccountByUserName(account.getUserName())) {
            return new ResponseEntity<>("User error", HttpStatus.BAD_REQUEST);
        }
        if (!accountService.validate(account.getEmailAddress())) {
            return new ResponseEntity<>("Email error", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(accountService.addAccount(account), HttpStatus.OK);
    }

    @GetMapping
    // admin endpoint
    public Set<AccountDto> getAllUser() {
        return accountService.getAllAccount();
    }

    @GetMapping(path = "{id}")
    @PreAuthorize("hasRole('ROLE_TRADER')")
    public ResponseEntity getUserById(@PathVariable("id") UUID id) {
        Optional<AccountDto> account = accountService.getAccount(id);
        if (account.isPresent()) {
            return new ResponseEntity<>(account.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_TRADER')")
    public ResponseEntity deleteUserById(@RequestBody HashMap<String, String> body) {
        UUID id = UUID.fromString(body.get("id"));
        if (accountService.getAccount(id).isPresent()) {
            accountService.deleteAccount(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(path = "{id}")
    @PreAuthorize("hasRole('ROLE_TRADER')")
    public ResponseEntity updateUser(@PathVariable("id") UUID id, @RequestBody HashMap<String, String> fields) {
        Optional<AccountDto> accountDto = accountService.updateAccount(id, fields);
        if (accountDto.isPresent()) {
            return new ResponseEntity<>(accountDto.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
