package uz.pdp.class_manager.data_loader;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.pdp.class_manager.entity.User;
import uz.pdp.class_manager.entity.enums.RoleEnum;
import uz.pdp.class_manager.repository.UserRepository;


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



            User user1 = new User("Mr.Mike", encoder.encode("07062003"),"email@gmail.com", RoleEnum.TEACHER,"Maths");
            User user2 = new User("Olimjon",encoder.encode("07062003"),"email1@gmail.com", RoleEnum.STUDENT,"Maths");
            User user3 = new User("Doniyor",encoder.encode("07062003"),"email2@gmail.com", RoleEnum.STUDENT,"Maths");
            User user4 = new User("Utkir",encoder.encode("07062003"),"email3@gmail.com", RoleEnum.STUDENT,"Maths");
            User user5 = new User("Jabbor",encoder.encode("07062003"),"email4@gmail.com", RoleEnum.STUDENT,"Maths");

            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);
            userRepository.save(user4);
            userRepository.save(user5);


        }
    }
}
