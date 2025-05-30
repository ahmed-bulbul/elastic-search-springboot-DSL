package com.bulbul.es.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(indexName = "customers")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer {
    private int id;
    private String firstName;
    private String lastName;
    private int age;
}
