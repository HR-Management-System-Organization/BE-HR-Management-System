package com.hrproject.service;

import com.hrproject.dto.request.*;
import com.hrproject.exception.ErrorType;
import com.hrproject.exception.UserManagerException;
import com.hrproject.mapper.IUserMapper;
import com.hrproject.rabbitmq.model.CompanyModel;
import com.hrproject.rabbitmq.model.ExpenseModel;
import com.hrproject.rabbitmq.model.MailModel;
import com.hrproject.rabbitmq.model.RegisterModel;
import com.hrproject.rabbitmq.producer.CompanyProducer;
import com.hrproject.rabbitmq.producer.ExpenseProducer;
import com.hrproject.rabbitmq.producer.MailProducer;
import com.hrproject.repository.IAvansRepository;
import com.hrproject.repository.IUserRepository;
import com.hrproject.repository.IizinRepository;
import com.hrproject.repository.entity.Avanstelebi;
import com.hrproject.repository.entity.Izintelebi;
import com.hrproject.repository.entity.UserProfile;
import com.hrproject.repository.enums.EGender;
import com.hrproject.repository.enums.EIzinTur;
import com.hrproject.repository.enums.ERole;
import com.hrproject.repository.enums.EStatus;
import com.hrproject.utility.JwtTokenManager;
import com.hrproject.utility.PasswordGenerator;
import com.hrproject.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class UserService extends ServiceManager<UserProfile, Long> { //extends ServiceManager<UserProfile, String> {

    private final IUserRepository userRepository;

    private final PasswordGenerator passwordGenerator;

    private final JwtTokenManager jwtTokenManager;

    private final IAvansRepository avansRepository;

    private final IUserMapper userMapper;

    private final MailProducer mailProducer;

    private final CompanyProducer companyProducer;

    private final ExpenseProducer expenseProducer;

    private final IizinRepository iizinRepository;

    public UserService(IUserRepository userRepository, PasswordGenerator passwordGenerator, JwtTokenManager jwtTokenManager, IAvansRepository avansRepository, IUserMapper userMapper, MailProducer mailProducer, CompanyProducer companyProducer, ExpenseProducer expenseProducer, IizinRepository iizinRepository) {
        super(userRepository);
        this.userRepository = userRepository;
        this.passwordGenerator = passwordGenerator;
        this.jwtTokenManager = jwtTokenManager;
        this.avansRepository = avansRepository;
        this.userMapper = userMapper;
        this.mailProducer = mailProducer;
        this.companyProducer = companyProducer;
        this.expenseProducer = expenseProducer;
        this.iizinRepository = iizinRepository;
    }

    public void createNewUserWithRabbitmq(RegisterModel model) {

        UserProfile userProfile = userMapper.toUserProfile(model);
        System.out.println((model.getActivationDate()));
        userProfile.setActivationDate(model.getActivationDate());

        save(userProfile);

    }

    public UserProfile savedto(UserSaveRequestDto dto) {

        UserProfile userProfile = userMapper.toUserProfile(dto);

        return save(userProfile);

    }

    public String logindto(UserLoginDto dto) {
        System.out.println(dto.toString());
        if (userRepository.findOptionalByUsernameAndPassword(dto.getUsername(), dto.getPassword()).isEmpty()) {
            if (userRepository.findOptionalByEmailAndPassword(dto.getUsername(), dto.getPassword()).isPresent()){
                UserProfile userProfile = userRepository.findOptionalByEmailAndPassword(dto.getUsername(), dto.getPassword()).get();
                if (!userProfile.getStatus().equals(EStatus.ACTIVE))
                    throw new UserManagerException(ErrorType.ACCOUNT_NOT_ACTIVE);
                System.out.println(userProfile.toString());
                return String.valueOf(jwtTokenManager.createToken(userProfile.getId(), userProfile.getRole(),userProfile.getStatus()).get());
            }

            throw new UserManagerException(ErrorType.DOLOGIN_USERNAMEORPASSWORD_NOTEXISTS);
        } else {

            UserProfile userProfile = userRepository.findOptionalByUsernameAndPassword(dto.getUsername(), dto.getPassword()).get();
            LocalDate activationDate = userProfile.getActivationDate();

            if (activationDate != null && activationDate.isBefore(LocalDate.now())) {
                inactiveyap(userProfile.getId());
            };
            if (!userProfile.getStatus().equals(EStatus.ACTIVE)&&!userProfile.getStatus().equals(EStatus.INACTIVE))
                throw new UserManagerException(ErrorType.ACCOUNT_NOT_ACTIVE);
            System.out.println(userProfile.toString());
            return String.valueOf(jwtTokenManager.createToken(userProfile.getId(), userProfile.getRole(),userProfile.getStatus()).get());
        }

    }

    public void inactiveyap(Long id){
        List<UserProfile> companyUsers = userRepository.findAll()
                .stream()
                .filter(a -> {
                    Long companyId = a.getCompanyId();
                    Long targetCompanyId = userRepository.findById(id).orElse(new UserProfile()).getCompanyId();
                    return companyId != null && companyId.equals(targetCompanyId);
                })
                .toList();
        companyUsers.stream().forEach(a->a.setStatus(EStatus.INACTIVE));
        userRepository.saveAll(companyUsers);

    }

    public void uyeliksatinal(Long id,int day){
        UserProfile userProfile=userRepository.findById(id).get();
        LocalDate activationDate = userProfile.getActivationDate();
        if (activationDate == null || activationDate.isBefore(LocalDate.now())){
            userProfile.setActivationDate(LocalDate.now().plusDays(day));
            userProfile.setStatus(EStatus.ACTIVE);
        }else userProfile.setActivationDate(userProfile.getActivationDate().plusDays(day));
        List<UserProfile> companyusers = userRepository.findAll()
                .stream()
                .filter(a -> {
                    Long companyId = a.getCompanyId();
                    Long targetCompanyId = userRepository.findById(id).orElse(new UserProfile()).getCompanyId();
                    return companyId != null && companyId.equals(targetCompanyId);
                })
                .toList();
        companyusers.stream().forEach(a-> a.setActivationDate(userProfile.getActivationDate()));
        companyusers.stream().forEach(a-> a.setStatus(EStatus.ACTIVE));
        userRepository.saveAll(companyusers);

    }

    public List<UserProfile> findalluser(UserProfile userProfile) {

        if (userProfile.getRole().equals(ERole.COMPANY_MANAGER) || userProfile.getRole().equals(ERole.ADMIN)) {
            return userRepository.findAll();
        } else throw new UserManagerException(ErrorType.NO_PERMISION);
    }

    public String delete(Long id) {

        try {
            delete(id);
            return "Başarılı";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void activation(String username) {

        if (userRepository.findByUsername(username).isEmpty()) {
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        } else {
            UserProfile userProfile = userRepository.findByUsername(username).get();
            userProfile.setStatus(EStatus.ACTIVE);
            update(userProfile);
        }
    }

    public String requestParentalLeave(UserLoginDto dto, int days) {

        if (userRepository.findOptionalByUsernameAndPassword(dto.getUsername(), dto.getPassword()).isPresent()) {
            UserProfile userProfile = userRepository.findOptionalByUsernameAndPassword(dto.getUsername(), dto.getPassword()).get();

            if (userProfile.getGender() == EGender.MALE) {
                if (days <= 10) {
                    userProfile.setParentalLeave(userProfile.getParentalLeave() + days);
                    System.out.println(userProfile.getName() + " " + days + " günlük babalık izni aldı.");
                    update(userProfile);
                } else {
                    System.out.println("Erkek çalışanlar en fazla 10 günlük babalık izni alabilir.");
                }
            } else if (userProfile.getGender() == EGender.FEMALE) {
                if (days <= 180) {
                    userProfile.setParentalLeave(userProfile.getParentalLeave() + days);
                    System.out.println(userProfile.getName() + " " + days + " günlük annelik izni aldı.");
                    update(userProfile);
                } else {
                    System.out.println("Kadın çalışanlar en fazla 6 aylık annelik izni alabilir.");
                }
            } else {
                System.out.println("Cinsiyet bilgisi hatalı.");
            }
        } else {
            throw new UserManagerException(ErrorType.DOLOGIN_USERNAMEORPASSWORD_NOTEXISTS);
        }

        return "İzin alma başarılı.";
    }

    public String requestAnnualLeave(UserLoginDto dto, int days) {

        if (userRepository.findOptionalByUsernameAndPassword(dto.getUsername(), dto.getPassword()).isPresent()) {
            UserProfile userProfile = userRepository.findOptionalByUsernameAndPassword(dto.getUsername(), dto.getPassword()).get();

            if (days <= userProfile.getTotalAnnualLeave()) {
                userProfile.setTotalAnnualLeave(userProfile.getTotalAnnualLeave() - days);
                System.out.println(userProfile.getName() + " " + days + " günlük yıllık izin aldı.");
                update(userProfile);
            } else {
                System.out.println("Yıllık izin hakkınız yetersiz.");
            }
        } else {
            throw new UserManagerException(ErrorType.DOLOGIN_USERNAMEORPASSWORD_NOTEXISTS);
        }

        return "İzin alma başarılı.";
    }

    public int getTotalAnnualLeave(UserLoginDto dto) {

        if (userRepository.findOptionalByUsernameAndPassword(dto.getUsername(), dto.getPassword()).isPresent()) {
            UserProfile userProfile = userRepository.findOptionalByUsernameAndPassword(dto.getUsername(), dto.getPassword()).get();
            return userProfile.getTotalAnnualLeave();
        } else {
            throw new UserManagerException(ErrorType.DOLOGIN_USERNAMEORPASSWORD_NOTEXISTS);
        }
    }

    public UserProfile findbytokken(String tokken) {
        try {
            // Sleep for 5 seconds (5000 milliseconds)
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // Handle the InterruptedException, which can be thrown if another thread interrupts the sleep.
        }
        UserProfile userProfile = userRepository.findByUsername(jwtTokenManager.getUsername(tokken).get()).get();

        return userProfile;
    }

    public List<UserProfile> finduserprofilesbyadmin(String tokken) {

        System.out.println("burdasinfindbyadim");
        System.out.println(tokken);

        if (jwtTokenManager.verifyToken(tokken).equals(false)) throw new UserManagerException(ErrorType.INVALID_TOKEN);

        if (!jwtTokenManager.getRoleFromToken(tokken).get().equals(ERole.ADMIN.toString()))
            throw new UserManagerException(ErrorType.NO_PERMISION);
        else return userRepository.findAll();
    }

    public List<UserProfile> finduserprofilesbyadminpending(String tokken) {

        System.out.println("burdasinfindbyadim");
        System.out.println(tokken);

        if (jwtTokenManager.verifyToken(tokken).equals(false)) throw new UserManagerException(ErrorType.INVALID_TOKEN);

        if (!jwtTokenManager.getRoleFromToken(tokken).get().equals(ERole.ADMIN.toString()))
            throw new UserManagerException(ErrorType.NO_PERMISION);
        else {
            return userRepository.findAll().stream().filter(a -> a.getRole().equals(ERole.COMPANY_MANAGER)).filter(a -> a.getStatus().equals(EStatus.PENDING)).toList();
        }
    }

    public List<UserProfile> findallguestbycompanymanager(String tokken) {

        System.out.println("burdasinfindbyadim");
        System.out.println(tokken);
        System.out.println("1."+jwtTokenManager.getRoleFromToken(tokken).get());

        System.out.println(jwtTokenManager.getRoleFromToken(tokken).get());
        if (!jwtTokenManager.getRoleFromToken(tokken).get().equals(ERole.COMPANY_MANAGER.toString()))
            throw new UserManagerException(ErrorType.NO_PERMISION);
        else {
            System.out.println(jwtTokenManager.getIdFromToken(tokken));
            UserProfile userProfile=findById(jwtTokenManager.getIdFromToken(tokken).get()).get();
            System.out.println(userProfile.getCompanyId());
            Long id=userProfile.getCompanyId();
            System.out.println(id);;
            return userRepository.findAll().stream().filter(a -> a.getCompanyId() != null && a.getCompanyId().equals(id)).filter(b->b.getStatus().equals(EStatus.ACTIVE)).toList();

        }
    }

    public void activitosyon(String token, Long id) {

        if (!jwtTokenManager.verifyToken(token)) {
            throw new UserManagerException(ErrorType.INVALID_TOKEN);
        }
        System.out.println(jwtTokenManager.getRoleFromToken(token).get());
        if (!jwtTokenManager.getRoleFromToken(token).get().equals(ERole.ADMIN.toString())) {
            throw new UserManagerException(ErrorType.NO_PERMISION);
        }
        UserProfile admin = userRepository.findById(jwtTokenManager.getIdFromToken(token).get()).get();
        UserProfile userProfile = userRepository.findById(id).get();
        userProfile.setStatus(EStatus.ACTIVE);
        System.out.println(update(userProfile));
        MailModel mailModel = MailModel.builder().
                text("Uyeliginiz " + admin.getUsername() + "tarafindan onaylanmıs/n"
                        + "Linke tıklayarak giris sayfasina ulasabilirsiniz  " + "http://localhost:3000/authentication/sign-in").
                email(userProfile.getEmail())
                .subject("Aktivasyon onay maili").build();
        mailProducer.sendMail(mailModel);
        System.out.println(mailModel);

    }
    public void deletebyadmin(String token, Long id) {

        if (!jwtTokenManager.verifyToken(token)) {
            throw new UserManagerException(ErrorType.INVALID_TOKEN);
        }
        System.out.println(jwtTokenManager.getRoleFromToken(token).get());
        if (!jwtTokenManager.getRoleFromToken(token).get().equals(ERole.ADMIN.toString())) {
            throw new UserManagerException(ErrorType.NO_PERMISION);
        }
        UserProfile admin = userRepository.findById(jwtTokenManager.getIdFromToken(token).get()).get();
        UserProfile userProfile = userRepository.findById(id).get();
        userProfile.setStatus(EStatus.DELETED);
        System.out.println(update(userProfile));
        MailModel mailModel = MailModel.builder().
                text("Uyeliginiz " + admin.getUsername() + "tarafindan onaylanmamıstır"
                ).
                email(userProfile.getEmail())
                .subject("Delete your account").build();
        mailProducer.sendMail(mailModel);
        System.out.println(mailModel);
    }


    public void activedate(String token, int sec) {

        if (!jwtTokenManager.verifyToken(token)) {
            throw new UserManagerException(ErrorType.INVALID_TOKEN);
        }


        UserProfile admin = userRepository.findById(jwtTokenManager.getIdFromToken(token).get()).get();
        if (sec==1)uyeliksatinal(admin.getId(),30);
        if (sec==2)uyeliksatinal(admin.getId(),60);
        if (sec==3)uyeliksatinal(admin.getId(),90);

    }

    public UserProfile userProfilefindbidwithtokken(String tokken) {

        if (jwtTokenManager.verifyToken(tokken).equals(false)) throw new UserManagerException(ErrorType.INVALID_TOKEN);
        if (jwtTokenManager.getIdFromToken(tokken).isEmpty()) throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        UserProfile userProfile = userRepository.findById(jwtTokenManager.getIdFromToken(tokken).get()).get();
        return userProfile;
    }

    public UserProfile findEmployeeByAuthId(Long authId) {

        Optional<UserProfile> employee = userRepository.findById(authId);
        if (employee.isPresent())
            return employee.get();
        else
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
    }

    public List<UserProfile> getAllEmployees(Long companyId) {

        List<UserProfile> employeeList = userRepository.findByRole(ERole.EMPLOYEE);
        return employeeList.stream().filter(emp -> emp.getCompanyId() == companyId).toList();
//        Optional<User> user = findEmployeeByAuthId(userId);
//        if (optionalUser.isPresent()) {
//            String companyId = user.get().getCompanyId();
//            List<User> employeeList = userRepository.findByCompanyId(companyId);
//            return employeeList.size();
    }

    public UserProfile updateprofile(Long id, UserProfileUpdateRequestDto dto){

        UserProfile userProfile=findById(id).get();
        userProfile.setName(dto.getName());
        userProfile.setEmail(dto.getEmail());
        userProfile.setSurName(dto.getSurName());
        update(userProfile);
        return userProfile;
    }

    public ArrayList<Long>resmitattiler() throws ParseException {

        ArrayList<Long> resmitattiller2=new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        String[] tarihler = {"01.01.2024", "23.04.2024", "19.05.2024", "30.08.2024", "29.10.2024"};

        for (String tarihStr : tarihler) {
            Date tarih = dateFormat.parse(tarihStr);
            long zamanDamgasi = tarih.getTime();
            System.out.println("Tarih: " + tarihStr + " -> Unix Zaman Damgası: " + zamanDamgasi);
            resmitattiller2.add(zamanDamgasi);
        }
        return resmitattiller2;
    }

    public boolean izintelebi(String tokken, String neden, String tarihler,String izitur) throws ParseException {

        System.out.println("x--izintur->"+izitur);

        UserProfile userProfile1=findById(jwtTokenManager.getIdFromToken(tokken).get()).get();
        String dateStr = tarihler;

        // İlk tarih (ilk 10 karakteri alın)
        String firstDateStr = dateStr.substring(0, 10);

        // İkinci tarih (sonraki 10 karakteri alın)
        String secondDateStr = dateStr.substring(10);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date firstDate = dateFormat.parse(firstDateStr);
        Date secondDate = dateFormat.parse(secondDateStr);long firstDateMillis = firstDate.getTime();

        long secondDateMillis = secondDate.getTime();
        if (firstDateMillis>secondDateMillis) throw new UserManagerException(ErrorType.DATES_NOT_CORRECT);
        if (firstDateMillis==secondDateMillis) throw new UserManagerException(ErrorType.DATES_NOT_CORRECT2);
        ArrayList<Long> dates=new ArrayList<>();
        Date dayOfWeekDate = new Date(firstDateMillis);

        SimpleDateFormat dayFormat = new SimpleDateFormat("EEE", Locale.ENGLISH);
        String dayOfWeek = dayFormat.format(firstDate);
        ArrayList<Long> resmitattiler=new ArrayList<>();
        resmitattiler=resmitattiler();
        System.out.println("Day: " + dayOfWeek);
        if (dayOfWeek.equals("Sat")||dayOfWeek.equals("Sun")){
            System.out.println("haftasonu");
        }

        for (long i = firstDateMillis; i <secondDateMillis ; i=i+86400000l) {
            dayOfWeekDate=new Date(i);
            dayOfWeek = dayFormat.format(dayOfWeekDate);
            System.out.println(i);
            Long x=i;


            if (!dayOfWeek.equals("Sat") && !dayOfWeek.equals("Sun") && !resmitattiler.stream().anyMatch(a -> a.equals(x))) {
                dates.add(i);
            }
        }

        UserProfile userProfile=findAll().stream().filter(a->a.getRole().equals(ERole.COMPANY_MANAGER))
                .filter(a -> a.getCompanyId() != null && a.getCompanyId().equals(userProfile1.getCompanyId())).findFirst().get();

        System.out.println(userProfile);EIzinTur izinTur1=EIzinTur.Yillikizin;
        if (izitur.equals("babalik")) {
            if (userProfile1.getGender().equals(EGender.FEMALE)) throw new UserManagerException(ErrorType.WRONG_GENDER_MAN);
            izinTur1=EIzinTur.Babalikizini;
        }else if (izitur.equals("annelik")) {
            if (userProfile1.getGender().equals(EGender.MALE)) throw new UserManagerException(ErrorType.WRONG_GENDER_WOMAN);
            izinTur1=EIzinTur.Annelikizini;
        }

        Izintelebi izintelebi= Izintelebi.builder().status(EStatus.PENDING).username(userProfile1.getUsername()).izinTur(izinTur1)
                .managerid(userProfile.getCompanyId()).izinsuresi(dates.size()).izinhakki(userProfile1.getTotalAnnualLeave()).
                nedeni(neden).userid(userProfile.getId()).izinbaslangic(firstDateMillis).izinbitis(secondDateMillis).build();

        iizinRepository.save(izintelebi);
        return true;
    }
    public List<Izintelebi> findallreguestbycompanymanager(String tokken) {

        System.out.println("burdasinfindbyadim");
        System.out.println(tokken);
        System.out.println("1."+jwtTokenManager.getRoleFromToken(tokken).get());

        System.out.println(jwtTokenManager.getRoleFromToken(tokken).get());
        if (!jwtTokenManager.getRoleFromToken(tokken).get().equals(ERole.COMPANY_MANAGER.toString()))
            throw new UserManagerException(ErrorType.NO_PERMISION);
        else {
            System.out.println(jwtTokenManager.getIdFromToken(tokken));
            UserProfile userProfile=findById(jwtTokenManager.getIdFromToken(tokken).get()).get();
            System.out.println(userProfile.getCompanyId());
            Long id=userProfile.getCompanyId();
            System.out.println(id);;
            return iizinRepository.findAll().stream().
                    filter(a->a.getManagerid().equals(userProfile.getCompanyId())).filter(a->a.getStatus().equals(EStatus.PENDING)).toList();
        }
    }
    public void deleterequestbyadmin(String token, Long id) {

        if (!jwtTokenManager.verifyToken(token)) {
            throw new UserManagerException(ErrorType.INVALID_TOKEN);
        }
        System.out.println(jwtTokenManager.getRoleFromToken(token).get());
        if (!jwtTokenManager.getRoleFromToken(token).get().equals(ERole.COMPANY_MANAGER.toString())) {
            throw new UserManagerException(ErrorType.NO_PERMISION);
        }

        UserProfile admin = userRepository.findById(jwtTokenManager.getIdFromToken(token).get()).get();
        Izintelebi izintelebi=iizinRepository.findById(id).get();
        UserProfile userProfile=findById(izintelebi.getUserid()).get();
        izintelebi.setStatus(EStatus.DELETED);
        System.out.println(iizinRepository.save(izintelebi));
        MailModel mailModel = MailModel.builder().
                text("Uyeliginiz " + admin.getUsername() + "tarafindan talebiniz onaylanmamıstır"
                ).
                email(userProfile.getEmail())
                .subject("Delete your request").build();
        mailProducer.sendMail(mailModel);
        System.out.println(mailModel);
    }

    public void activerequestbyadmin(String token, Long id) {

        if (!jwtTokenManager.verifyToken(token)) {
            throw new UserManagerException(ErrorType.INVALID_TOKEN);
        }
        System.out.println(jwtTokenManager.getRoleFromToken(token).get());
        if (!jwtTokenManager.getRoleFromToken(token).get().equals(ERole.COMPANY_MANAGER.toString())) {
            throw new UserManagerException(ErrorType.NO_PERMISION);
        }

        UserProfile admin = userRepository.findById(jwtTokenManager.getIdFromToken(token).get()).get();
        Izintelebi izintelebi=iizinRepository.findById(id).get();
        UserProfile userProfile=findById(izintelebi.getUserid()).get();
        izintelebi.setStatus(EStatus.ACTIVE);
        System.out.println(iizinRepository.save(izintelebi));
        MailModel mailModel = MailModel.builder().
                text("Uyeliginiz " + admin.getUsername() + "tarafindan talebiniz onaylanmıstır"
                ).
                email(userProfile.getEmail())
                .subject("Apprpve your request").build();
        mailProducer.sendMail(mailModel);
        System.out.println(mailModel);
        System.out.println(izintelebi.getIzinTur().toString()+"frrwr");
        if (izintelebi.getIzinTur().toString().equals(EIzinTur.Yillikizin.toString()))
            System.out.println("burdayiz");
            userProfile.setTotalAnnualLeave(userProfile.getTotalAnnualLeave()- izintelebi.getIzinsuresi());
        System.out.println(userProfile.getTotalAnnualLeave());
        if (izintelebi.getIzinTur().toString().equals(EIzinTur.Annelikizini.toString()))
            userProfile.setParentalLeave(userProfile.getParentalLeave()- izintelebi.getIzinsuresi());
        if (izintelebi.getIzinTur().toString().equals(EIzinTur.Babalikizini.toString()))
            userProfile.setParentalLeave(userProfile.getParentalLeave()- izintelebi.getIzinsuresi());

        save(userProfile);
    }

    public List<Izintelebi> findalloldreguestbycompanymanager(String tokken) {

        System.out.println("burdasinfindbyadim");
        System.out.println(tokken);
        System.out.println("1."+jwtTokenManager.getRoleFromToken(tokken).get());

        System.out.println(jwtTokenManager.getRoleFromToken(tokken).get());
        if (!jwtTokenManager.getRoleFromToken(tokken).get().equals(ERole.COMPANY_MANAGER.toString()))
            throw new UserManagerException(ErrorType.NO_PERMISION);
        else {
            System.out.println(jwtTokenManager.getIdFromToken(tokken));
            UserProfile userProfile=findById(jwtTokenManager.getIdFromToken(tokken).get()).get();
            System.out.println(userProfile.getCompanyId());
            Long id=userProfile.getCompanyId();
            System.out.println(id);;
            return iizinRepository.findAll().stream()
                    .filter(a->a.getManagerid().
                            equals(userProfile.getCompanyId())).filter(a->!a.getStatus().equals(EStatus.PENDING)).toList();

        }
    }

    public void addEmployee(Long id, AddEmployeeDto addEmployeeCompanyDto) {

        Optional<UserProfile> userProfile= userRepository.findById(id);

        //  UserProfile getCompanyId = new UserProfile();

        // getCompanyId.setCompanyId(addEmployeeCompanyDto.getCompanyId());


        String companyEmail = addEmployeeCompanyDto.getName() + addEmployeeCompanyDto.getSurName() + "@";

        companyProducer.sendCompany(CompanyModel.builder().companyId(userProfile.get().getCompanyId()).mail(companyEmail).build());

        String[] mailArray = companyEmail.toLowerCase().split(" ");

        companyEmail = "";

        for (String part : mailArray) {

            companyEmail = companyEmail + part;
        }

        UserProfile userModel = new UserProfile();
        userModel.setName(addEmployeeCompanyDto.getName());
        userModel.setSurName(addEmployeeCompanyDto.getSurName());
        userModel.setUsername(addEmployeeCompanyDto.getUserName());
        userModel.setBirthDate(addEmployeeCompanyDto.getBirthDate());
        userModel.setCompanyEmail(companyEmail);
        userModel.setRole(ERole.EMPLOYEE);
        userModel.setPhone(addEmployeeCompanyDto.getPhone());
        userModel.setPassword(addEmployeeCompanyDto.getPassword());
        userModel.setCompanyId(userProfile.get().getCompanyId());
        if (addEmployeeCompanyDto.getGender().equals("MALE")) {
            userModel.setGender(EGender.MALE);
        }
        if (addEmployeeCompanyDto.getGender().equals("FEMALE")) {
            userModel.setGender(EGender.FEMALE);
        }

        save(userModel);
    }

    public Boolean addEmployee(String tokken,AddEmployeeCompanyDto addEmployeeCompanyDto) throws ParseException {

        if (!jwtTokenManager.getRoleFromToken(tokken).get().equals(ERole.COMPANY_MANAGER.toString()))
            throw new UserManagerException(ErrorType.NO_PERMISION);
        else {
            System.out.println(jwtTokenManager.getIdFromToken(tokken));
            UserProfile userProfile=findById(jwtTokenManager.getIdFromToken(tokken).get()).get();

            System.out.println("company id" + userProfile.getCompanyId());
            System.out.println("gelenbilgiler ->>> "+addEmployeeCompanyDto);
            System.out.println("company name "+addEmployeeCompanyDto.getCompanyname());
            if (addEmployeeCompanyDto.getGender().isEmpty()) throw new UserManagerException(ErrorType.GENDER);
            String companyEmail = addEmployeeCompanyDto.getName() + addEmployeeCompanyDto.getSurName() + "@"+addEmployeeCompanyDto.getCompanyname().replaceAll("\\s", "")+".com.tr";

            companyProducer.sendCompany(CompanyModel.builder().companyId(userProfile.getCompanyId()).mail(companyEmail).build());

            /*String[] mailArray = companyEmail.toLowerCase().split(" ");

            companyEmail = "";

            for (String part : mailArray) {

                companyEmail = companyEmail + part;

            }*/

            UserProfile userModel = userMapper.toUserProfile(addEmployeeCompanyDto);
            System.out.println(userModel.toString());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            Date firstDate = dateFormat.parse(addEmployeeCompanyDto.getBirthday());
            LocalDate localDate = firstDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

//        userModel.setName(addEmployeeCompanyDto.getName());
//        userModel.setSurName(addEmployeeCompanyDto.getSurname());
//        userModel.setUsername(addEmployeeCompanyDto.getUsername());

            userModel.setBirthDate(localDate);
            userModel.setCompanyEmail(companyEmail.toLowerCase());
            userModel.setRole(ERole.EMPLOYEE);
            userModel.setPhone(addEmployeeCompanyDto.getPhone());
            userModel.setEmail(addEmployeeCompanyDto.getAddress());
            userModel.setCompanyId(userProfile.getCompanyId());
            userModel.setPassword(passwordGenerator.generatePassword(12));
            userModel.setStatus(EStatus.ACTIVE);

            String text="Merhaba "+userModel.getName()+
                    " Kayıt Başarılı Olmuştur. Şifreniz: "+userModel.getPassword()+"  Şirket Mailiniz: "+userModel.getCompanyEmail();

            save(userModel);
            MailModel mailModel= MailModel.builder().email(userModel.getEmail()).text(text).subject("yeni kayit").build();
            mailProducer.sendMail(mailModel);

        }return null;
    }

    public UserProfile updateprofileManager( UserProfileUpdateDto dto){
        UserProfile userProfile=findById(dto.getId()).get();
        userProfile.setName(dto.getName());
        userProfile.setEmail(dto.getEmail());
        userProfile.setSurName(dto.getSurName());
        userProfile.setPhone(dto.getPhone());
        update(userProfile);
        return userProfile;
    }

    public void deleteprofilebycompanymanager(String token, Long id) {

        if (!jwtTokenManager.verifyToken(token)) {
            throw new UserManagerException(ErrorType.INVALID_TOKEN);
        }
        System.out.println(jwtTokenManager.getRoleFromToken(token).get());
        if (!jwtTokenManager.getRoleFromToken(token).get().equals(ERole.COMPANY_MANAGER.toString())) {
            throw new UserManagerException(ErrorType.NO_PERMISION);
        }
        UserProfile userProfile=findById(id).get();
        UserProfile admin = userRepository.findById(jwtTokenManager.getIdFromToken(token).get()).get();
        userProfile.setStatus(EStatus.DELETED);
        System.out.println(userRepository.save(userProfile));
        MailModel mailModel = MailModel.builder().
                text("Uyeliginiz " + admin.getUsername() + "tarafindan silinmisitr"
                ).
                email(userProfile.getEmail())
                .subject("Delete your account").build();
        mailProducer.sendMail(mailModel);
        System.out.println(mailModel);
    }

    public UserProfile maasekle(Long sayi,int maas,String name,String surname,Long company ){
        Double maas1= (double) maas;
        Double tax=null;
        if (maas<=27000)tax=0.15;
        if (maas<=65000&&maas>27000)tax=0.20;
        if (maas<=95000&&maas>65000)tax=0.25;
        else tax=0.35;
        tax=tax*maas1;
        Double brut=tax+maas1;
        LocalDate localDate=LocalDate.now();
        UserProfile userProfile=findById(sayi).get();
        userProfile.setSalary(maas1);
        ExpenseModel model=ExpenseModel.builder().name(name).sayi(sayi).maas(maas).surname(surname).company(company).build();
        expenseProducer.sendCompany(model);
        return update(userProfile);
    }

    public boolean avanstelebi(String tokken, String neden, Integer miktar) throws ParseException {

        UserProfile userProfile1=findById(jwtTokenManager.getIdFromToken(tokken).get()).get();

        UserProfile userProfile=findAll().stream().filter(a->a.getRole().equals(ERole.COMPANY_MANAGER))
                .filter(a -> a.getCompanyId() != null && a.getCompanyId().equals(userProfile1.getCompanyId())).findFirst().get();

        System.out.println(userProfile1.getSalary());
        //Double result = Double.parseDouble(miktar);
        Double result= Double.valueOf(miktar);
        if (result>userProfile1.getSalary()) throw new UserManagerException(ErrorType.Salary_is_insufficient);

        Avanstelebi avanstelebi= Avanstelebi.builder().status(EStatus.PENDING).username(userProfile1.getUsername()).avanstalebi(result)
                .managerid(userProfile.getCompanyId()).avanstarihi(LocalDate.now()).maas(userProfile1.getSalary()).
                nedeni(neden).userid(userProfile.getId()).build();



       avansRepository.save(avanstelebi);
        return true;
    }

    public List<Avanstelebi> findallavansreguestbycompanymanager(String tokken) {

        System.out.println("burdasinfindbyadim");
        System.out.println(tokken);
        System.out.println("1."+jwtTokenManager.getRoleFromToken(tokken).get());

        System.out.println(jwtTokenManager.getRoleFromToken(tokken).get());
        if (!jwtTokenManager.getRoleFromToken(tokken).get().equals(ERole.COMPANY_MANAGER.toString()))
            throw new UserManagerException(ErrorType.NO_PERMISION);
        else {
            System.out.println(jwtTokenManager.getIdFromToken(tokken));
            UserProfile userProfile=findById(jwtTokenManager.getIdFromToken(tokken).get()).get();
            System.out.println(userProfile.getCompanyId());
            Long id=userProfile.getCompanyId();
            System.out.println(id);;
            return avansRepository.findAll().stream().
                    filter(a->a.getManagerid().equals(userProfile.getId())).filter(a->a.getStatus().equals(EStatus.PENDING)).toList();

        }
    }

    public void deleteavansrequestbyadmin(String token, Long id) {

        if (!jwtTokenManager.verifyToken(token)) {
            throw new UserManagerException(ErrorType.INVALID_TOKEN);
        }
        System.out.println(jwtTokenManager.getRoleFromToken(token).get());
        if (!jwtTokenManager.getRoleFromToken(token).get().equals(ERole.COMPANY_MANAGER.toString())) {
            throw new UserManagerException(ErrorType.NO_PERMISION);
        }

        UserProfile admin = userRepository.findById(jwtTokenManager.getIdFromToken(token).get()).get();
        Avanstelebi avanstelebi=avansRepository.findById(id).get();
        UserProfile userProfile=findById(avanstelebi.getUserid()).get();
        avanstelebi.setStatus(EStatus.DELETED);
        System.out.println(avansRepository.save(avanstelebi));
        MailModel mailModel = MailModel.builder().
                text("Uyeliginiz " + admin.getUsername() + "tarafindan talebiniz onaylanmamıstır"
                ).
                email(userProfile.getEmail())
                .subject("Delete your request").build();
        mailProducer.sendMail(mailModel);
        System.out.println(mailModel);
    }

    public void activeavansrequestbyadmin(String token, Long id) {

        if (!jwtTokenManager.verifyToken(token)) {
            throw new UserManagerException(ErrorType.INVALID_TOKEN);
        }
        System.out.println(jwtTokenManager.getRoleFromToken(token).get());
        if (!jwtTokenManager.getRoleFromToken(token).get().equals(ERole.COMPANY_MANAGER.toString())) {
            throw new UserManagerException(ErrorType.NO_PERMISION);
        }

        UserProfile admin = userRepository.findById(jwtTokenManager.getIdFromToken(token).get()).get();
        Avanstelebi avanstelebi=avansRepository.findById(id).get();
        UserProfile userProfile=findById(avanstelebi.getUserid()).get();
        avanstelebi.setStatus(EStatus.ACTIVE);
        System.out.println(avansRepository.save(avanstelebi));
        MailModel mailModel = MailModel.builder().
                text("Uyeliginiz " + admin.getUsername() + "tarafindan talebiniz onaylanmıstır"
                ).
                email(userProfile.getEmail())
                .subject("Apprpve your request").build();
        mailProducer.sendMail(mailModel);
        System.out.println(mailModel);
        ExpenseModel expenseModel=ExpenseModel.builder().name(userProfile.getName()).sayi(userProfile.getId())
                .surname(userProfile.getSurName()).company(userProfile.getCompanyId()).expense(avanstelebi.getAvanstalebi()).about(avanstelebi.getNedeni()).build();
        expenseProducer.sendCompany(expenseModel);
        save(userProfile);
    }
}