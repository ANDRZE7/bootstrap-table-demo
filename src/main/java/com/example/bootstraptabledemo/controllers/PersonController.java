package com.example.bootstraptabledemo.controllers;

import com.example.bootstraptabledemo.datatable.DataTableQueryParameters;
import com.example.bootstraptabledemo.datatable.DataTableResponse;
import com.example.bootstraptabledemo.datatable.ParamResolverException;
import com.example.bootstraptabledemo.datatable.params.DataTableQueryParametersFactory;
import com.example.bootstraptabledemo.domain.Person;
import com.example.bootstraptabledemo.services.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Controller
@RequestMapping("persons")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping({"", "index", "example1"})
    public String getPersonsExample1(Model model) {
        model.addAttribute("persons", personService.findAll());
        return "example1";
    }

    @GetMapping({"example2"})
    public String getPersonsExample2(Model model) {
        return "example2";
    }

    @GetMapping({"example3"})
    public String getPersonsExample3(Model model) {
        return "example3";
    }

    /**
     *
     * @return All persons in response body in Json or XML
     */
    @GetMapping({"/api/data"})
    @ResponseBody
    public Iterable<Person> getPersons() {
        return personService.findAll();
    }

    @GetMapping({"/api/data/managed"})
    @ResponseBody
    public DataTableResponse getPersonData(@RequestParam Map<String, String> allRequestParams) {
        try{
            DataTableQueryParameters dataTableQueryParameters = DataTableQueryParametersFactory
                    .createFromParametersMap(allRequestParams);
            return personService.query(dataTableQueryParameters);
        } catch (ParamResolverException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "DataTable request query params format is not supported");
        }
    }
}
