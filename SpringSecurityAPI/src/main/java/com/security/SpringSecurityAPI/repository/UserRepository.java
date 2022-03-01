package com.security.SpringSecurityAPI.repository;

import com.security.SpringSecurityAPI.model.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Exceptions thrown by @Repository annotations will converted into Spring Data JPA exceptions.
*/
@Repository("userRepository")   // to perform data access operations (read and write)
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Some common operations provided by CrudRepository are:
     * - findById()
     * - findAll()
     * - save()
     * - saveAll()
     * - delete()
     * - deleteAll()
     * - count()
    */

    // If change the name of these interface methods, code will not work as expected.
    
    User findByEmail(String email);

    User findByConfirmationToken(String confirmationToken);
}
