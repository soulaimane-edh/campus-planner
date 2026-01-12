package com.example.campusplanner.service;

import com.example.campusplanner.model.Role;
import com.example.campusplanner.model.UserAccount;
import com.example.campusplanner.repository.UserAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserAccountRepository userAccountRepository;
    public UserService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Transactional
    public UserAccount createUser(String fullName, String email, String rawPassword, Role role) {
        Optional<UserAccount> existing = userAccountRepository.findByEmail(email);
        if (existing.isPresent()) {
            return existing.get();
        }
        UserAccount user = new UserAccount(fullName, email, rawPassword, role);
        return userAccountRepository.save(user);
    }

    public List<UserAccount> findAll() {
        return userAccountRepository.findAll();
    }

    public Optional<UserAccount> findByEmail(String email) {
        return userAccountRepository.findByEmail(email);
    }
}
