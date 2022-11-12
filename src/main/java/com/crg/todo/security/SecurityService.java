package com.crg.todo.security;

import com.crg.todo.security.entity.User;
import com.crg.todo.security.repository.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecurityService {

    private static final Logger logger =
            LoggerFactory.getLogger(SecurityService.class);

    @Autowired
    private UsersRepository usersRepository;

    public User createUser(User user) {
        List<User> usersFromDatabase = usersRepository.findAll();

        boolean isUserExists = false;
        for (User userFromDatabase : usersFromDatabase) {
            if (userFromDatabase.getUsername().equals(user.getUsername())) {
                isUserExists = true;
            }
        }

        if (!isUserExists) {
            return usersRepository.save(user);
        } else {
            return null;
        }
    }

    public List<User> findAllUsers() {
        return usersRepository.findAll();
    }
}
