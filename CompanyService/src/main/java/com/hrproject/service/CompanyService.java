package com.hrproject.service;


import com.hrproject.dto.request.*;
import com.hrproject.dto.response.*;
import com.hrproject.exception.CompanyManagerException;
import com.hrproject.exception.ErrorType;
import com.hrproject.mapper.ICompanyMapper;
import com.hrproject.repository.ICompanyRepository;
import com.hrproject.repository.entity.Company;
import com.hrproject.repository.enums.ERole;
import com.hrproject.utility.JwtTokenProvider;
import com.hrproject.utility.ServiceManager;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class CompanyService extends ServiceManager<Company, Long> {
    private final ICompanyRepository companyRepository;
    private final JwtTokenProvider jwtTokenProvider;


    private CompanyService(ICompanyRepository companyRepository, JwtTokenProvider jwtTokenProvider) {
        super(companyRepository);
        this.companyRepository = companyRepository;
        this.jwtTokenProvider = jwtTokenProvider;

    }

    public Long save(SaveCompanyRequestDto dto) {
        if (!companyRepository.existsByCompanyNameIgnoreCase(dto.getCompanyName()) && !companyRepository.existsByTaxNumber(dto.getTaxNumber())) {
            Company company = ICompanyMapper.INSTANCE.fromSaveCompanyResponseDtoToCompany(dto);

            return save(company).getCompanyId();
        }
        throw new CompanyManagerException(ErrorType.COMPANY_ALREADY_EXIST);
    }
    public Long updateCompany(CompanyUpdateRequestDto dto) {
        Long companyId = jwtTokenProvider.getCompanyIdFromToken(dto.getToken())
                .orElseThrow(() -> new CompanyManagerException(ErrorType.COMPANY_NOT_FOUND));
        Optional<Company> companyProfile = companyRepository.findById(companyId);
        List<String> roles = jwtTokenProvider.getRoleFromToken(dto.getToken());
        if (roles.isEmpty()) {
            throw new CompanyManagerException(ErrorType.USER_NOT_FOUND);
        }
        if (roles.contains(ERole.MANAGER.toString())) {
            if (companyProfile.isPresent()) {
                Company company = companyProfile.get();
                company.setCompanyMail(dto.getCompanyMail());
                company.setCompanyPhone(dto.getCompanyPhone());
                company.setCompanyCountry(dto.getCompanyCountry());
                company.setCompanyNeighbourhood(dto.getCompanyNeighbourhood());
                company.setCompanyDistrict(dto.getCompanyDistrict());
                company.setCompanyBuildingNumber(dto.getCompanyBuildingNumber());
                company.setCompanyApartmentNumber(dto.getCompanyApartmentNumber());
                company.setSector(dto.getSector());
                company.setCompanyProvince(dto.getCompanyProvince());
                return update(company).getCompanyId();
            } else {
                throw new CompanyManagerException(ErrorType.USER_NOT_FOUND);
            }
        } else {
            throw new CompanyManagerException(ErrorType.AUTHORIZATION_ERROR);
        }
    }

    public CompanyUpdateRequestDto showCompanyInformation(String token) {
        Long companyId = jwtTokenProvider.getCompanyIdFromToken(token).orElseThrow(() -> new CompanyManagerException(ErrorType.INVALID_TOKEN));

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyManagerException(ErrorType.COMPANY_NOT_FOUND));
        CompanyUpdateRequestDto companyInfo = new CompanyUpdateRequestDto();
        companyInfo.setCompanyName(company.getCompanyName());
        companyInfo.setCompanyPhone(company.getCompanyPhone());
        companyInfo.setCompanyMail(company.getCompanyMail());
        companyInfo.setTaxNumber(company.getTaxNumber());
        companyInfo.setCompanyCountry(company.getCompanyCountry());
        companyInfo.setCompanyProvince(company.getCompanyProvince());
        companyInfo.setCompanyDistrict(company.getCompanyDistrict());
        companyInfo.setCompanyNeighbourhood(company.getCompanyNeighbourhood());
        companyInfo.setCompanyBuildingNumber(company.getCompanyBuildingNumber());
        companyInfo.setCompanyApartmentNumber(company.getCompanyApartmentNumber());
        companyInfo.setSector(company.getSector());

        return companyInfo;
    }





    public PersonnelCompanyInformationResponseDto getPersonnelCompanyInformation(Long companyId) {
        Company company = findById(companyId).orElseThrow(() -> {
            throw new CompanyManagerException(ErrorType.COMPANY_NOT_FOUND);
        });
        PersonnelCompanyInformationResponseDto dto = ICompanyMapper.INSTANCE.fromCompanyToPersonnelCompanyInformationResponseDto(company);
        if (company.getLogo() != null) {
            try {
                byte[] decodedBytes = Base64.getDecoder().decode(company.getLogo());
                String decodedLogo = new String(decodedBytes);
                dto.setLogo(decodedLogo);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
        return dto;
    }



//    public ManagerDashboardResponseDto getManagerDashboardInformation(String token) {
//        Long authId = jwtTokenProvider.getIdFromToken(token).orElseThrow(() -> {
//            throw new CompanyManagerException(ErrorType.USER_NOT_FOUND);
//        });
//        List<String> roles = jwtTokenProvider.getRoleFromToken(token);
//        if (roles.isEmpty())
//            throw new CompanyManagerException(ErrorType.USER_NOT_FOUND);
//        if (roles.contains(ERole.MANAGER.toString())) {
//            UserProfileManagerDashboardResponseDto dtoUser = userManager.getUserProfileManagerDashboard(authId).getBody();
//            Company company = findById(dtoUser.getCompanyId()).orElseThrow(() -> {
//                throw new CompanyManagerException(ErrorType.COMPANY_NOT_FOUND);
//            });
//            ManagerDashboardResponseDto managerDto = ICompanyMapper.INSTANCE.fromCompanyToManagerDashboardResponseDto(company);
//            managerDto.setCompanyPersonnelCount(dtoUser.getCompanyPersonnelCount());
//            return managerDto;
//        }
//        throw new CompanyManagerException(ErrorType.NO_AUTHORIZATION);
//    }


    public SalaryDateRequestDto getSalaryDateResponseDto(Long companyId) {
        Company company = findById(companyId).orElseThrow(() -> {
            throw new CompanyManagerException(ErrorType.COMPANY_NOT_FOUND);
        });
        try {
            Date salaryDate = new SimpleDateFormat("dd-MM-yyyy").parse(company.getSalaryDate());
            long millis = System.currentTimeMillis();
            java.sql.Date date = new java.sql.Date(millis);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            if (salaryDate.before(date)) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(salaryDate);
                Calendar millisCalendar = Calendar.getInstance();
                millisCalendar.setTimeInMillis(millis);
                calendar.set(Calendar.MONTH, millisCalendar.get(Calendar.MONTH));
                calendar.set(Calendar.YEAR, millisCalendar.get(Calendar.YEAR));
                salaryDate = calendar.getTime();
            }
            String formattedDate = dateFormat.format(salaryDate);
            if (!formattedDate.equals(company.getSalaryDate())) {
                company.setSalaryDate(formattedDate);
                update(company);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return ICompanyMapper.INSTANCE.fromCompanyToSalaryDateRequestDto(company);
    }






    @PostConstruct
    public void defaultCompany(){
        save(Company.builder()
                .companyName("A Şirketi")
                .companyCountry("Türkiye")
                .companyProvince("Ankara")
                .companyDistrict("")
                .companyNeighbourhood("")
                .companyMail("asirketi@asirketi.com")
                .companyPhone("555-5-555")
                .companyPostalCode(6500)
                .taxNumber("000-000-001")
                .companyBalanceStatus(500000000D)
                .companyApartmentNumber(7)
                .companyBuildingNumber(145)
                .sector("Construction")
                .build());
        save(Company.builder()
                .companyName("B Şirketi")
                .companyCountry("Türkiye")
                .companyProvince("İstanbul")
                .companyDistrict("")
                .companyNeighbourhood("")
                .companyMail("bsirketi@bsirketi.com")
                .companyPhone("444-0-444")
                .companyPostalCode(34500)
                .taxNumber("000-000-002")
                .companyBalanceStatus(377000000D)
                .companyApartmentNumber(65)
                .companyBuildingNumber(43)
                .sector("Software")
                .build());
    }


    public List<PublicHolidaysRequestDto> getPublicHolidays() {
        List<PublicHolidaysRequestDto> listHoliday = new ArrayList<>();

        listHoliday.add(createHoliday(1L, "Yılbaşı", "01.01.2024", "Miladi takvimin ilk günü"));
        listHoliday.add(createHoliday(2L, "Ulusal Egemenlik ve Çocuk Bayramı", "23.04.2024", "Ulusal Egemenlik ve Çocuk Bayramının kutlandığı gündür"));
        listHoliday.add(createHoliday(3L, "Atatürk’ü Anma, Gençlik ve Spor Bayramı", "19.05.2024", "Atatürk’ü Anma, Gençlik ve Spor Bayramı'dır."));
        listHoliday.add(createHoliday(4L, "Zafer Bayramı", "30.08.2024", "Zafer Bayramıdır."));
        listHoliday.add(createHoliday(5L, "Cumhuriyet Bayramı", "29.10.2024", "Cumhuriyet Bayramı'dır."));

        return listHoliday;
    }
    private PublicHolidaysRequestDto createHoliday(Long id, String name, String date, String description) {
        PublicHolidaysRequestDto holiday = new PublicHolidaysRequestDto();
        holiday.setId(id);
        holiday.setName(name);
        holiday.setDate(date);
        holiday.setDescription(description);
        return holiday;
    }

}
