package com.WWI16AMA.backend_api.Account.CustomGenerator;

import com.WWI16AMA.backend_api.Account.AccountRepository;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AccountIdGenerator implements IdentifierGenerator {

    final int START_NUMBER = 37459;
    @Autowired
    AccountRepository accountRepository;

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {

        Connection connection = sharedSessionContractImplementor.connection();
        Statement statement = null;
        ResultSet r = null;
        try {
            statement = connection.createStatement();
            r = statement.executeQuery("select max(id) from account");
            if (r.next()) {
                if (r.getInt(1) == 0) {
                    return START_NUMBER;
                } else {
                    return r.getInt(1) + 1;
                }
            }

        } catch (Exception ex) {

            /*

           We are not closing our sql connection because the method does not create a new connection, it reuses one
           which already exists. If you close the connection, other resources cannot retrieve information from the database
           This lead to exceptions
             */
        } finally {
            try {
                statement.close();
                r.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        return null;
    }


}
