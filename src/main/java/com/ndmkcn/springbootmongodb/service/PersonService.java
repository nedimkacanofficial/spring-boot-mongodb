package com.ndmkcn.springbootmongodb.service;

import com.ndmkcn.springbootmongodb.collection.Person;
import com.ndmkcn.springbootmongodb.dto.PersonDTO;
import com.ndmkcn.springbootmongodb.mapper.PersonMapper;
import com.ndmkcn.springbootmongodb.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PersonService {
    private final Logger logger= LoggerFactory.getLogger(PersonService.class);
    private final PersonRepository personRepository;
    private final MongoTemplate mongoTemplate;

    public PersonDTO save(PersonDTO personDTO) {
        Person person=this.personRepository.save(PersonMapper.toEntity(personDTO));
        return PersonMapper.toDTO(person);
    }

    public List<PersonDTO> getPersonStartWith(String firstName) {
        List<Person> personList=this.personRepository.findByFirstNameStartsWith(firstName);
        return PersonMapper.toDTOList(personList);
    }

    public void delete(String id) {
        this.personRepository.deleteById(id);
    }

    public List<PersonDTO> getByPersonAge(Integer minAge, Integer maxAge) {
        List<Person> personList=this.personRepository.findByAgeBetween(minAge,maxAge);
        return PersonMapper.toDTOList(personList);
    }

    public Page<Person> searchPerson(String name, Integer minAge, Integer maxAge, String city, Pageable pageable) {
        Query query=new Query().with(pageable);
        List<Criteria> criteria=new ArrayList<>();
        if (name!=null&&!name.isEmpty()) {
            criteria.add(Criteria.where("firstName").regex(name,"i"));
        }
        if (minAge!=null&&maxAge!=null) {
            criteria.add(Criteria.where("age").gte(minAge).lte(maxAge));
        }
        if (city!=null&&!city.isEmpty()) {
            criteria.add(Criteria.where("address.city").is(city));
        }
        if (criteria!=null&&!criteria.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[0])));
        }
        Page<Person> personPage= PageableExecutionUtils.getPage(
                this.mongoTemplate.find(
                        query,
                        Person.class
                ), pageable, () -> this.mongoTemplate.count(query.skip(0).limit(0),Person.class)
        );
        return personPage;
    }

    public List<Document> getOldestPersonByCity() {
        UnwindOperation unwindOperation= Aggregation.unwind("addresses");
        SortOperation sortOperation=Aggregation.sort(Sort.Direction.DESC,"age");
        GroupOperation groupOperation=Aggregation.group("addresses.city").first(Aggregation.ROOT).as("oldestPerson");
        Aggregation aggregation=Aggregation.newAggregation(unwindOperation,sortOperation,groupOperation);
        List<Document> person=this.mongoTemplate.aggregate(aggregation,Person.class,Document.class).getMappedResults();
        return person;
    }

    public List<Document> getPopulationByCity() {
        UnwindOperation unwindOperation=Aggregation.unwind("addresses");
        GroupOperation groupOperation=Aggregation.group("addresses.city").count().as("popCount");
        SortOperation sortOperation=Aggregation.sort(Sort.Direction.DESC,"popCount");
        ProjectionOperation projectionOperation=Aggregation.project().andExpression("_id").as("city").andExpression("popCount").as("count").andExclude("_id");
        Aggregation aggregation=Aggregation.newAggregation(unwindOperation,groupOperation,sortOperation,projectionOperation);
        List<Document> documents=this.mongoTemplate.aggregate(aggregation,Person.class,Document.class).getMappedResults();
        return documents;
    }
}
