package com.techbase.application;

import com.techbase.interfaces.handle.dto.EmployeeReportDTO;
import com.techbase.interfaces.handle.response.PagerResponse;
import org.springframework.data.domain.PageRequest;

/**
 * @author nguyentanh
 */
public interface EmployeeReportService {

    PagerResponse<EmployeeReportDTO> reportEmployees(PageRequest pageRequest);
}
