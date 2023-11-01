package com.hrproject.service;

import com.hrproject.dto.request.UserLoginDto;
import com.hrproject.dto.request.UserSaveRequestDto;
import com.hrproject.exception.ErrorType;
import com.hrproject.exception.UserManagerException;
import com.hrproject.mapper.IUserMapper;
import com.hrproject.rabbitmq.model.MailModel;
import com.hrproject.rabbitmq.model.RegisterModel;
import com.hrproject.rabbitmq.producer.MailProducer;
import com.hrproject.repository.IUserRepository;
import com.hrproject.repository.entity.UserProfile;
import com.hrproject.repository.enums.EGender;
import com.hrproject.repository.enums.ERole;
import com.hrproject.repository.enums.EStatus;
import com.hrproject.utility.JwtTokenManager;
import com.hrproject.utility.ServiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService extends ServiceManager<UserProfile, Long> { //extends ServiceManager<UserProfile, String> {

    private final IUserRepository userRepository;

    private final JwtTokenManager jwtTokenManager;

    private final IUserMapper userMapper;
    private final MailProducer mailProducer;


    public UserService(IUserRepository userRepository, JwtTokenManager jwtTokenManager, IUserMapper userMapper, MailProducer mailProducer) {
        super(userRepository);
        this.userRepository = userRepository;
        this.jwtTokenManager = jwtTokenManager;
        this.userMapper = userMapper;
        this.mailProducer = mailProducer;
    }

    public void createNewUserWithRabbitmq(RegisterModel model) {

        UserProfile userProfile = userMapper.toUserProfile(model);

        save(userProfile);

    }

    public UserProfile savedto(UserSaveRequestDto dto) {

        UserProfile userProfile = userMapper.toUserProfile(dto);

        return save(userProfile);

    }

    public String logindto(UserLoginDto dto) {

        if (userRepository.findOptionalByUsernameAndPassword(dto.getUsername(), dto.getPassword()).isEmpty()) {
            throw new UserManagerException(ErrorType.DOLOGIN_USERNAMEORPASSWORD_NOTEXISTS);
        } else {

            UserProfile userProfile = userRepository.findOptionalByUsernameAndPassword(dto.getUsername(), dto.getPassword()).get();
            if (!userProfile.getStatus().equals(EStatus.ACTIVE)) throw new UserManagerException(ErrorType.ACCOUNT_NOT_ACTIVE);
            System.out.println(userProfile.toString());
            return String.valueOf(jwtTokenManager.createToken(userProfile.getId(),userProfile.getRole()).get());
        }

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

    public UserProfile findbytokken(String tokken){
        try {
            // Sleep for 5 seconds (5000 milliseconds)
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // Handle the InterruptedException, which can be thrown if another thread interrupts the sleep.
        }
        UserProfile userProfile=userRepository.findByUsername(jwtTokenManager.getUsername(tokken).get()).get();


        return userProfile;
    }



    public List<UserProfile> getAllEmployees() {

        List<UserProfile> employeeList = userRepository.findByRole(ERole.EMPLOYEE.name());

        return employeeList;
    }

    public List<UserProfile> finduserprofilesbyadmin(String tokken){
        System.out.println("burdasinfindbyadim");
        System.out.println(tokken);

        if (jwtTokenManager.verifyToken(tokken).equals(false)) throw new UserManagerException(ErrorType.INVALID_TOKEN);

        if (!jwtTokenManager.getRoleFromToken(tokken).get().equals(ERole.ADMIN.toString())) throw new UserManagerException(ErrorType.NO_PERMISION);
        else return userRepository.findAll();
    }
    public List<UserProfile> finduserprofilesbyadminpending( String tokken){
        System.out.println("burdasinfindbyadim");
        System.out.println(tokken);

        if (jwtTokenManager.verifyToken(tokken).equals(false)) throw new UserManagerException(ErrorType.INVALID_TOKEN);

        if (!jwtTokenManager.getRoleFromToken(tokken).get().equals(ERole.ADMIN.toString())) throw new UserManagerException(ErrorType.NO_PERMISION);
        else{ ;
            return userRepository.findAll().stream().filter(a->a.getStatus().equals(EStatus.PENDING)).toList();

        }
    }
    public void activitosyon (String token,Long id){
        if (!jwtTokenManager.verifyToken(token)){
            throw new UserManagerException(ErrorType.INVALID_TOKEN);
        }
        System.out.println(jwtTokenManager.getRoleFromToken(token).get());
        if (!jwtTokenManager.getRoleFromToken(token).get().equals(ERole.ADMIN.toString())){
            throw new UserManagerException(ErrorType.NO_PERMISION);
        }
        UserProfile admin=userRepository.findById(jwtTokenManager.getIdFromToken(token).get()).get();
        UserProfile userProfile=userRepository.findById(id).get();
        userProfile.setStatus(EStatus.ACTIVE);
        System.out.println(update(userProfile));
        MailModel mailModel= MailModel.builder().
                text("Uyeliginiz "+admin.getUsername()+ "tarafindan onaylanmıs/n"
                        +"Linke tıklayarak giris sayfasina ulasabilirsiniz  "+"http://localhost:3000/authentication/sign-in").
                email(userProfile.getEmail())
                .subject("Aktivasyon onay maili").build();
        mailProducer.sendMail(mailModel);
        System.out.println(mailModel);







    }
    public UserProfile userProfilefindbidwithtokken(String tokken){
        if (jwtTokenManager.verifyToken(tokken).equals(false)) throw new UserManagerException(ErrorType.INVALID_TOKEN);
        if (jwtTokenManager.getIdFromToken(tokken).isEmpty()) throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        UserProfile userProfile=userRepository.findById(jwtTokenManager.getIdFromToken(tokken).get()).get();


        return userProfile;

    }
    public UserProfile findEmployeeByAuthId(Long authId) {
        Optional<UserProfile> employee = userRepository.findByAuthId(authId);
        if (employee.isPresent())
            return employee.get();
        else
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
    }
}
