package com.yomna.salaries.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
public class SalariesSheet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sheet_id") private Integer id;
    @Column(name = "month") private String month;
    @Column(name = "root_path") private String rootPath;
    @Column(name = "relative_path") private String sheetRelativePath;
    @Column(name = "sheet_filename") private String sheetFilename;
    @Column(name = "evaluated_at") private LocalDateTime evaluatedAt;
    @Column(name = "report_file_name") private String reportFileName;

    @ManyToOne @JoinColumn(name = "company_id") private Company company;
    @ManyToOne @JoinColumn(name = "submitted_by") private User submittedBy;
}
