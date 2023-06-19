package net.javaguides.employeeservice.service;

import net.javaguides.employeeservice.dto.DepartmentDto;
import net.javaguides.employeeservice.dto.OrganizationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="ORGANIZATION-SERVICE")
public interface OrganizationAPIClient {
    // Build get department REST API
    @GetMapping("api/organizations/{organization-code}")
    public OrganizationDto getOrganization(@PathVariable("organization-code") String code);
}
