package com.yomna.salaries.seeder;

import com.yomna.salaries.model.Company;
import com.yomna.salaries.model.User;
import com.yomna.salaries.repository.CompanyRepository;
import com.yomna.salaries.repository.UserRepository;
import com.yomna.salaries.util.AuthorizationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class Seeder {
    private static final Logger logger = LoggerFactory.getLogger(Seeder.class);

    @Autowired private CompanyRepository companyRepository;
    @Autowired private UserRepository userRepository;

    private List<Company> companies;
    private List<User> users;

    @PostConstruct
    public void seed() {
        logger.info("seed() | Start ...");

        seedCompanies();
        seedUsers();

        logger.info("seed() | ... Finish");
    }

    private void seedCompanies() {
        logger.info("seedCompanies() | Start ...");

        companies = new ArrayList<>();
        companies.add(new Company(1, "ABC"));

        companies = companyRepository.saveAll(companies);
        logger.debug("seedCompanies() | Seeded Companies: {}", companies);

        logger.info("seedCompanies() | ... Finish");
    }

    private void seedUsers() {
        logger.info("seedUsers() | Start ...");

        users = new ArrayList<>();
        users.add(
                new User(1, "Test", "User", "test.user@abc.com", "hashed#password",
                        LocalDateTime.now(),companies.get(0)));

        users = userRepository.saveAll(users);
        logger.debug("seedUsers() | Seeded Users: {}", users);

        AuthorizationUtil.setCurrentAuthorizedUser(users.get(0));

        logger.info("seedUsers() | ... Finish");
    }
}
