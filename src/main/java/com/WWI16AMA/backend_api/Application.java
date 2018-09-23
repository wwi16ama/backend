package com.WWI16AMA.backend_api;

import com.WWI16AMA.backend_api.Member.HibernateUtil;
import com.WWI16AMA.backend_api.Member.MemberRepository;
import com.WWI16AMA.backend_api.Member.Office;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;


@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner demo(MemberRepository memberRepository) {
        return (args) -> {


            Session session = HibernateUtil.getSessionFactory().openSession();

            Transaction transaction = session.beginTransaction();
            initOfficeTable().forEach(office -> {
                session.save(office);
            });
            transaction.commit();
            session.close();
        };
    }

    private static ArrayList<Office> initOfficeTable() {

        Office office = new Office(Office.OfficeName.FLUGWART);
        Office office1 = new Office(Office.OfficeName.IMBETRIEBSKONTROLLTURMARBEITEND);
        Office office2 = new Office(Office.OfficeName.KASSIERER);
        Office office3 = new Office(Office.OfficeName.VORSTANDSVORSITZENDER);
        Office office4 = new Office(Office.OfficeName.SYSTEMADMINISTRATOR);

        ArrayList<Office> list = new ArrayList<>();
        list.add(office);
        list.add(office1);
        list.add(office2);
        list.add(office3);
        list.add(office4);

        return list;

    }
}
