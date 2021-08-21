package com.yomna.salaries.model.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class Company extends Customer{
    @OneToOne @JoinColumn(name = "salaries_account_num") private Account salariesAccount;

    public Company(Integer id, String name, String type, Account salariesAccount) {
        super(id, name, type);
        this.salariesAccount = salariesAccount;
    }
}
