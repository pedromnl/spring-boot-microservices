package net.javaguides.departmentservice.service.impl;

import lombok.AllArgsConstructor;
import net.javaguides.departmentservice.dto.DepartmentDto;
import net.javaguides.departmentservice.entity.Department;
import net.javaguides.departmentservice.exception.ResourceNotFoundException;
import net.javaguides.departmentservice.mapper.DepartmentMapper;
import net.javaguides.departmentservice.repository.DepartmentRepository;
import net.javaguides.departmentservice.service.DepartmentService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private DepartmentRepository departmentRepository;

    // ModelMapper
    //private ModelMapper modelMapper;

    @Override
    public DepartmentDto saveDepartment(DepartmentDto departmentDto) {

        // Map to employee entity using ModelMapper
        //Department department = modelMapper.map(departmentDto, Department.class);

        // Map to employee entity using MapStruct
        Department department = DepartmentMapper.MAPPER.mapToDepartment(departmentDto);

        Department savedDepartment = departmentRepository.save(department);

        // Map to dto using ModelMapper
        //return modelMapper.map(department, DepartmentDto.class);

        // Map to dto using MapStruct
        return DepartmentMapper.MAPPER.mapToDepartmentDto(savedDepartment);
    }

    @Override
    public DepartmentDto getDepartmentByCode(String departmentCode) {
        Department department = departmentRepository
                .findByDepartmentCode(departmentCode)
                .orElseThrow( () -> new ResourceNotFoundException("Department", "code", departmentCode));

        // Map to dto using ModelMapper
        // return modelMapper.map(department, DepartmentDto.class);

        // Map to dto using MapStruct
        return DepartmentMapper.MAPPER.mapToDepartmentDto(department);
    }
}