package com.yomna.salaries.model.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id") private Integer id;
    @Column(name = "firstname") private String firstname;
    @Column(name = "lastname") private String lastname;
    @Column(name = "email") private String email;
    @Column(name = "password") private String password;
    @Column(name = "activated_at") private LocalDateTime activatedAt;

    @ManyToOne @JoinColumn(name = "company_id") private Company company;
}
