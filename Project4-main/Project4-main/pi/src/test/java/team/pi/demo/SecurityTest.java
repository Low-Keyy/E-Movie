package team.pi.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SecurityTest {
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Test
    public void testBcryptPasswordEncoder() {

        String encode = bCryptPasswordEncoder.encode("123456");
        String encode1 = bCryptPasswordEncoder.encode("123456");
        System.out.println(encode);
        System.out.println(encode1);
        System.out.println(bCryptPasswordEncoder.matches("1234","$2a$10$6rNsLXYoJuO9nX2U1ZP6UebWou32/ZgOnFE9KhDPVn1SjVtu90IXa"));
    }
}
