package team.legend.jobhunter.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import team.legend.jobhunter.model.PriceItem;

@Mapper
@Component
public interface PriceDao {

    @Insert("INSERT INTO origin_price (price ,type) VALUES(#{price},#{type})")
    int insertPrice(Integer price,Integer type);

    @Select("SELECT price , discount FROM origin_price WHERE type = #{type}")
    PriceItem selectPriceByType(int type);
}
