package com.ndmkcn.springbootmongodb.dto;

import com.ndmkcn.springbootmongodb.collection.Address;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonDTO {
    private String id;
    private String firstName;
    private String lastName;
    private Integer age;
    private List<String> hobbies;
    private List<Address> addresses;
}
