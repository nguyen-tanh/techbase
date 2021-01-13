package com.techbase.interfaces.employee;

import com.techbase.application.EmployeeReportService;
import com.techbase.interfaces.handle.dto.EmployeeReportDTO;
import com.techbase.interfaces.handle.response.APIResponse;
import com.techbase.interfaces.handle.response.PagerResponse;
import com.techbase.interfaces.shared.AbstractAPIController;
import com.techbase.support.binding.Pager;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author nguyentanh
 */
@RestController
public class EmployeeController extends AbstractAPIController {

    private final EmployeeReportService employeeReportService;

    public EmployeeController(EmployeeReportService employeeReportService) {
        this.employeeReportService = employeeReportService;
    }

    @GetMapping("user/employee")
    @Pager
    @ResponseBody
    public APIResponse<PagerResponse<EmployeeReportDTO>> getEmployees(PageRequest pager) {
        PagerResponse<EmployeeReportDTO> employeePage = employeeReportService.reportEmployees(pager);

        APIResponse<PagerResponse<EmployeeReportDTO>> response = new APIResponse<>();
        response.statusMessage(APIResponse.StatusMessage.SUCCESS);
        response.addData(employeePage);

        return response;
    }
}
