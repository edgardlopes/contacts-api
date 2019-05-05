package com.dev.usersapi.api.v1;

import com.dev.usersapi.entity.Contact;
import com.dev.usersapi.entity.Person;
import com.dev.usersapi.exception.ResourceNotFoundException;
import com.dev.usersapi.repository.ContactRepository;
import com.dev.usersapi.repository.PersonRepository;
import com.dev.usersapi.validator.ContactValidator;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
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

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ContactValidator validator;

    @GetMapping
    public @ResponseBody
    Page<Contact> getContacts(@PathVariable Long personId, Pageable pageable) {
        validatePerson(personId);
        return this.repository.findByPersonId(personId, pageable);
    }

    @PostMapping
    public ResponseEntity create(@PathVariable Long personId, @RequestBody @Valid Contact contact, BindingResult result) throws MethodArgumentNotValidException {
        validatePerson(personId);

        validator.validate(contact, result);
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }

        contact.setPerson(new Person(personId));
        repository.save(contact);
        return ResponseEntity.status(HttpStatus.CREATED).body(contact);
    }

    @GetMapping("/{id}")
    public @ResponseBody
    Contact getById(@PathVariable Long personId, @PathVariable Long id) {
        validatePerson(personId);
        return repository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Long personId, @PathVariable Long id, @RequestBody @Valid Contact contact, BindingResult result) throws MethodArgumentNotValidException {
        validatePerson(personId);

        validator.validate(contact, result);
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }

        contact.setPerson(new Person(personId));
        contact.setId(id);
        repository.save(contact);
        return ResponseEntity.ok(contact);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long personId, @PathVariable Long id) {
        validatePerson(personId);

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private void validatePerson(Long personId) {
        if (!personRepository.existsById(personId)) {
            throw new ResourceNotFoundException("User " + personId + " doesn't exists.");
        }
    }
}
