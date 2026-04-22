package hk.edu.hkiit.itp4511.employeeapi.service;

import hk.edu.hkiit.itp4511.employeeapi.exception.EmployeeNotFoundException;
import hk.edu.hkiit.itp4511.employeeapi.model.Employee;
import hk.edu.hkiit.itp4511.employeeapi.repository.EmployeeRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional(readOnly = true)
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Employee findById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    public Employee create(Employee employee) {
        employee.setId(null);
        return employeeRepository.save(employee);
    }

    public Employee update(Long id, Employee employee) {
        Employee existingEmployee = findById(id);
        existingEmployee.setFirstName(employee.getFirstName());
        existingEmployee.setLastName(employee.getLastName());
        existingEmployee.setEmail(employee.getEmail());
        return employeeRepository.save(existingEmployee);
    }

    public void delete(Long id) {
        Employee existingEmployee = findById(id);
        employeeRepository.delete(existingEmployee);
    }
}
