package com.WWI16AMA.backend_api;

import com.WWI16AMA.backend_api.Member.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;


@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner demo(MemberRepository memberRepository, OfficeRepository officeRepository) {
        return (args) -> {

            officeRepository.saveAll(initOfficeTable());

            generateSomeMembers(memberRepository, initOfficeTable());
        };
    }

    private static ArrayList<Office> initOfficeTable() {

        Office office = Office.FLUGWART;
        Office office1 = Office.IMBETRIEBSKONTROLLTURMARBEITEND;
        Office office2 = Office.KASSIERER;
        Office office3 = Office.VORSTANDSVORSITZENDER;
        Office office4 = Office.SYSTEMADMINISTRATOR;

        ArrayList<Office> list = new ArrayList<>();
        list.add(office);
        list.add(office1);
        list.add(office2);
        list.add(office3);
        list.add(office4);

        return list;

    }

    private static void generateSomeMembers(MemberRepository memberRepository, List<Office> offices){
        Address adr = new Address(25524, "Itzehoe", "Twietbergstraße 53");
        Member mem = new Member("Karl", "Hansen",
                LocalDate.of(1996, Month.DECEMBER, 21), Gender.MALE, Status.PASSIVE,
                "karl.hansen@mail.com", adr, "123456789", false);

        mem.setOffices(offices);
        memberRepository.save(mem);

        Address adr1 = new Address(12345, "Hamburg", "Hafenstraße 5");
        Member mem1 = new Member("Kurt", "Krömer",
                LocalDate.of(1975, Month.DECEMBER, 2), Gender.MALE, Status.PASSIVE,
                "karl.hansen@mail.com", adr, "123456789", false);
        mem1.setAddress(adr1);

        memberRepository.save(mem1);
    }
}
