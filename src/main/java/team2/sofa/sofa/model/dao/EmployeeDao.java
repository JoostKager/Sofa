package team2.sofa.sofa.model.dao;

import org.springframework.data.repository.CrudRepository;
import team2.sofa.sofa.model.Employee;

public interface EmployeeDao extends CrudRepository<Employee, Integer> {

    Employee findEmployeeByUsername(String username);
}

