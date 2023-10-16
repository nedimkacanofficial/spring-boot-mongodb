package com.ndmkcn.springbootmongodb.controller;

import com.ndmkcn.springbootmongodb.collection.Person;
import com.ndmkcn.springbootmongodb.dto.PersonDTO;
import com.ndmkcn.springbootmongodb.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "person")
@RequiredArgsConstructor
public class PersonController {
    private final Logger logger= LoggerFactory.getLogger(PersonController.class);
    private final PersonService personService;
    @PostMapping
    public ResponseEntity<PersonDTO> save(@RequestBody PersonDTO personDTO) {
        try {
            PersonDTO personDTO1=this.personService.save(personDTO);
            logger.debug("REST request to save Person : {}", personDTO1);
            return new ResponseEntity<>(personDTO1, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping
    public ResponseEntity<List<PersonDTO>> getPersonStartWith(@RequestParam(name = "name") String firstName) {
        List<PersonDTO> personDTO=this.personService.getPersonStartWith(firstName);
        logger.debug("REST request to get First Name {}",firstName);
        if (personDTO == null || personDTO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(personDTO,HttpStatus.OK);
    }
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") String id) {
        this.personService.delete(id);
        logger.debug("REST request to delete Person Id {}",id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping(path = "/age")
    public ResponseEntity<List<PersonDTO>> getByPersonAge(@RequestParam(name = "minAge") Integer minAge,@RequestParam(name = "maxAge") Integer maxAge) {
        List<PersonDTO> personDTOList=this.personService.getByPersonAge(minAge,maxAge);
        logger.debug("REST request to get Min Age {}, Max Age {}",minAge,maxAge);
        if (personDTOList == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(personDTOList,HttpStatus.OK);
    }
    @GetMapping(path = "/search")
    public ResponseEntity<Page<Person>> searchPerson(
            @RequestParam(required = false,name = "name") String name,
            @RequestParam(required = false,name = "minAge") Integer minAge,
            @RequestParam(required = false,name = "maxAge") Integer maxAge,
            @RequestParam(required = false,name = "city") String city,
            @RequestParam(defaultValue = "0",name = "page") Integer page,
            @RequestParam(defaultValue = "5",name = "size") Integer size
    ) {
        Pageable pageable= PageRequest.of(page,size);
        return new ResponseEntity<>(personService.searchPerson(name,minAge,maxAge,city,pageable),HttpStatus.OK);
    }
    @GetMapping(path = "/oldestPerson")
    public ResponseEntity<List<Document>> getOldestPerson() {
        List<Document> documents=this.personService.getOldestPersonByCity();
        return new ResponseEntity<>(documents,HttpStatus.OK);
    }
}
