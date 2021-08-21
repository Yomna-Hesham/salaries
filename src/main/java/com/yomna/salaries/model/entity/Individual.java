package com.yomna.salaries.model.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class Individual extends Customer{
    @Column(name = "mobile") private String mobile;

    public Individual(Integer id, String name, String type, String mobile) {
        super(id, name, type);
        this.mobile = mobile;
    }
}
