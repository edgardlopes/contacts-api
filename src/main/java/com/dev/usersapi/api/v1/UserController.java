package com.dev.usersapi.api.v1;

import com.dev.usersapi.entity.Person;
import com.dev.usersapi.exception.ResourceNotFoundException;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.dev.usersapi.repository.PersonRepository;

@RestController
@RequestMapping("/api/v1/persons")
public class UserController {

    @Autowired
    private PersonRepository repository;

    @GetMapping
    public @ResponseBody
    Page<Person> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @GetMapping("/{id}")
    public @ResponseBody
    Person getById(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    @PostMapping
    public void create(@RequestBody @Valid Person user) {
        repository.save(user);
    }

    @PutMapping("/{id}")
    public void create(@PathVariable Long id, @Valid @RequestBody Person user) {
        user.setId(id);
        repository.save(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        if(!repository.existsById(id)){
            throw new ResourceNotFoundException();
        }
        repository.deleteById(id);
    }


}
