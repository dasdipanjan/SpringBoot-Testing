## Unit Testing of SpringBoot Application using Junit5 and Mockito

---

This project describes how to write BDD style unit testing of SpringBoot application using Junit5 and Mockito library.
It is not only describing unit testing, it has also implementation of integration testing with MYSQL. It also shows
how to write integration testing using TestContainer.

Key Topics are

> 1. Junit testing for Repository layer with help of @JpaDataTest and H2 in memory database.
> 2. Junit Testing for Service layer using mockito and assertj library.
> 3. Junit Testing for Rest Controller layer using mockito and assertj library.
> 4. Local Integration Testing with MYSQL database.
> 5. Integration testing using TestContainer.

Important Annotations:

#### 1. @DataJpaTest:

This annotation help us to test Repository layer of a spring boot application. By default it includes H2 inmemory database during test execution.

```
@DataJpaTest
class EmployeeRepositoryTest {
    //Unit test class is annotated with @DataJpaTest
}
```

#### 2. @ExtendWith(MockitoExtension.class)

This annotation is responsible to run unit test which are dependent on Mockito library.

```
@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {
    //Unit test class is annotated with @ExtendWith
    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;
}
```

Without @ExtendWith annotation @Mock and  @InjectMocks annotations will not work.

#### 3. @WebMvcTest

Spring provides this annotation to test Spring MVC Controller layer's components. This annotation does not load Repository ot
Service layer components. It only loads controller layer components.

#### 4. SpringBootTest

SpringBoot provides @SpringBootTest annotation for integration testing. This annotation creates and application
context and loads full application context.

### How to test an exception scenario:

Suppose below method throws an exception due to some condition failure.

```
 @Override
 public Employee saveEmployee(Employee employee) {
      Optional<Employee> savedEmployee = mEmployeeRepository.findByEmail(employee.getEmail());
    if (savedEmployee.isPresent()) {
        throw new ResourceNotFoundException("Employee already exists with given email : " + employee.getEmail());
    }
    return mEmployeeRepository.save(employee);
 }
```

Unit testing for above-mentioned method in exception scenario is below.

```
    // JUnit test for saveEmployee method
    @DisplayName("JUnit test for saveEmployee method which throws exception")
    @Test
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException(){
        // given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee));
        // when -  action or the behaviour that we are going test
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.saveEmployee(employee);
        });
        // then
        verify(employeeRepository, never()).save(any(Employee.class));
    }
```

## Integration Testing

---

![integrationtesting.png]([assets/integrationtesting.png](https://github.com/dasdipanjan/SpringBoot-Testing/blob/main/spring-boot-testing/assets/integrationtesting.png))

![integrationtesting2.png]([assets/integrationtesting2.png](https://github.com/dasdipanjan/SpringBoot-Testing/blob/main/spring-boot-testing/assets/integrationtesting2.png))

### Sample Junit Integration Testing  Class

---
```
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerITest {
    @Autowired
    private MockMvc mockMvc;
}
```
### Data Repository Integration Test Class

---
```
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EmployeeRepositoryITest {

}
```
