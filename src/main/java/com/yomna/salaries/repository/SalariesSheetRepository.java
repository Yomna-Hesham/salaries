package com.yomna.salaries.repository;

import com.yomna.salaries.model.entity.SalariesSheet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalariesSheetRepository extends JpaRepository<SalariesSheet, Integer> {
}
