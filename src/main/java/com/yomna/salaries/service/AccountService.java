package com.yomna.salaries.service;

import com.yomna.salaries.client.CurrencyConversionClient;
import com.yomna.salaries.exception.Exception;
import com.yomna.salaries.exception.NotFoundException;
import com.yomna.salaries.exception.SalaryTransferFailureException;
import com.yomna.salaries.model.Salary;
import com.yomna.salaries.model.entity.Account;
import com.yomna.salaries.model.entity.Company;
import com.yomna.salaries.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class AccountService {
    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Value("${accounts.balance.min}") private Double minBalance;
    @Autowired private AccountRepository repository;
    @Autowired private CurrencyConversionClient currencyConversionClient;

    public List<Salary> transferSalaries(List<Salary> salaries, Company company) {
        salaries.forEach(salary -> {
            try {
                transferSalary(salary, company);
            } catch (SalaryTransferFailureException e) {
                logger.error("transferSalaries() | Salary Transfer Failed: {}", e.getMessage());
                salary.setFailureReason(e.getMessage());
            }
        });

        return salaries;
    }

    private void transferSalary(Salary salary, Company company) {
        try {
            _transferSalary(salary, company);
        } catch (SalaryTransferFailureException e) {
            throw e;
        } catch (Exception e) {
            throw new SalaryTransferFailureException(e.getMessage());
        } catch (java.lang.Exception e) {
            logException(e);
            throw new SalaryTransferFailureException("Failed with unknown reason - contact the admin");
        }
    }

    private void logException(java.lang.Exception e) {
        // Log this unexpected exception somewhere (ex: database) for further technical investigation
    }

    private void _transferSalary(Salary salary, Company company) throws Exception{
        logger.info("transferSalary() | Start ...");
        logger.info("transferSalary() | Salary: {}, Company: {}", salary, company);

        validateSalaryTransfer(salary);
        Account account = getAccount(salary.getAccountNum());
        validateSalaryTransfer(salary, company, account);

        transfer(company.getSalariesAccount(), account, salary.getSalary(), salary.getCurrency());
    }

    private void validateSalaryTransfer(Salary salary) {
        if (salary.getName().equals("")) {
            throw new SalaryTransferFailureException("Missing Customer Name");
        }
        if (salary.getAccountNum().equals("")) {
            throw new SalaryTransferFailureException("Missing Account Number");
        }
        if (salary.getSalary() == null) {
            throw new SalaryTransferFailureException("Missing Salary");
        }
    }

    public Account getAccount(String accountNum) throws NotFoundException{
        logger.info("getAccount() | Start ... accountNum: {}", accountNum);

        Account account = repository.findById(accountNum)
                .orElseThrow(() -> new NotFoundException("No account with num " + accountNum));

        logger.debug("getAccount() | account: {}", account);

        return account;
    }

    private void validateSalaryTransfer(Salary salary, Company company, Account account) {
        if (!account.getCustomer().getName().equals(salary.getName())) {
            throw new SalaryTransferFailureException("Wrong Customer Name");
        }
        if (account.getSalaryCompany() == null) {
            throw new SalaryTransferFailureException("Not a Salaries Account");
        }
        if (!account.getSalaryCompany().equals(company)) {
            throw new SalaryTransferFailureException("Salary account doesn't belong to company: " + company.getName());
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void transfer(Account from, Account to, Double sourceAmount, String currency) {
        logger.info("transfer() | Start ...");
        logger.debug("transfer() | From: {}, To: {}, Amount: {} {}", from, to, sourceAmount, currency);

        currency = currency.equals("") ? to.getCurrency() : currency;
        Double currencyConversionFees = 0.0;
        if (!from.getCurrency().equals(currency)) {
            logger.debug("transfer() | Source Currency NOT equal Required Currency .. Convert Currency");

            sourceAmount = currencyConversionClient.convert(currency, from.getCurrency(), sourceAmount);

            logger.debug("transfer() | Amount after conversion: {}", sourceAmount);
        }

        Double destinationAmount = sourceAmount;
        if (!to.getCurrency().equals(from.getCurrency())) {
            logger.debug("transfer() | Source Currency NOT equal Destination Currency .. Convert Currency");

            destinationAmount = currencyConversionClient.convert(from.getCurrency(), to.getCurrency(), sourceAmount);
            currencyConversionFees = currencyConversionClient.getConversionFees(from.getCurrency(), to.getCurrency(), sourceAmount);

            logger.debug("transfer() | Amount after conversion: {}, Conversion Fees: {}", destinationAmount, currencyConversionFees);
        }

        if (from.getActualBalance() - minBalance < sourceAmount + currencyConversionFees) {
            logger.error("transfer() | Insufficient Fund");
            throw new SalaryTransferFailureException("Insufficient Fund");
        }

        from.decrementActualBalance(sourceAmount + currencyConversionFees);
        from.decrementAvailableBalance(sourceAmount + currencyConversionFees);

        to.incrementActualBalance(destinationAmount);
        to.incrementAvailableBalance(destinationAmount);

        List<Account> updatedAccounts = repository.saveAll(Arrays.asList(from, to));
        logger.debug("transfer() | Updated Accounts: {}", updatedAccounts);
    }
}
