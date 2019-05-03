package com.dev.usersapi.api.v1;

import com.dev.usersapi.entity.Contact;
import com.dev.usersapi.entity.Person;
import com.dev.usersapi.exception.ResourceNotFoundException;
import com.dev.usersapi.repository.ContactRepository;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author edgard
 */
@Controller
@RequestMapping("/api/v1/persons/{personId}/contacts")
public class ContactController {

    @Autowired
    private ContactRepository repository;

    @GetMapping
    public @ResponseBody
    Page<Contact> getContacts(@PathVariable Long personId, Pageable pageable) {
        return this.repository.findByPersonId(personId, pageable);
    }

    @PostMapping
    public void create(@PathVariable Long personId, @RequestBody @Valid Contact contact) {
        contact.setPerson(new Person(personId));
        repository.save(contact);
    }

    @GetMapping("/{id}")
    public @ResponseBody
    Contact getById(@PathVariable Long personId, @PathVariable Long id) {
        return repository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }
    
    @PutMapping("/{id}")
    public void update(@PathVariable Long personId, @PathVariable Long id, @RequestBody @Valid Contact contact){
        contact.setPerson(new Person(personId));
        contact.setId(id);
        repository.save(contact);
    }
    
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long personId, @PathVariable Long id){
        repository.deleteById(id);
    }

}
