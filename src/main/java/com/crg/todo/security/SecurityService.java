package com.crg.todo.security;

import com.crg.todo.security.entity.Account;
import com.crg.todo.security.repository.AccountsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecurityService {

    private static final Logger logger = LoggerFactory.getLogger(SecurityService.class);

    @Autowired
    private AccountsRepository accountsRepository;

    public Account createUser(Account user) {
        List<Account> usersFromDatabase = accountsRepository.findAll();

        for (Account accountFromDatabase : usersFromDatabase) {
            if (accountFromDatabase.getUsername().equals(user.getUsername())) {
                return null;
            }
        }
        return accountsRepository.save(user);
    }

    public List<Account> findAllUsers() {
        return accountsRepository.findAll();
    }

    public Account findByUsername(String username) {
        return accountsRepository.findByUsername(username);
    }
}
