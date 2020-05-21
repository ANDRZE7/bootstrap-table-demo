package com.example.bootstraptabledemo.controllers;

import com.example.bootstraptabledemo.datatable.DataTableQueryParameters;
import com.example.bootstraptabledemo.datatable.DataTableResponse;
import com.example.bootstraptabledemo.datatable.ParamResolverException;
import com.example.bootstraptabledemo.datatable.params.DataTableQueryParametersFactory;
import com.example.bootstraptabledemo.domain.Person;
import com.example.bootstraptabledemo.services.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Controller
@RequestMapping("persons")
public class PersonController {

    private final PersonService personService;
    private Integer rowsetSize = 100;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @ModelAttribute("select")
    public int getSelect() {
        return this.rowsetSize;
    }

    @GetMapping("{example}")
    public String getPersonsExample3(Model model, @PathVariable String example) {
        model.addAttribute("exampleName", example);
        // for example 1 push the data to be binded by Thymeleaf
        if("example1".equals(example))
            model.addAttribute("persons", personService.findAll(rowsetSize));
        return "persons";
    }

    /**
     *
     * @return All persons in response body in Json or XML
     */
    @GetMapping({"/api/data"})
    @ResponseBody
    public Iterable<Person> getPersons() {
        return personService.findAll(rowsetSize);
    }

    @GetMapping({"/api/data/managed"})
    @ResponseBody
    public DataTableResponse getPersonData(@RequestParam Map<String, String> allRequestParams) {
        try{
            DataTableQueryParameters dataTableQueryParameters = DataTableQueryParametersFactory
                    .createFromParametersMap(allRequestParams);
            return personService.query(dataTableQueryParameters, rowsetSize);
        } catch (ParamResolverException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "DataTable request query params format is not supported");
        }
    }

    @PostMapping("/api")
    @ResponseBody
    public ResponseEntity<Integer> setRowsetSize(@RequestParam int size) {
        this.rowsetSize = size;
        return new ResponseEntity<>(size, HttpStatus.OK);
    }
}
