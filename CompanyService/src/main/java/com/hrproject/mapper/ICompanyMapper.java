package com.hrproject.mapper;


import com.hrproject.dto.request.SalaryDateRequestDto;
import com.hrproject.dto.request.SaveCompanyRequestDto;
import com.hrproject.dto.response.*;
import com.hrproject.repository.entity.Company;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ICompanyMapper {
    ICompanyMapper INSTANCE = Mappers.getMapper(ICompanyMapper.class);

    Company fromSaveCompanyResponseDtoToCompany(final SaveCompanyRequestDto dto);

    PersonnelCompanyInformationResponseDto fromCompanyToPersonnelCompanyInformationResponseDto(final Company company);

    ManagerDashboardResponseDto fromCompanyToManagerDashboardResponseDto(final Company company);
    AllCompanyInfosForUserProfileResponseDto fromCompanyToAllCompanyInfosForUserProfileResponseDto(final Company company);
    SalaryDateRequestDto fromCompanyToSalaryDateRequestDto(final Company company);
}
