package team.pi.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import team.pi.demo.mapper.CompanyMapper;
import team.pi.demo.mapper.MovieMapper;
import team.pi.demo.mapper.PersonMapper;
import team.pi.demo.pojo.Company;
import team.pi.demo.pojo.Movie;
import team.pi.demo.pojo.Person;

import java.util.List;


@SpringBootTest
@RunWith(SpringRunner.class)
public class MapperTest {

    @Autowired
    MovieMapper movieMapper;

    @Autowired
    PersonMapper personMapper;

    @Autowired
    CompanyMapper companyMapper;
    @Test
    public void testMovieMapper() {
        List<Movie> toy_story = movieMapper.selectByMovieName("Toy Story");
        System.out.println(toy_story);

    }
    @Test
    public void testMovieMapper1() {
        List<Movie> toy_story = movieMapper.selectByVagueMovieName("oy");
        System.out.println(toy_story);

    }

    @Test
    public void testPersonMapper() {
        List<Person> persons = personMapper.selectByName("Tom Hanks");
        System.out.println(persons);
    }

    @Test
    public void testCompanyMapper() {
        Company company = companyMapper.selectByCompanyName("Pixar Animation Studios");
        System.out.println(company);
    }


}
