package com.yomna.salaries.service;

import com.yomna.salaries.repository.IndividualRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IndividualService {
    @Autowired private IndividualRepository repository;
}
