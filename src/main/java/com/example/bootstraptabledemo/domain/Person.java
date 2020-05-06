package com.example.bootstraptabledemo.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "persons")
@Getter
@Setter
@Slf4j
@EqualsAndHashCode
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "email")
    private String email;

    @Column(name = "dateofbirth")
    private Date dateOfBirth;

    @Column(name = "personalnumber")
    private String personalNumber;

    public Person() {
    }

    @Builder
    public Person(Integer id, String name, String surname, String email, Date dateOfBirth, String personalNumber) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.personalNumber = personalNumber;
    }
}
