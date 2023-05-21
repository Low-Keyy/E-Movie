package team.pi.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import team.pi.demo.utils.RandomUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static team.pi.demo.utils.Constants.projectPath;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UtilsTest {

    @Test
    public void testJWT() throws Exception {

//        String jwt = createJWT("2123");
//        String jwt1 = createJWT("2123");
//        Claims claims = parseJWT(jwt);
//        String subject = claims.getSubject();
//        System.out.println(jwt);
//        System.out.println(jwt1);
//        System.out.println(subject);


        System.out.println(RandomUtils.getTime());
    }
}
