package com.vann.services;

import java.util.*;
import java.util.regex.Pattern;

import com.vann.exceptions.*;
import com.vann.models.*;
import com.vann.repositories.*;
import com.vann.utils.LogHandler;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService {

    private final UserRepo userRepo;
    private final CartRepo cartRepo;

    public UserService(UserRepo userRepo, CartRepo cartRepo) {
        this.userRepo = userRepo;
        this.cartRepo = cartRepo;
    }

    /**
     * Creates a single User instance.
     * This method makes an exception to this application's general rule of delegating interactions
     * with each repository to its designated service layer. The reason for this exception is to
     * overcome the temporal issue of both the UserService and CartService requiring an instance
     * of the other service at initialisation. The UserService is permitted to interact with
     * the CartRepo for the sole purpose of creating a new cart when a new user is created.
     * 
     * @param potentialUser
     * @return user A newly created user
     */
    @Transactional
    public User createOne(User potentialUser) {
        User user = create(potentialUser);
        LogHandler.status201Created(UserService.class + " | 1 user record created | id=" + user.getId());
        Cart cart = cartRepo.save(new Cart(user, new HashMap<>()));
        LogHandler.status201Created(UserService.class + " | 1 cart record created | id=" + cart.getId());
        return user;
    }

    @Transactional
    private User create(User potentialUser) {
        String formattedEmail = potentialUser.getEmail().trim().toLowerCase();
        potentialUser.setEmail(formattedEmail);
        validateUserAttributes(potentialUser);
        return saveUser(potentialUser);
    }

    private void validateUserAttributes(User user) throws IllegalArgumentException {
        String name = user.getName();
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(UserService.class + " | invalid name | cannot be null or empty");
        }
        String email = user.getEmail();
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException(UserService.class + " | invalid email | email=" + email);
        }
        checkForEmailConflict(user);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern pattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        if (pattern.matcher(email).matches()) {
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public User saveUser(User user) {
        checkForEmailConflict(user);
        return userRepo.save(user);
    }

    private void checkForEmailConflict(User user) throws FieldConflictException {
        Optional<User> existingUserOptional = userRepo.findByEmail(user.getEmail().toLowerCase());
    
        if (existingUserOptional.isPresent() && 
            !existingUserOptional.get().getId().equals(user.getId())) {
            throw new FieldConflictException(UserService.class + " | record already exists | email=" + user.getEmail());
        }
    }

    public List<User> findAllUsers() {
        List<User> users = userRepo.findAll();
        logBulkFindOperation(users, "findAll()");
        return users;
    }

    private void logBulkFindOperation(List<User> users, String details) {
        if (users.isEmpty()) {
            LogHandler.status204NoContent(UserService.class + " | 0 records | " + details);    
        } else {
            LogHandler.status200OK(UserService.class + " | " + users.size() + " records | " + details);
        }
    }

    public User findUserById(UUID id) throws RecordNotFoundException {
        Optional<User> userOptional = userRepo.findById(id);

        if (userOptional.isPresent()) {
            LogHandler.status200OK(UserService.class + " | record found | id=" + id);
            return userOptional.get();
        } else {
            throw new RecordNotFoundException(UserService.class + " | record not found | id=" + id);
        }
    }

    public User findUserByEmail(String email) throws RecordNotFoundException {
        Optional<User> userOptional = userRepo.findByEmail(email.toLowerCase());

        if (userOptional.isPresent()) {
            LogHandler.status200OK(UserService.class + " | record found | email=" + email);
            return userOptional.get();
        } else {
            throw new RecordNotFoundException(UserService.class + " | record not found | email=" + email);
        }
    }

    @Transactional
    public User updateUser(UUID id, User updatedUser) throws RecordNotFoundException {
        User existingUser = userRepo.findById(id).orElseThrow(() -> 
            new RecordNotFoundException(UserService.class + " | record not found | id=" + id)
        );
        validateUserAttributes(updatedUser);
        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        User savedUser = saveUser(existingUser);
        LogHandler.status200OK(UserService.class + " | record updated | id=" + id);

        return savedUser;
    }

    @Transactional
    public void deleteUser(UUID id) {
        if (userRepo.existsById(id)) {
            userRepo.deleteById(id);
            LogHandler.status204NoContent(UserService.class + " | record deleted | id=" + id);
        }
    }
}
