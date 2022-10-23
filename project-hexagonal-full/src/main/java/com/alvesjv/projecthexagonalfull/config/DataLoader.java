package com.alvesjv.projecthexagonalfull.config;

import com.alvesjv.projecthexagonalfull.app.domain.entities.User;
import com.alvesjv.projecthexagonalfull.app.domain.enums.Role;
import com.alvesjv.projecthexagonalfull.app.ports.out.DataBaseIntegration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Configuration
public class DataLoader {

    @Autowired
    @Qualifier("car")
    private DataBaseIntegration carDataBase;

    @Autowired
    @Qualifier("user")
    private DataBaseIntegration userDataBase;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void run() throws Exception{
        createUserTest();
    }

    private void createUserTest(){
        User user1 = new User();
        user1.setIdUser(UUID.fromString("f4eff9cf-e496-427c-9629-bd35edaa2190"));
        user1.setUsername("joao");
        user1.setPassword(passwordEncoder.encode("123"));
        user1.setRole(Role.USER);
        userDataBase.save(user1);

        User user2 = new User();
        user2.setIdUser(UUID.fromString("bac239f8-4d1a-4f00-b737-36451de7cc84"));
        user2.setUsername("maria");
        user2.setRole(Role.ADMIN);
        user2.setPassword(passwordEncoder.encode("456"));
        userDataBase.save(user2);

        User user3 = new User();
        user3.setIdUser(UUID.fromString("203b6558-ed6e-4360-965f-418264abc58f"));
        user3.setUsername("rafael");
        user3.setRole(Role.USER);
        user3.setPassword(passwordEncoder.encode("789"));
        userDataBase.save(user3);
    }

}
