package team.legend.jobhunter.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import team.legend.jobhunter.model.OfferInfo;

import java.util.List;

@Mapper
@Component
public interface OfferDao {

        @Select("SELECT * FROM  offer_info t1 " +
            "JOIN (SELECT offer_id FROM offer_info limit #{pagesize}, #{page} ) t2 " +
            "ON t1.offer_id = t2.offer_id")
        List<OfferInfo> selectOfferInfo(int pagesize, int page);

        @Select("SELECT * FROM offer_info WHERE offer_id = #{offer_id}")
        OfferInfo selectOfferInfoByOfferId(@Param("offer_id") String offer_id);

        @Insert("INSERT INTO offer_info(offer_id,offer_name,offer_requirement,offer_location," +
                "offer_company_name,offer_type, offer_description, offer_post_way,offer_salary)" +
                "VALUES(#{offer_id},#{offer_name},#{offer_requirement},#{offer_location}," +
                "#{offer_company_name},#{offer_type}, #{offer_description}, #{offer_post_way}, #{offer_salary})" +
                "ON DUPLICATE KEY UPDATE offer_name = #{offer_name},offer_requirement = #{offer_requirement}," +
                "offer_location = #{offer_location},offer_company_name = #{offer_company_name},offer_type = #{offer_type}, " +
                "offer_description = #{offer_description}, offer_post_way = #{offer_post_way},offer_salary = #{offer_salary} ")
        int insertOfferInfo(OfferInfo offerInfo);
}
