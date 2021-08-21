package com.yomna.salaries.model.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Inheritance(strategy = InheritanceType.JOINED)
public class Customer {
    @Id
    @Column(name = "customer_id") private Integer id;
    @Column(name = "name") private String name;
    @Column(name = "type") private String type;
}
