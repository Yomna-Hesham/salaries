package com.yomna.salaries.model;

import lombok.*;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class Individual extends Customer{
    public Individual(Integer id, String name, String type) {
        super(id, name, type);
    }
}
