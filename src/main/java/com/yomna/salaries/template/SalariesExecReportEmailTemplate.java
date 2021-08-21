package com.yomna.salaries.template;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SalariesExecReportEmailTemplate {
    private long totalCount;
    private long failedCount;

    public String toString() {
        return "Salaries Sheet Execution Final Report \n\n " +
                "Total Executions: " + totalCount + "\n" +
                "No of Successful Executions: " + (totalCount - failedCount) + "\n" +
                "No of Failed Executions: " + failedCount + "\n\n" +
                "(You can find the final report attached to this email)";
    }
}
