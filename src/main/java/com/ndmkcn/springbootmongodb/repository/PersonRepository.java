package com.ndmkcn.springbootmongodb.repository;

import com.ndmkcn.springbootmongodb.collection.Person;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends MongoRepository<Person,String> {
    List<Person> findByFirstNameStartsWith(String firstName);
    @Query(value = "{'age':{$gt:?0,$lt:?1}}",fields = "{address: 0}")
    // Aşağıdaki sorgu yerine yukarıdaki query aynı işi yapıyor kullanabiliriz.
    // yukarıda şunu diyor age parametresinde methoda gelen min age 0ıncı değer
    // olarak atanacak maxage ise lt letterthan olarak ikinci parameter olarak
    // atanaccak ve bu aralıkta bir age sorgulaması yapacak.
    // burada fields alanı ise filtreleme yapabiliyor
    // 0 olunca adressleri getirme demek oluyor.
    List<Person> findByAgeBetween(Integer minAge,Integer maxAge);
}
