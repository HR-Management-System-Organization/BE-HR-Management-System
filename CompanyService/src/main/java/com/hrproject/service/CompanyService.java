package com.hrproject.service;


import com.hrproject.dto.request.*;
import com.hrproject.dto.response.*;
import com.hrproject.exception.CompanyManagerException;
import com.hrproject.exception.ErrorType;
import com.hrproject.mapper.ICompanyMapper;
import com.hrproject.repository.ICompanyRepository;
import com.hrproject.repository.IExpenseRepository;
import com.hrproject.repository.IIncomeRepository;
import com.hrproject.repository.entity.Company;
import com.hrproject.repository.entity.Expense;
import com.hrproject.repository.entity.Income;
import com.hrproject.repository.enums.EExpenseStatus;
import com.hrproject.repository.enums.ERole;
import com.hrproject.utility.JwtTokenManager;
import com.hrproject.utility.JwtTokenProvider;
import com.hrproject.utility.ServiceManager;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class CompanyService extends ServiceManager<Company, Long> {
    private final ICompanyRepository companyRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenManager jwtTokenManager;
    private final IExpenseRepository expenseRepository;
    private final IIncomeRepository incomeRepository;


    private CompanyService(ICompanyRepository companyRepository, JwtTokenProvider jwtTokenProvider, JwtTokenManager jwtTokenManager, IExpenseRepository expenseRepository, IIncomeRepository incomeRepository) {
        super(companyRepository);
        this.companyRepository = companyRepository;
        this.jwtTokenProvider = jwtTokenProvider;

        this.jwtTokenManager = jwtTokenManager;
        this.expenseRepository = expenseRepository;
        this.incomeRepository = incomeRepository;
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
    public void defaultCompany() {
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

    public Company findByIdd(Long id) {
        return findById(id).get();
    }



    public Expense maasekle(Long sayi,int maas,String name,String surname,Long company ){

        Double maas1= (double) maas;
        Double tax=null;
        if (maas<=27000)tax=0.15;
        if (maas<=65000&&maas>27000)tax=0.20;
        if (maas<=95000&&maas>65000)tax=0.25;
        else tax=0.35;
        tax=tax*maas1;
        Double brut=tax+maas1;
        LocalDate localDate=LocalDate.now();
        LocalDate sonrakiAyDorduncuGun = localDate
                .plusMonths(1) // Bir sonraki ayı bul
                .withDayOfMonth(4);
        Optional<Expense> expense1=expenseRepository.findAll().stream().filter(a->a.getUserId().equals(sayi)).filter(a->a.getExpenseType().equals("Salary"))
                .filter(a->a.getBillDate().equals(sonrakiAyDorduncuGun)).findFirst();
        if (expense1.isPresent()){
            expenseRepository.delete(expense1.get());
        };
        Expense expense=Expense.builder().expenseType("Salary").netAmount(maas1).billDate(sonrakiAyDorduncuGun).name(name).surname(surname).amount(brut).tax(tax).companyId(company).userId(sayi).build();
        expense.setEExpenseStatus(EExpenseStatus.ACTIVE);
        return expenseRepository.save(expense);

    }
    public Income incomeekle(String gelirtur,Long id,Double gelir,Long comapnyid,String sebep,String name,String surname,String gelirtarihi) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");



        Date firstDate = dateFormat.parse(gelirtarihi);
        LocalDate localDate = firstDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        Income income=Income.builder().incomeType(gelirtur).userId(id).amount(gelir).companyId(comapnyid).aciklama(sebep).name(name).surname(surname).billDate(localDate).build();
        income.setEIncomeStatus(EExpenseStatus.ACTIVE);
        return incomeRepository.save(income);
    }
    public Expense expenseEkle(String harcamaTur,Long id,Double gider,Long comapnyid,String sebep,String name,String surname,String gidertarihi) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date firstDate = dateFormat.parse(gidertarihi);
        LocalDate localDate = firstDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        Expense expense=Expense.builder().expenseType(harcamaTur).userId(id).amount(gider).companyId(comapnyid).description(sebep).name(name).surname(surname).billDate(localDate).build();
        expense.setEExpenseStatus(EExpenseStatus.PENDING);
        return expenseRepository.save(expense);
    }
    public DTOGELIRGIDER dtogelirgider(Long companyid){
        List<Income> incomes= incomeRepository.findAll().stream().filter(a->a.getCompanyId().equals(companyid)).toList();
        List<Expense> expenses= expenseRepository.findAll().stream().filter(a->a.getCompanyId().equals(companyid)).toList();
        LocalDate suankiTarih = LocalDate.now();

        // Şu anki yılı ve ayı al
        int yil = suankiTarih.getYear();
        int ay = suankiTarih.getMonthValue();

        // Belirli ay ve yıl için ilk günü hesapla
        LocalDate ilkGun = LocalDate.of(yil, ay, 1).minusDays(1);




        List<Income> incomes1 =incomes.stream().
                filter(a->a.getBillDate().isAfter(ilkGun)).
                filter(a->a.getBillDate().isBefore(ilkGun.plusDays(8))).toList();
        List<Income> incomes2 =incomes.stream().
                filter(a->a.getBillDate().isAfter(ilkGun.plusDays(7))).
                filter(a->a.getBillDate().isBefore(ilkGun.plusDays(15))).toList();
        List<Income> incomes3 =incomes.stream().
                filter(a->a.getBillDate().isAfter(ilkGun.plusDays(14))).
                filter(a->a.getBillDate().isBefore(ilkGun.plusDays(22))).toList();
        List<Income> incomes4 =incomes.stream().
                filter(a->a.getBillDate().isAfter(ilkGun.plusDays(21))).toList();

        List<Expense> expenses1=expenses.stream().
                filter(a->a.getBillDate().isAfter(ilkGun)).
                filter(a->a.getBillDate().isBefore(ilkGun.plusDays(8))).toList();
        List<Expense> expenses2=expenses.stream().
                filter(a->a.getBillDate().isAfter(ilkGun.plusDays(7))).
                filter(a->a.getBillDate().isBefore(ilkGun.plusDays(15))).toList();
        List<Expense> expenses3=expenses.stream().
                filter(a->a.getBillDate().isAfter(ilkGun.plusDays(14))).
                filter(a->a.getBillDate().isBefore(ilkGun.plusDays(22))).toList();
        List<Expense> expenses4=expenses.stream().

                filter(a->a.getBillDate().isAfter(ilkGun.plusDays(21))).toList();

        List<Double> toplamAmountincomeaylik = incomes.stream()
                .filter(a -> a.getBillDate().isAfter(ilkGun))
                .map(a -> a.getAmount())
                .collect(Collectors.toList());

        Double toplamAmountincomeaylikToplam = toplamAmountincomeaylik.stream().mapToDouble(Double::doubleValue).sum();
        System.out.println(toplamAmountincomeaylikToplam+" toplamAmountincomeaylikToplam");

        List<Double> toplamAmountexpenceaylik = expenses.stream()
                .filter(a -> a.getBillDate().isAfter(ilkGun))
                .map(a -> a.getAmount())
                .collect(Collectors.toList());

        Double toplamAmountexpenceaylikToplam = toplamAmountexpenceaylik.stream().mapToDouble(Double::doubleValue).sum();
        System.out.println(toplamAmountexpenceaylikToplam+" ttoplamAmountexpenceaylikToplam");


        List<Double> toplamAmountincome = incomes.stream()

                .map(a -> a.getAmount())
                .collect(Collectors.toList());

        Double toplamAmountincomeToplam = toplamAmountincome.stream().mapToDouble(Double::doubleValue).sum();

        List<Double> toplamAmountexpence = expenses.stream()

                .map(a -> a.getAmount())
                .collect(Collectors.toList());
        System.out.println("total kazanc"+toplamAmountincomeToplam);

        Double toplamAmountexpenceToplam = toplamAmountexpence.stream().mapToDouble(Double::doubleValue).sum();

        incomes1.stream().map(a -> a.getAmount()).collect(Collectors.toList()).stream().mapToDouble(Double::doubleValue).sum();




        DTOGELIRGIDER dtogelirgider=DTOGELIRGIDER.builder()
                .totalincome1(incomes1.stream().map(a -> a.getAmount()).collect(Collectors.toList()).stream().mapToDouble(Double::doubleValue).sum())
                .totalexpense1(expenses1.stream().map(a -> a.getAmount()).collect(Collectors.toList()).stream().mapToDouble(Double::doubleValue).sum())
                .totalincome2(incomes2.stream().map(a -> a.getAmount()).collect(Collectors.toList()).stream().mapToDouble(Double::doubleValue).sum())
                .totalexpense2(expenses2.stream().map(a -> a.getAmount()).collect(Collectors.toList()).stream().mapToDouble(Double::doubleValue).sum())

                .totalincome3(incomes3.stream().map(a -> a.getAmount()).collect(Collectors.toList()).stream().mapToDouble(Double::doubleValue).sum())
                .totalexpense3(expenses3.stream().map(a -> a.getAmount()).collect(Collectors.toList()).stream().mapToDouble(Double::doubleValue).sum())
                .totalincome4(incomes4.stream().map(a -> a.getAmount()).collect(Collectors.toList()).stream().mapToDouble(Double::doubleValue).sum())
                .totalexpense4(expenses4.stream().map(a -> a.getAmount()).collect(Collectors.toList()).stream().mapToDouble(Double::doubleValue).sum())

                .expense5(expenses.stream().filter(a->a.getBillDate().isAfter(LocalDate.now().minusDays(1))).toList())

                .incomes(incomes).expenses(expenses)
                .income1(incomes1).expense1(expenses1).
                income1(incomes2).expense1(expenses2).
                income1(incomes3).expense1(expenses3).
                income1(incomes4).expense1(expenses4).
                totalincome(toplamAmountincomeToplam).totalexpense(toplamAmountexpenceToplam).
                montlytotalincome(toplamAmountincomeaylikToplam).montlytotalexpense(toplamAmountexpenceaylikToplam).
                build();

        return dtogelirgider;
    }
    public List<Expense> findalloldreguestbycompanymanager(String tokken) {
        System.out.println("burdasinfindbyadim");
        System.out.println(tokken);
        System.out.println("1."+jwtTokenManager.getRoleFromToken(tokken).get());

        System.out.println(jwtTokenManager.getRoleFromToken(tokken).get());


            System.out.println(jwtTokenManager.getIdFromToken(tokken));

            Long id=expenseRepository.findAll().stream().filter(a->a.getUserId().equals(jwtTokenManager.getIdFromToken(tokken).get())).findFirst().get().getCompanyId();


            System.out.println(id);;
            return expenseRepository.findAll().stream()
                    .filter(a->a.getCompanyId().equals(id)).filter(a->a.getBillDate().isAfter(LocalDate.now().minusDays(1))).toList();


    }public Expense avansexpense(Double expense1,String name,String surname,Long companyıd,Long sayi){
        Expense expense=Expense.builder().
                expenseType("Avans").userId(sayi).amount(expense1).name(name).surname(surname).companyId(companyıd).billDate(LocalDate.now()).eExpenseStatus(EExpenseStatus.ACTIVE).build();
        return expenseRepository.save(expense);
    }
    public List<Expense> findallexpensebycompanymanager(String tokken) {
        System.out.println("burdasinfindbyadim");
        System.out.println(tokken);
        System.out.println("1."+jwtTokenManager.getRoleFromToken(tokken).get());

        System.out.println(jwtTokenManager.getRoleFromToken(tokken).get());


        System.out.println(jwtTokenManager.getIdFromToken(tokken));

        Long id=expenseRepository.findAll().stream().filter(a->a.getUserId().equals(jwtTokenManager.getIdFromToken(tokken).get())).findFirst().get().getCompanyId();


        System.out.println(id);;
        return expenseRepository.findAll().stream()
                .filter(a->a.getCompanyId().equals(id)).filter(a->a.getEExpenseStatus().equals(EExpenseStatus.PENDING)).toList();


    }
}
