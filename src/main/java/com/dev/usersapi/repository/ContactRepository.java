package com.dev.usersapi.repository;

import com.dev.usersapi.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author edgard
 */
@Repository
public interface ContactRepository extends JpaRepository<Contact, Long>{
    
    Page<Contact> findByPersonId(Long id, Pageable pageable);
    
}
