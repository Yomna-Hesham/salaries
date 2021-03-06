package com.yomna.salaries.model.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    @Column(name = "account_num") private String accountNum;
    @Column(name = "type") private String type;
    @Column(name = "currency") private String currency;
    @Column(name = "available_balance") private Double availableBalance;
    @Column(name = "actual_balance") private Double actualBalance;

    @ManyToOne @JoinColumn(name = "customer_id") private Customer customer;
    @ManyToOne @JoinColumn(name = "salary_company_id") private Company salaryCompany;

    public void incrementActualBalance(Double amount) {
        actualBalance += amount;
    }
    public void incrementAvailableBalance(Double amount) {
        availableBalance += amount;
    }

    public void decrementActualBalance(Double amount) {
        actualBalance -= amount;
    }
    public void decrementAvailableBalance(Double amount) {
        availableBalance -= amount;
    }


}
