package com.hrproject.mapper;

import com.hrproject.dto.request.SalaryDateRequestDto;
import com.hrproject.dto.request.SaveCompanyRequestDto;
import com.hrproject.dto.response.AllCompanyInfosForUserProfileResponseDto;
import com.hrproject.dto.response.ManagerDashboardResponseDto;
import com.hrproject.dto.response.PersonnelCompanyInformationResponseDto;
import com.hrproject.repository.entity.Company;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-10-27T09:09:27+0300",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.2.jar, environment: Java 17.0.9 (Amazon.com Inc.)"
)
@Component
public class ICompanyMapperImpl implements ICompanyMapper {

    @Override
    public Company fromSaveCompanyResponseDtoToCompany(SaveCompanyRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Company.CompanyBuilder<?, ?> company = Company.builder();

        company.companyName( dto.getCompanyName() );
        company.companyNeighbourhood( dto.getCompanyNeighbourhood() );
        company.companyDistrict( dto.getCompanyDistrict() );
        company.companyProvince( dto.getCompanyProvince() );
        company.companyCountry( dto.getCompanyCountry() );
        company.companyBuildingNumber( dto.getCompanyBuildingNumber() );
        company.companyApartmentNumber( dto.getCompanyApartmentNumber() );
        company.taxNumber( dto.getTaxNumber() );
        company.sector( dto.getSector() );
        company.companyPhone( dto.getCompanyPhone() );
        company.companyMail( dto.getCompanyMail() );

        return company.build();
    }

    @Override
    public PersonnelCompanyInformationResponseDto fromCompanyToPersonnelCompanyInformationResponseDto(Company company) {
        if ( company == null ) {
            return null;
        }

        PersonnelCompanyInformationResponseDto.PersonnelCompanyInformationResponseDtoBuilder personnelCompanyInformationResponseDto = PersonnelCompanyInformationResponseDto.builder();

        personnelCompanyInformationResponseDto.companyName( company.getCompanyName() );
        personnelCompanyInformationResponseDto.logo( company.getLogo() );
        List<String> list = company.getHolidayDates();
        if ( list != null ) {
            personnelCompanyInformationResponseDto.holidayDates( new ArrayList<String>( list ) );
        }

        return personnelCompanyInformationResponseDto.build();
    }

    @Override
    public ManagerDashboardResponseDto fromCompanyToManagerDashboardResponseDto(Company company) {
        if ( company == null ) {
            return null;
        }

        ManagerDashboardResponseDto.ManagerDashboardResponseDtoBuilder managerDashboardResponseDto = ManagerDashboardResponseDto.builder();

        managerDashboardResponseDto.companyName( company.getCompanyName() );
        managerDashboardResponseDto.companyBalanceStatus( company.getCompanyBalanceStatus() );
        List<String> list = company.getHolidayDates();
        if ( list != null ) {
            managerDashboardResponseDto.holidayDates( new ArrayList<String>( list ) );
        }

        return managerDashboardResponseDto.build();
    }

    @Override
    public AllCompanyInfosForUserProfileResponseDto fromCompanyToAllCompanyInfosForUserProfileResponseDto(Company company) {
        if ( company == null ) {
            return null;
        }

        AllCompanyInfosForUserProfileResponseDto.AllCompanyInfosForUserProfileResponseDtoBuilder allCompanyInfosForUserProfileResponseDto = AllCompanyInfosForUserProfileResponseDto.builder();

        allCompanyInfosForUserProfileResponseDto.companyName( company.getCompanyName() );
        allCompanyInfosForUserProfileResponseDto.companyNeighbourhood( company.getCompanyNeighbourhood() );
        allCompanyInfosForUserProfileResponseDto.companyDistrict( company.getCompanyDistrict() );
        allCompanyInfosForUserProfileResponseDto.companyProvince( company.getCompanyProvince() );
        allCompanyInfosForUserProfileResponseDto.companyCountry( company.getCompanyCountry() );
        allCompanyInfosForUserProfileResponseDto.companyBuildingNumber( company.getCompanyBuildingNumber() );
        allCompanyInfosForUserProfileResponseDto.companyApartmentNumber( company.getCompanyApartmentNumber() );
        allCompanyInfosForUserProfileResponseDto.companyPostalCode( company.getCompanyPostalCode() );

        return allCompanyInfosForUserProfileResponseDto.build();
    }

    @Override
    public SalaryDateRequestDto fromCompanyToSalaryDateRequestDto(Company company) {
        if ( company == null ) {
            return null;
        }

        SalaryDateRequestDto.SalaryDateRequestDtoBuilder salaryDateRequestDto = SalaryDateRequestDto.builder();

        salaryDateRequestDto.salaryDate( company.getSalaryDate() );

        return salaryDateRequestDto.build();
    }
}
