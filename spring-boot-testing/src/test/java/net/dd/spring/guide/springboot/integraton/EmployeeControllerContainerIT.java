package net.dd.spring.guide.springboot.integraton;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.dd.spring.guide.springboot.model.Employee;
import net.dd.spring.guide.springboot.repository.EmployeeRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public class EmployeeControllerContainerIT extends AbstractContainerBaseTest{
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        employeeRepository.deleteAll();
    }

    //Junit test for
    @DisplayName("Integration test for create employee")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenSavedEmployee() throws Exception {
        //Given - Precondition or setup.
        Employee employee = Employee.builder()
                .firstName("Dipanjan")
                .lastName("Das")
                .email("das.dtx@hotmail.com")
                .build();
        //When - action or behaviour that we are going to test.
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(employee)));
        //Then - verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }

    //Junit test for
    @DisplayName("Integration test for get all employee")
    @Test
    public void givenEmployeeObjects_whenGetAllEmployees_thenSavedEmployees() throws Exception {
        //Given - Precondition or setup.
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(Employee.builder().firstName("Dipanjan").lastName("Das").email("das.dipanjan@hotmail.com").build());
        employeeList.add(Employee.builder().firstName("Satyaranjan").lastName("Das").email("das.satyaranjan@hotmail.com").build());
        employeeRepository.saveAll(employeeList);
        //When - action or behaviour that we are going to test.
        ResultActions response = mockMvc.perform(get("/api/employees"));
        //Then - verify the output
        response.andExpect(status().isOk())
                .andDo(print()).andExpect(jsonPath("$.size()", CoreMatchers.is(employeeList.size())));
    }

    //Junit test for
    @DisplayName("Integration test for get employee by id")
    @Test
    public void givenEmployeeController_whenGetEmployeeById_thenVerifyResponse() throws Exception {
        //Given - Precondition or setup.
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Dipanjan")
                .lastName("Das")
                .email("das.dtx@hotmail.com")
                .build();
        employeeRepository.save(employee);
        //When - action or behaviour that we are going to test.
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employee.getId()));
        //Then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }

    //Junit test for
    @DisplayName("Integration test for update employee in positive case")
    @Test
    public void givenEmployeeController_whenUpdateEmployee_thenVerifyResponse() throws Exception {
        //Given - Precondition or setup.
        Employee employee = Employee.builder()
                .firstName("Dipanjan")
                .lastName("Das")
                .email("das.dtx@hotmail.com")
                .build();
        employeeRepository.save(employee);
        Employee updatedEmployee = Employee.builder()
                .firstName("Bidisa")
                .lastName("Das")
                .email("das.bidisa@hotmail.com")
                .build();

        //When - action or behaviour that we are going to test.
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(updatedEmployee)));
        //Then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", CoreMatchers.is(updatedEmployee.getEmail())));
    }

    //Junit test for
    @DisplayName("Integration test for update employee negetive case")
    @Test
    public void givenEmployeeController_whenUpdateEmployee_thenVerifyResponseNegative() throws Exception {
        //Given - Precondition or setup.
        Employee updatedEmployee = Employee.builder()
                .firstName("Bidisa")
                .lastName("Das")
                .email("das.bidisa@hotmail.com")
                .build();
        long employeeId = 1L;
        //When - action or behaviour that we are going to test.
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(updatedEmployee)));
        //Then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    //Junit test for
    @DisplayName("Integration test for delete employee")
    @Test
    public void givenEmployeeController_whenDeleteEmployee_thenVerifyResponseNegative() throws Exception {
        //Given - Precondition or setup.
        Employee employee = Employee.builder()
                .firstName("Bidisa")
                .lastName("Das")
                .email("das.bidisa@hotmail.com")
                .build();
        employeeRepository.save(employee);
        //When - action or behaviour that we are going to test.
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employee.getId()));
        //Then - verify the output
        response.andExpect(status().isOk()).andDo(print());
    }

}
