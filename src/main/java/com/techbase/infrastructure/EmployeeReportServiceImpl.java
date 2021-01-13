package com.techbase.infrastructure;

import com.techbase.application.EmployeeReportService;
import com.techbase.interfaces.handle.dto.EmployeeReportDTO;
import com.techbase.interfaces.handle.response.PagerResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import java.util.ArrayList;
import java.util.List;

/**
 * @author nguyentanh
 */
@Repository
public class EmployeeReportServiceImpl extends AbstractEntityManagerSupport implements EmployeeReportService {

    @Override
    @SuppressWarnings("unchecked")
    public PagerResponse<EmployeeReportDTO> reportEmployees(PageRequest pageRequest) {

        String query = "SELECT " +
                " e.NAME AS empName, r.NAME AS roleName, " +
                "    GROUP_CONCAT(DISTINCT COALESCE(t.name,'') SEPARATOR ', ') AS team, " +
                "    GROUP_CONCAT(DISTINCT CONCAT(COALESCE(d.NAME,''), COALESCE(dd.NAME,'')) SEPARATOR ', ') AS depart " +
                "FROM EMPLOYEE e " +
                "INNER JOIN ROLE r ON e.ROLE_ID=r.ID " +
                "LEFT JOIN EMP_TEAM et ON e.ID=et.EMPLOYEE_ID " +
                "LEFT JOIN TEAM t ON et.TEAM_ID=t.ID " +
                "LEFT JOIN DEPARTMENT d ON t.DEPARTMENT_ID=d.ID " +
                "LEFT JOIN DEPARTMENT dd ON e.ID=dd.MANAGER_ID " +
                "GROUP BY e.NAME, e.ROLE_ID ";


        String countQuery = "SELECT COUNT(*) FROM ( " + query + " ) tbl";

        List<Tuple> tuples = entityManager().createNativeQuery(query, Tuple.class)
                .setFirstResult(pageRequest.getPageNumber() * pageRequest.getPageSize())
                .setMaxResults(pageRequest.getPageSize())
                .getResultList();

        List<EmployeeReportDTO> employeeReportDTOS = new ArrayList<>();
        for (Tuple tuple : tuples) {
            EmployeeReportDTO employeeReportDTO = new EmployeeReportDTO(
                    tuple.get("empName", String.class),
                    tuple.get("roleName", String.class),
                    tuple.get("team", String.class),
                    tuple.get("depart", String.class)
            );

            employeeReportDTOS.add(employeeReportDTO);
        }

        Number count = (Number) entityManager().createNativeQuery(countQuery).getSingleResult();

        return new PagerResponse<>(employeeReportDTOS,
                count.longValue(),
                pageRequest.getPageNumber() + 1,
                pageRequest.getPageSize()
        );
    }
}
