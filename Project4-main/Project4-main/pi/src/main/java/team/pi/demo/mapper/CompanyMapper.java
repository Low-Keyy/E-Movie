package team.pi.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import team.pi.demo.pojo.Company;

@Mapper
public interface CompanyMapper {

    Company selectByCompanyName(@Param("companyName") String name);
}
