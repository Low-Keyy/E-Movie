package team.pi.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SecurityTest {
    @Autowired
    PasswordEncoder bCryptPasswordEncoder;
    @Test
    public void testBcryptPasswordEncoder() {

        String encode = bCryptPasswordEncoder.encode("1234");
        String encode1 = bCryptPasswordEncoder.encode("1234");
        System.out.println(encode);
        System.out.println(encode1);
        System.out.println(bCryptPasswordEncoder.matches("1234","$2a$10$3bOts2TPL.1cL8.kTcp2LuawpqAUpNb9NJqn6mEl9/48XiyHT7W6K"));
    }
}
