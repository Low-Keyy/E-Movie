package team.pi.demo;

import io.jsonwebtoken.Claims;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import team.pi.demo.utils.JwtUtil;

import team.pi.demo.utils.JwtUtil;

import static team.pi.demo.utils.JwtUtil.createJWT;
import static team.pi.demo.utils.JwtUtil.parseJWT;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UtilsTest {

    @Test
    public void testJWT() throws Exception {

        String jwt = createJWT("2123");
        String jwt1 = createJWT("2123");
        Claims claims = parseJWT(jwt);
        String subject = claims.getSubject();
        System.out.println(jwt);
        System.out.println(jwt1);
        System.out.println(subject);
    }
}
