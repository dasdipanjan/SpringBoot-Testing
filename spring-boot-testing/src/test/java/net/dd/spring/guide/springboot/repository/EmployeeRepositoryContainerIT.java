package net.dd.spring.guide.springboot.repository;

import lombok.extern.log4j.Log4j2;
import net.dd.spring.guide.springboot.integraton.AbstractContainerBaseTest;
import net.dd.spring.guide.springboot.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Log4j2
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EmployeeRepositoryContainerIT extends AbstractContainerBaseTest {
    @Autowired
    private EmployeeRepository mRepository;
    private Employee employee;

    @BeforeEach
    public void setup() {
        //Given -> Precondition or setup.
        employee = Employee.builder()
                .firstName("Dipanjan")
                .lastName("Das")
                .email("dipanjan@hotmail.com")
                .build();
        mRepository.deleteAll();
    }

    //Junit test for save employee operation.
    @DisplayName("Junit test for save employee operation.")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {
        //When -> action or behaviour that we are going to test.
        Employee savedEmployee = mRepository.save(employee);
        //Then - verify the output
        log.info("Expected : {}{}{}", "Valid Employee Object", "Actual: ", employee.toString());
        assertThat(savedEmployee).isNotNull();
        log.info("Expected : {}{}{}", "Employee ID is greater than zero", "Actual: ", savedEmployee.getId());
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    //Junit test for
    @DisplayName("Junit test for retrieve all employee operation.")
    @Test
    public void givenEmployeeList_whenFindAll_thenFoundEmployeeList() {
        //Given - Precondition or setup.
        Employee employee2 = Employee.builder()
                .firstName("Surjyatapa")
                .lastName("Das")
                .email("surjyatapa@hotmail.com")
                .build();
        mRepository.save(employee);
        mRepository.save(employee2);
        //When - action or behaviour that we are going to test.
        List<Employee> employeeList = mRepository.findAll();
        //Then - verify the output
        log.info("Expected : {}{}{}", "Valid Employee Object", "Actual: ", employeeList.toString());
        assertThat(employeeList).isNotNull();
        log.info("Expected : {}{}", "2", employeeList.size());
        assertThat(employeeList.size()).isEqualTo(2);
    }

    //Junit test for get employee by id operation.
    @DisplayName("Junit test for get employee by id operation.")
    @Test
    public void givenEmployeeObject_whenFindById_thenFoundEmployeeObject() {
        mRepository.save(employee);
        //When - action or behaviour that we are going to test.
        Employee employeeDB = mRepository.findById(employee.getId()).get();
        //Then - verify the output
        assertThat(employeeDB).isNotNull();
    }

    //Junit test for retrieve employee by email Operation.
    @DisplayName("Junit test for retrieve employee by email operaton")
    @Test
    public void givenEmployeeEmail_whenFindByEmail_thenFoundEmployeeObject() {
        //Given - Precondition or setup.
        mRepository.save(employee);
        //When - action or behaviour that we are going to test.
        Employee employeeByEmail = mRepository.findByEmail(employee.getEmail()).get();
        //Then - verify the output
        assertThat(employeeByEmail).isNotNull();
        assertThat(employeeByEmail.getEmail()).isEqualTo(employee.getEmail());
    }

    //Junit test for update employee operation
    @DisplayName("Junit test for update employee operation")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        //Given - Precondition or setup.
        mRepository.save(employee);
        //When - action or behaviour that we are going to test.
        Employee savedEmployee = mRepository.findById(employee.getId()).get();
        savedEmployee.setEmail("surjya@gmail.com");
        savedEmployee.setFirstName("Ram");
        Employee updatedEmployee = mRepository.save(savedEmployee);
        //Then - verify the output
        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getEmail()).isEqualTo("surjya@gmail.com");
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Ram");
    }

    //Junit test for employee delete operation.
    @DisplayName("Junit test for employee delete operation")
    @Test
    public void givenEmployeeObject_whenDelete_thenRemovedEmployeeObject() {
        //Given - Precondition or setup.
        mRepository.save(employee);
        //When - action or behaviour that we are going to test.
        mRepository.delete(employee);
        Optional<Employee> employeeOptional = mRepository.findById(employee.getId());
        //Then - verify the output
        assertThat(employeeOptional).isEmpty();
    }

    //Junit test for custom Query using JPQL with index.
    @DisplayName("Junit test for custom Query using JPQL with index.")
    @Test
    public void givenFirstNameLastName_whenFindByJPQLIndex_thenReturnEmployeeOject() {
        //Given - Precondition or setup.
        mRepository.save(employee);
        //When - action or behaviour that we are going to test.
        Employee savedEmployee = mRepository.findByJPQLIndexParam(employee.getFirstName(), employee.getLastName());
        //Then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getFirstName()).isEqualTo(employee.getFirstName());
    }


    //Junit test for custom Query using JPQL with named parameter.
    @DisplayName("Junit test for custom Query using JPQL with named parameter.")
    @Test
    public void givenFirstNameLastName_whenFindByJPQLNamedParam_thenReturnEmployeeOject() {
        //Given - Precondition or setup.
        mRepository.save(employee);
        //When - action or behaviour that we are going to test.
        Employee savedEmployee = mRepository.findByJPQLNamedParam(employee.getFirstName(), employee.getLastName());
        //Then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getFirstName()).isEqualTo(employee.getFirstName());
    }

    //Junit test for custom Query using JPQL with native SQL index param.
    @DisplayName("Junit test for custom Query using JPQL with native SQL index param.")
    @Test
    public void givenFirstNameLastName_whenFindByNativeSQLIndexParam_thenReturnEmployeeOject() {
        //Given - Precondition or setup.
        mRepository.save(employee);
        //When - action or behaviour that we are going to test.
        Employee savedEmployee = mRepository.findByNativeSQLIndexParam(employee.getFirstName(), employee.getLastName());
        //Then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getFirstName()).isEqualTo(employee.getFirstName());
    }

    //Junit test for custom Query using JPQL with native SQL named param.
    @DisplayName("Junit test for custom Query using JPQL with native SQL named param.")
    @Test
    public void givenFirstNameLastName_whenFindByNativeSQLNamedParam_thenReturnEmployeeOject() {
        //Given - Precondition or setup.
        mRepository.save(employee);
        //When - action or behaviour that we are going to test.
        Employee savedEmployee = mRepository.findByNativeSQLNamedParam(employee.getFirstName(), employee.getLastName());
        //Then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getFirstName()).isEqualTo(employee.getFirstName());
    }
}