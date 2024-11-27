package lyzzcw.work.zspi.handler.dao;

import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public interface DBProxyDao {
     SqlSessionFactory getSqlSessionFactory();
     String getService(String proxy);
     List<DBProxy> getAllServices();
}
