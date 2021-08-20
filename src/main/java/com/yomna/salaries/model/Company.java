package com.yomna.salaries.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "company")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id") private Integer id;
    @Column(name = "name") @NonNull private String name;
}
