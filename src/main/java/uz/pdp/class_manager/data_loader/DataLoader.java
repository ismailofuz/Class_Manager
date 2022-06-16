package uz.pdp.class_manager.data_loader;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.pdp.class_manager.entity.User;
import uz.pdp.class_manager.entity.enums.RoleEnum;
import uz.pdp.class_manager.repository.UserRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddl;

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(ddl);
        if (ddl.equals("create")){

            // new User adding default 5 users



            User user1 = new User("O'tkirjon", "Jo'raqulov","Mr.Mike", encoder.encode("07062003"),"email@gmail.com", RoleEnum.TEACHER);
            User user2 = new User("O'tkirjon", "Jo'raqulov","Olimjon",encoder.encode("07062003"),"email1@gmail.com", RoleEnum.STUDENT);
            User user3 = new User("O'tkirjon", "Jo'raqulov","Doniyor",encoder.encode("07062003"),"email2@gmail.com", RoleEnum.STUDENT);
            User user4 = new User("O'tkirjon", "Jo'raqulov","Utkir",encoder.encode("07062003"),"email3@gmail.com", RoleEnum.STUDENT);
            User user5 = new User("O'tkirjon", "Jo'raqulov","Jabbor",encoder.encode("07062003"),"email4@gmail.com", RoleEnum.STUDENT);
            User user6 = new User("Sirojiddin", "Ismoilov","Ismailofuz",encoder.encode("09052002"),"sirojiddin@gmail.com", RoleEnum.ADMIN);

            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);
            userRepository.save(user4);
            userRepository.save(user5);
            userRepository.save(user6);
        }
    }
}
