package net.javaguides.employeeservice.service.impl;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import net.javaguides.employeeservice.dto.ApiResponseDto;
import net.javaguides.employeeservice.dto.DepartmentDto;
import net.javaguides.employeeservice.dto.EmployeeDto;
import net.javaguides.employeeservice.dto.OrganizationDto;
import net.javaguides.employeeservice.entity.Employee;
import net.javaguides.employeeservice.exception.ResourceNotFoundException;
import net.javaguides.employeeservice.mapper.EmployeeMapper;
import net.javaguides.employeeservice.repository.EmployeeRepository;
import net.javaguides.employeeservice.service.DepartmentAPIClient;
import net.javaguides.employeeservice.service.EmployeeService;
import net.javaguides.employeeservice.service.OrganizationAPIClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private EmployeeRepository employeeRepository;

    // ModelMapper
    //private ModelMapper modelMapper;

    // private RestTemplate restTemplate;

    private WebClient webClient;

    private DepartmentAPIClient departmentApiClient;

    private OrganizationAPIClient organizationAPIClient;

    @Override
    public EmployeeDto saveEmployee(EmployeeDto employeeDto) {

        // Map to employee entity using ModelMapper
        //Employee employee = modelMapper.map(employeeDto, Employee.class);

        // Map to employee entity using MapStruct
        Employee employee = EmployeeMapper.MAPPER.mapToEmployee(employeeDto);

        Employee savedEmployee = employeeRepository.save(employee);

        // Map to dto using ModelMapper
        //return modelMapper.map(employee, EmployeeDto.class);

        // Map to dto using MapStruct
        return EmployeeMapper.MAPPER.mapToEmployeeDto(savedEmployee);
    }

    //@CircuitBreaker(name = "${spring.application.name}", fallbackMethod = "getDefaultDepartment")
    @Retry(name = "${spring.application.name}", fallbackMethod = "getDefaultDepartment")
    @Override
    public ApiResponseDto getEmployeeById(Long id) {

        LOGGER.info("inside getEmployeeById method");

        Employee employee = employeeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Employee", "id", id));

        // Map to dto using ModelMapper
        // return modelMapper.map(employee, EmployeeDto.class);

        // REST Template
        //ResponseEntity<DepartmentDto> responseEntity = webClient.getForEntity("http://localhost:8080/api/departments/" + employee.getDepartmentCode(), DepartmentDto.class);
        //DepartmentDto departmentDto = responseEntity.getBody();

        // Web Client
        /*DepartmentDto departmentDto = webClient.get()
                .uri("http://localhost:8080/api/departments/" + employee.getDepartmentCode())
                .retrieve()
                .bodyToMono(DepartmentDto.class)
                .block();*/

        // Spring Cloud Open Feign
        DepartmentDto departmentDto = departmentApiClient.getDepartment(employee.getDepartmentCode());

        OrganizationDto organizationDto = organizationAPIClient.getOrganization(employee.getOrganizationCode());

        // Map to dto using MapStruct
        EmployeeDto employeeDto = EmployeeMapper.MAPPER.mapToEmployeeDto(employee);

        ApiResponseDto apiResponseDto = new ApiResponseDto(
                employeeDto,
                departmentDto,
                organizationDto
        );

        return apiResponseDto;
    }

    public ApiResponseDto getDefaultDepartment(Long id, Exception exception) {

        LOGGER.info("inside getDefaultDepartment method");

        Employee employee = employeeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Employee", "id", id));

        // Map to dto using MapStruct
        EmployeeDto employeeDto = EmployeeMapper.MAPPER.mapToEmployeeDto(employee);

        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setDepartmentName("R&D Department");
        departmentDto.setDepartmentDescription("Research and Development Department");
        departmentDto.setDepartmentCode("RD001");

        OrganizationDto organizationDto = new OrganizationDto();
        organizationDto.setOrganizationName("Default Organization");
        organizationDto.setOrganizationDescription("Default Organization Description");
        organizationDto.setOrganizationCode("DEFAULT_ORG");
        organizationDto.setCreatedDate(LocalDateTime.now());

        ApiResponseDto apiResponseDto = new ApiResponseDto(
                employeeDto,
                departmentDto,
                organizationDto
        );
        return apiResponseDto;
    }
}
