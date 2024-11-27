package lyzzcw.work.zspi.handler.dao;


import lombok.Getter;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import java.util.List;
@Getter
public class DBProxyDaoImpl implements DBProxyDao {

    SqlSessionFactory sqlSessionFactory;

    public DBProxyDaoImpl(DataSource dataSource) {
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("db.proxy.env", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(DBProxyMapper.class);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    }


    @Override
    public String getService(String proxy) {
        SqlSession sqlSession = sqlSessionFactory.openSession(false);
        try {
            return sqlSession.selectOne("lyzzcw.work.zspi.handler.dao.DBProxyMapper.getService",proxy);
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public List<DBProxy> getAllServices() {
        SqlSession sqlSession = sqlSessionFactory.openSession(false);
        try {
            return sqlSession.selectList("lyzzcw.work.zspi.handler.dao.DBProxyMapper.getAllServices");
        } finally {
            sqlSession.close();
        }
    }
}
