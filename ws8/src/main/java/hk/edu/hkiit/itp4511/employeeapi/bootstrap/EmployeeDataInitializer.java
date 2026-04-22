package hk.edu.hkiit.itp4511.employeeapi.bootstrap;

import hk.edu.hkiit.itp4511.employeeapi.model.Employee;
import hk.edu.hkiit.itp4511.employeeapi.repository.EmployeeRepository;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class EmployeeDataInitializer implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;

    public EmployeeDataInitializer(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void run(String... args) {
        if (employeeRepository.count() == 0) {
            employeeRepository.saveAll(List.of(
                    new Employee(null, "Alice", "Wong", "alice.wong@example.com"),
                    new Employee(null, "Brian", "Chan", "brian.chan@example.com"),
                    new Employee(null, "Cathy", "Lee", "cathy.lee@example.com")
            ));
        }
    }
}
