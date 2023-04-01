package team.pi.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import team.pi.demo.pojo.Person;

import java.util.List;

@Mapper
public interface PersonMapper {

    List<Person> selectByName(@Param("name") String name);
}
