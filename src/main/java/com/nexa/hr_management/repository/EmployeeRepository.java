package com.nexa.hr_management.repository;

import com.nexa.hr_management.dto.DepartmentEmployeeCount;
import com.nexa.hr_management.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    /**
     * Search employees by various fields (display functionality)
     */
    @Query("SELECT e FROM Employee e WHERE " +
            "lower(e.firstName) LIKE lower(concat('%', :searchTerm, '%')) OR " +
            "lower(e.lastName) LIKE lower(concat('%', :searchTerm, '%')) OR " +
            "lower(e.position) LIKE lower(concat('%', :searchTerm, '%')) OR " +
            "lower(e.department) LIKE lower(concat('%', :searchTerm, '%')) OR " +
            "lower(e.role) LIKE lower(concat('%', :searchTerm, '%')) OR " +
            "lower(e.qualification) LIKE lower(concat('%', :searchTerm, '%')) OR " +
            "CAST(e.emp_id AS string) LIKE concat('%', :searchTerm, '%')")
    List<Employee> searchEmployees(@Param("searchTerm") String searchTerm);

    long count();

    @Query("SELECT NEW com.nexa.hr_management.dto.DepartmentEmployeeCount(e.department, COUNT(e.emp_id)) " +
            "FROM Employee e " +
            "WHERE e.department IS NOT NULL AND e.department <> '' " +
            "GROUP BY e.department")
    List<DepartmentEmployeeCount> countEmployeesByDepartment();

    @Query("SELECT COUNT(DISTINCT e.department) FROM Employee e " +
            "WHERE e.department IS NOT NULL AND e.department <> ''")
    long countDistinctDepartments();

    boolean existsByEmail(String email);

    Optional<Employee> findByEmail(String email);
}