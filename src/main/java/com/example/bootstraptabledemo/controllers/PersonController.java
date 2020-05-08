package com.example.bootstraptabledemo.controllers;

import com.example.bootstraptabledemo.domain.Person;
import com.example.bootstraptabledemo.domain.PersonRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("persons")
public class PersonController {

    private final PersonRepository personRepository;

    public PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @GetMapping({"", "index", "example1"})
    public String getPersonsExample1(Model model) {
        model.addAttribute("persons", personRepository.findAll());
        return "example1";
    }

    @GetMapping({"example2"})
    public String getPersonsExample2(Model model) {
        model.addAttribute("persons", personRepository.findAll());
        return "example2";
    }

    @GetMapping({"/api/data"})
    @ResponseBody
    public Iterable<Person> getPersons() {
        return personRepository.findAll();
    }

}
