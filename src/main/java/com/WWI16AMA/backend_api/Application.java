package com.WWI16AMA.backend_api;

import com.WWI16AMA.backend_api.Account.AccountRepository;
import com.WWI16AMA.backend_api.Member.*;
import com.WWI16AMA.backend_api.Plane.Plane;
import com.WWI16AMA.backend_api.Plane.PlaneRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    private static List<Office> initOfficeTable() {

        Office office = new Office(Office.Title.FLUGWART);
        Office office1 = new Office(Office.Title.IMBETRIEBSKONTROLLTURMARBEITEND);
        Office office2 = new Office(Office.Title.KASSIERER);
        Office office3 = new Office(Office.Title.SYSTEMADMINISTRATOR);
        Office office4 = new Office(Office.Title.VORSTANDSVORSITZENDER);

        Office[] offices = {office, office1, office2, office3, office4};
        return Arrays.asList(offices);
    }

    private static void generateSomeMembers(MemberRepository memberRepository, List<Office> offices) {

        FlightAuthorization fl1 = new FlightAuthorization(FlightAuthorization.Authorization.PPLA,
                LocalDate.of(2017, 11, 11),
                LocalDate.of(2019, 11, 10));
        FlightAuthorization fl2 = new FlightAuthorization(FlightAuthorization.Authorization.BZFI,
                LocalDate.of(2016, 10, 13),
                LocalDate.of(2018, 10, 12));
        List<FlightAuthorization> flList = new ArrayList<>();
        flList.add(fl1);
        flList.add(fl2);

        Address adr = new Address(25524, "Itzehoe", "Twietbergstraße 53");
        Member mem = new Member("Karl", "Hansen",
                LocalDate.of(1996, Month.DECEMBER, 21), Gender.MALE, Status.PASSIVE,
                "karl.hansen@mail.com", adr, "DE12345678901234567890", false);

        mem.setOffices(offices);
        mem.setFlightAuthorization(flList);
        memberRepository.save(mem);

        Address adr1 = new Address(12345, "Hamburg", "Hafenstraße 5");
        Member mem1 = new Member("Kurt", "Krömer",
                LocalDate.of(1975, Month.DECEMBER, 2), Gender.MALE, Status.PASSIVE,
                "karl.hansen@mail.com", adr, "DE12345678901234567890", false);
        mem1.setAddress(adr1);

        memberRepository.save(mem1);
    }

    private static void generateSomePlanes(PlaneRepository planeRepository) {

        FlightAuthorization.Authorization auth = FlightAuthorization.Authorization.PPLA;
        FlightAuthorization.Authorization auth1 = FlightAuthorization.Authorization.PPLB;
        Plane plane1 = new Plane("D-ERFI", "Diamond DA-40 TDI", auth, "Halle1");
        Plane plane2 = new Plane("D-EJEK", "DR 400 Remorqueur", auth, "Halle1");
        Plane plane3 = new Plane("D-KNIF", " SF25C Falke", auth1, "Halle2");
        Plane plane4 = new Plane("D-KMGA", "Diamond HK36 Diamona", auth1, "Halle2");
        Plane[] planes = {plane1, plane2, plane3, plane4};
        planeRepository.saveAll(Arrays.asList(planes));
    }


    @Bean
    public CommandLineRunner demo(MemberRepository memberRepository, OfficeRepository officeRepository, PlaneRepository planeRepository, AccountRepository accountRepository) {
        return (args) -> {

            List<Office> offices = initOfficeTable();
            officeRepository.saveAll(offices);

            generateSomeMembers(memberRepository, offices);

            generateSomePlanes(planeRepository);
        };
    }
}
