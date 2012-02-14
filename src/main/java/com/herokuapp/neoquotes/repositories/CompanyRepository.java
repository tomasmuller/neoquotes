package com.herokuapp.neoquotes.repositories;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.herokuapp.neoquotes.models.Company;

public interface CompanyRepository extends GraphRepository<Company> {

}
