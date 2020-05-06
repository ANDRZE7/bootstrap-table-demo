package com.example.bootstraptabledemo.controllers;

import com.example.bootstraptabledemo.domain.PersonRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

}
