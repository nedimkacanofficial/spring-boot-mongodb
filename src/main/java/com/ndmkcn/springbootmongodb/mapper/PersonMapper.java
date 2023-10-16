package com.ndmkcn.springbootmongodb.mapper;

import com.ndmkcn.springbootmongodb.collection.Person;
import com.ndmkcn.springbootmongodb.dto.PersonDTO;

import java.util.List;
import java.util.stream.Collectors;

public class PersonMapper {
    public static Person toEntity(PersonDTO personDTO) {
        Person person=new Person();
        person.setFirstName(personDTO.getFirstName());
        person.setLastName(personDTO.getLastName());
        person.setAge(personDTO.getAge());
        person.setHobbies(personDTO.getHobbies());
        person.setAddresses(personDTO.getAddresses());
        return person;
    }
    public static PersonDTO toDTO(Person person) {
        PersonDTO personDTO=new PersonDTO();
        personDTO.setId(person.getId());
        personDTO.setFirstName(person.getFirstName());
        personDTO.setLastName(person.getLastName());
        personDTO.setAge(person.getAge());
        personDTO.setHobbies(person.getHobbies());
        personDTO.setAddresses(person.getAddresses());
        return personDTO;
    }
    public static List<Person> toEntityList(List<PersonDTO> personDTOList) {
        return personDTOList.stream()
                .map(PersonMapper::toEntity)
                .collect(Collectors.toList());
    }
    public static List<PersonDTO> toDTOList(List<Person> personList) {
        return personList.stream()
                .map(PersonMapper::toDTO)
                .collect(Collectors.toList());
    }
}
