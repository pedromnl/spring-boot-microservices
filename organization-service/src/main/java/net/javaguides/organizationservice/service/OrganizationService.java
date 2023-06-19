package net.javaguides.organizationservice.service;

import net.javaguides.organizationservice.dto.OrganizationDto;
import org.springframework.stereotype.Service;

public interface OrganizationService {
    OrganizationDto saveOrganization(OrganizationDto organizationDto);

    OrganizationDto getOrganizationByCode(String organizationCode);
}
