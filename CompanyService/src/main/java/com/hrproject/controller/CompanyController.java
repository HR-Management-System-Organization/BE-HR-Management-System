package com.hrproject.controller;

import com.hrproject.dto.request.*;
import com.hrproject.dto.response.*;
import com.hrproject.repository.entity.Company;
import com.hrproject.repository.entity.Expense;
import com.hrproject.service.CompanyService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.hrproject.constants.EndPoints.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(COMPANY)
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @PostMapping("/save")
    @Operation(summary = "Şirket kaydeder.")
    public ResponseEntity<Long> saveCompanyRequestDto(@RequestBody SaveCompanyRequestDto dto) {
        return ResponseEntity.ok(companyService.save(dto));
    }

    @PutMapping("/update/company")
    @Operation(summary = "Şirket günceller.")
    public ResponseEntity<Long> updateCompanyRequestDto(@RequestBody CompanyUpdateRequestDto dto) {
        return ResponseEntity.ok(companyService.updateCompany(dto));
    }

    @GetMapping("/show-company-information/{token}")
    public ResponseEntity<CompanyUpdateRequestDto> showCompanyInformation(@PathVariable String token) {
        return ResponseEntity.ok(companyService.showCompanyInformation(token));
    }


    @GetMapping("/get-personnel-company-information/{companyId}")
    @Operation(summary = "Id'si sorgulanan şirketin logosu ve şirketin ismiyle birlikte personel izin günlerini getirir.")
    public ResponseEntity<PersonnelCompanyInformationResponseDto> getPersonnelCompanyInformation(@PathVariable Long companyId) {
        return ResponseEntity.ok(companyService.getPersonnelCompanyInformation(companyId));
    }


//    @Hidden
//    @GetMapping("get-manager-dashboard-information/{token}")
//    public ResponseEntity<ManagerDashboardResponseDto> getManagerDashboardInformation(@PathVariable String token){
//        return ResponseEntity.ok(companyService.getManagerDashboardInformation(token));
//    }


    @GetMapping("/get-salarydate-with-company-id/{companyId}")
    ResponseEntity<SalaryDateRequestDto> getCompanyNameAndWageDateResponseDto(@PathVariable Long companyId) {
        return ResponseEntity.ok(companyService.getSalaryDateResponseDto(companyId));
    }


    @GetMapping(HOLIDAYS)
    public ResponseEntity<List<PublicHolidaysRequestDto>> getPublicHoliday() {
        return ResponseEntity.ok(companyService.getPublicHolidays());
    }

    @GetMapping(FINDBYID + "/{id}")
    public ResponseEntity<Company> findByCompanyId(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.findByIdd(id));
    }

    @GetMapping("findall")
    public ResponseEntity<List<Company>> getAllCompanies() {
        return ResponseEntity.ok(companyService.findAll());
    }
    @PostMapping("/addsalary")
    public void addsalary(
            @RequestParam Integer authorId,@RequestParam Integer maas,String name,String surname,@RequestParam Integer companyid
    ) {
        System.out.println(authorId);
        String token = null;

        System.out.println("burdayim");
        Long longSayi = (long) authorId; // int'i Long'a dönüştür
        Long companyid1= (long) companyid;
        companyService.maasekle(longSayi,maas,name,surname,companyid1);




    }
    @PostMapping("/addincome")
    public void addincome(
            @RequestParam Integer companyid,@RequestParam String gelir,@RequestParam String sebep,@RequestParam String gelirtur,@RequestParam String gelirtarihi, @RequestParam Integer id,String name,String surname
    ) throws ParseException {
        System.out.println(id);
        String token = null;
        int integerNumber = Integer.parseInt(gelir); // String'i Integer'a dönüştürme
        Double gelir1= (double) integerNumber;
        System.out.println("burdayim");
        Long longSayi = (long) id; // int'i Long'a dönüştür
        Long companyid1= (long) companyid;
        companyService.incomeekle(gelirtur,longSayi,gelir1,companyid1,sebep,name,surname,gelirtarihi);





    }
    @PostMapping("/addexpense")
    public void addexpense(@RequestParam Integer companyid,@RequestParam String gelir,@RequestParam String sebep,@RequestParam String gelirtur,@RequestParam String gelirtarihi, @RequestParam Integer id,String name,String surname) throws ParseException {
        System.out.println(id);
        String token = null;
        int integerNumber = Integer.parseInt(gelir); // String'i Integer'a dönüştürme
        Double gelir1= (double) integerNumber;
        System.out.println("burdayim");
        Long longSayi = (long) id; // int'i Long'a dönüştür
        Long companyid1= (long) companyid;
        companyService.expenseEkle(gelirtur,longSayi,gelir1,companyid1,sebep,name,surname,gelirtarihi);

    }


    @GetMapping(FINDBYID + "2/{id}")
    public ResponseEntity<DTOGELIRGIDER> findBydtogelirgider(@PathVariable Long id) {
        System.out.println("ca"+id);
        return ResponseEntity.ok(companyService.dtogelirgider(id));
    }
    @PostMapping("/findalloldrequesbycompanymanager")
    public ResponseEntity<List<Expense>> findalloldrequesbycompanymanager(
            @RequestParam(required = false) String token,
            @RequestHeader(required = false) String authorization,
            @RequestBody(required = false) Map<String, String> requestBody) {

        if (token != null) {
            System.out.println("Sorgu parametresinden gelen token: " + token);
            // Sorgu parametresinden gelen token'ı işleyebilirsiniz
            token = token.substring(7);
        } else if (authorization != null) {
            // İstek başlığından gelen token'ı Bearer prefix'ini ayırarak işleyebilirsiniz
            if (authorization.startsWith("Bearer ")) {
                String tokenWithoutBearer = authorization.substring(7); // 7, "Bearer " prefix uzunluğudur
                System.out.println("İstek başlığından gelen token: " + tokenWithoutBearer);
                // tokenWithoutBearer artık Bearer prefix'inden arındırılmış token'ı içerir
                // tokenWithoutBearer değişkenini kullanarak işlemlerinizi gerçekleştirebilirsiniz
                token = tokenWithoutBearer;
            }
        } else if (requestBody != null && requestBody.containsKey("token")) {
            System.out.println("İstek gövdesinden gelen token: " + requestBody.get("token"));
            String tokenWithoutBearer = requestBody.get("token").substring(7); // 7, "Bearer " prefix uzunluğudur
            token = tokenWithoutBearer;
            System.out.println(token);
        } else {
            System.out.println("Token sağlanmadı");
        }

        // Alınan token ile işlemlerinizi gerçekleştirin
        return ResponseEntity.ok(companyService.findalloldreguestbycompanymanager(token));
    }
    @PostMapping("/findalloldrequesbycompanymanager2")
    public ResponseEntity<List<Expense>> findalloldrequesbycompanymanager2(
            @RequestParam(required = false) String token,
            @RequestHeader(required = false) String authorization,
            @RequestBody(required = false) Map<String, String> requestBody) {

        if (token != null) {
            System.out.println("Sorgu parametresinden gelen token: " + token);
            // Sorgu parametresinden gelen token'ı işleyebilirsiniz
        } else if (authorization != null) {
            // İstek başlığından gelen token'ı Bearer prefix'ini ayırarak işleyebilirsiniz
            if (authorization.startsWith("Bearer ")) {
                String tokenWithoutBearer = authorization.substring(7); // 7, "Bearer " prefix uzunluğudur
                System.out.println("İstek başlığından gelen token: " + tokenWithoutBearer);
                // tokenWithoutBearer artık Bearer prefix'inden arındırılmış token'ı içerir
                // tokenWithoutBearer değişkenini kullanarak işlemlerinizi gerçekleştirebilirsiniz
                token = tokenWithoutBearer;
            }
        } else if (requestBody != null && requestBody.containsKey("token")) {
            System.out.println("İstek gövdesinden gelen token: " + requestBody.get("token"));
            // İstek gövdesinden gelen token'ı işleyebilirsiniz
        } else {
            System.out.println("Token sağlanmadı");
        }

        // Alınan token ile işlemlerinizi gerçekleştirin
        System.out.println("expense burda");
        return ResponseEntity.ok(companyService.findallexpensebycompanymanager(token));
    }


}
