package net.javaguides.employeeservice.service.impl;

import lombok.AllArgsConstructor;
import net.javaguides.employeeservice.dto.EmployeeDto;
import net.javaguides.employeeservice.entity.Employee;
import net.javaguides.employeeservice.mapper.EmployeeMapper;
import net.javaguides.employeeservice.repository.EmployeeRepository;
import net.javaguides.employeeservice.service.EmployeeService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private EmployeeRepository employeeRepository;

    // ModelMapper
    //private ModelMapper modelMapper;

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

    @Override
    public EmployeeDto getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id).get();

        // Map to dto using ModelMapper
        // return modelMapper.map(employee, EmployeeDto.class);

        // Map to dto using MapStruct
        return EmployeeMapper.MAPPER.mapToEmployeeDto(employee);
    }
}
