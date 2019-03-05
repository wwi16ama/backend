package com.WWI16AMA.backend_api.Member;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class MemberIdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {

        Connection connection = sharedSessionContractImplementor.connection();
        PreparedStatement statement = null;
        ResultSet r = null;
        try {
            statement = connection.prepareStatement("SELECT id FROM member where id = ?");

            int randomId;
            Random random = new Random();
            do {
                randomId = 1000 + random.nextInt(9000);
                statement.setInt(1, randomId);
                r = statement.executeQuery();
            } while (r.next());
            return randomId;

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
