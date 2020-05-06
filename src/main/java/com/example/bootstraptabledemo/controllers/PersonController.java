package com.example.bootstraptabledemo.controllers;

import com.example.bootstraptabledemo.domain.Person;
import com.example.bootstraptabledemo.domain.PersonRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PersonController {

    private final PersonRepository personRepository;

    public PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @GetMapping({"", "index"})
    public String getPersons(Model model) {
        model.addAttribute("persons", personRepository.findAll());
        return "index";
    }

    @GetMapping({"/api/data"})
    @ResponseBody
    public Iterable<Person> getPersons() {
        return personRepository.findAll();
    }

}
