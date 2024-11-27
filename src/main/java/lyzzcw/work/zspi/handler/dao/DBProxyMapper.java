package lyzzcw.work.zspi.handler.dao;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DBProxyMapper {

    @Select("SELECT service FROM db_proxy WHERE proxy = #{proxy} limit 1")
    String getService(String proxy);


    @Select("SELECT * FROM db_proxy")
    @Results(value = {
            @Result(column = "id", property = "id"),
            @Result(column = "service", property = "service"),
            @Result(column = "proxy", property = "proxy"),
            @Result(column = "content", property = "content")
    })
    List<DBProxy> getAllServices();
}
