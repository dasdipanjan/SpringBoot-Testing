package net.dd.spring.guide.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.dd.spring.guide.springboot.model.Employee;
import net.dd.spring.guide.springboot.service.EmployeeService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import static org.mockito.BDDMockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebMvcTest
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EmployeeService employeeService;
    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;

    @BeforeEach
    public void setup() {
        employee = Employee.builder()
                .firstName("Dipanjan")
                .lastName("Das")
                .email("das.dtx@hotmail.com")
                .build();
    }

    //Junit test for
    @DisplayName("Junit test for creat employee operation.")
    @Test
    public void givenEmployeeController_whenPostCalled_thenVerifySavedEmployee() throws Exception {
        //Given - Precondition or setup.
        given(employeeService.saveEmployee(ArgumentMatchers.any(Employee.class)))
                .willAnswer((invocationOnMock -> invocationOnMock.getArgument(0)));
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
    @DisplayName("Junit testing for retrieving all employees")
    @Test
    public void givenEmployeeController_whenGetAllEmployees_thenVerifyResponse() throws Exception {
        //Given - Precondition or setup.
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(Employee.builder().firstName("Dipanjan").lastName("Das").email("das.dipanjan@hotmail.com").build());
        employeeList.add(Employee.builder().firstName("Satyaranjan").lastName("Das").email("das.satyaranjan@hotmail.com").build());
        given(employeeService.getAllEmployees()).willReturn(employeeList);
        //When - action or behaviour that we are going to test.
        ResultActions response = mockMvc.perform(get("/api/employees"));
        //Then - verify the output
        response.andExpect(status().isOk())
                .andDo(print()).andExpect(jsonPath("$.size()", CoreMatchers.is(employeeList.size())));
    }

    //Junit test for
    @Test
    public void givenEmployeeController_whenGetEmployeeById_thenVerifyResponse() throws Exception {
        //Given - Precondition or setup.
        long employeeId = 1L;
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));
        //When - action or behaviour that we are going to test.
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));
        //Then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }

    //Junit test for
    @Test
    public void givenEmployeeController_whenUpdateEmployee_thenVerifyResponse() throws Exception {
        //Given - Precondition or setup.
        Employee updatedEmployee = Employee.builder()
                .firstName("Bidisa")
                .lastName("Das")
                .email("das.bidisa@hotmail.com")
                .build();
        long employeeId = 1L;
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));
        given(employeeService.updateEmployee(ArgumentMatchers.any(Employee.class)))
                .willAnswer((invocationOnMock -> invocationOnMock.getArgument(0)));
        //When - action or behaviour that we are going to test.
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
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
    @Test
    public void givenEmployeeController_whenUpdateEmployee_thenVerifyResponseNegative() throws Exception {
        //Given - Precondition or setup.
        Employee updatedEmployee = Employee.builder()
                .firstName("Bidisa")
                .lastName("Das")
                .email("das.bidisa@hotmail.com")
                .build();
        long employeeId = 1L;
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
        //When - action or behaviour that we are going to test.
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(updatedEmployee)));
        //Then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    //Junit test for
    @Test
    public void givenEmployeeController_whenDeleteEmployee_thenVerifyResponseNegative() throws Exception {
        //Given - Precondition or setup.
        long employeeId = 1L;
        willDoNothing().given(employeeService).deleteEmployee(employeeId);
        //When - action or behaviour that we are going to test.
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employeeId));
        //Then - verify the output
        response.andExpect(status().isOk()).andDo(print());
    }
}