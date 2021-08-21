package com.yomna.salaries.seeder;

import com.yomna.salaries.model.entity.Account;
import com.yomna.salaries.model.entity.Company;
import com.yomna.salaries.model.entity.Individual;
import com.yomna.salaries.model.entity.User;
import com.yomna.salaries.repository.AccountRepository;
import com.yomna.salaries.repository.CompanyRepository;
import com.yomna.salaries.repository.IndividualRepository;
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
    @Autowired private IndividualRepository individualRepository;
    @Autowired private AccountRepository accountRepository;

    private List<Company> companies;
    private List<User> users;
    private List<Individual> individuals;
    private List<Account> accounts;

    @PostConstruct
    public void seed() {
        logger.info("seed() | Start ...");

        seedIndividuals();
        seedCompanies();
        seedUsers();
        seedAccounts();

        logger.info("seed() | ... Finish");
    }

    private void seedIndividuals() {
        logger.info("seedIndividuals() | Start ...");

        individuals = new ArrayList<>();
        individuals.add(new Individual(2, "ahmed", "INDIVIDUAL", "01234567890"));
        individuals.add(new Individual(3, "mahmoud", "INDIVIDUAL", "01234567891"));
        individuals.add(new Individual(4, "saeed", "INDIVIDUAL", "01234567892"));
        individuals.add(new Individual(5, "taha", "INDIVIDUAL", "01234567893"));

        individuals = individualRepository.saveAll(individuals);
        logger.debug("seedIndividuals() | Seeded Individuals: {}", individuals);

        logger.info("seedIndividuals() | ... Finish");

    }

    private void seedCompanies() {
        logger.info("seedCompanies() | Start ...");

        companies = new ArrayList<>();
        companies.add(new Company(1, "ABC", "COMPANY",null));

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

    private void seedAccounts() {
        logger.info("seedAccounts() | Start ...");

        accounts = new ArrayList<>();
        accounts.add(new Account("13-5H4990-AC", "CURRENT", "EGP", 10000000.0, 10000000.0, companies.get(0), null));
        accounts.add(new Account("13-117U4R-AI", "SAVING", "EGP", 2500.0, 3000.0, individuals.get(0), companies.get(0)));
        accounts.add(new Account("13-Q00M68-AI", "SAVING", "USD", 500.0, 500.0, individuals.get(1), companies.get(0)));
        accounts.add(new Account("13-232355-AI", "SAVING", "SAR", 4790.0, 4700.0, individuals.get(2), companies.get(0)));
        accounts.add(new Account("13-K4P707-AI", "SAVING", "EGP", 15400.0, 15400.0, individuals.get(3), companies.get(0)));

        accounts = accountRepository.saveAll(accounts);
        logger.debug("seedAccounts() | Seeded Accounts: {}", accounts);

        companies.get(0).setSalariesAccount(accounts.get(0));
        companyRepository.save(companies.get(0));

        logger.info("seedAccounts() | ... Finish");
    }

}
