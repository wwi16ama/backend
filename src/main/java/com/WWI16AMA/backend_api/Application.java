package com.WWI16AMA.backend_api;

import com.WWI16AMA.backend_api.Plane.*;
import com.WWI16AMA.backend_api.Member.*;
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
    public CommandLineRunner demo(MemberRepository memberRepository, OfficeRepository officeRepository, PlaneRepository planeRepository) {
        return (args) -> {

            List<Office> offices = initOfficeTable();
            officeRepository.saveAll(offices);

            generateSomeMembers(memberRepository, offices);

            FlightAuthorization.Authorization auth = FlightAuthorization.Authorization.PPLA;

            Plane plane1 = new Plane("D-ERFI","Diamond DA-40 TDI", auth,"Halle1");

            planeRepository.save(plane1);




        };
    }

    private static ArrayList<Office> initOfficeTable() {

        Office office = new Office(Office.OfficeName.FLUGWART);
        Office office1 = new Office(Office.OfficeName.IMBETRIEBSKONTROLLTURMARBEITEND);
        Office office2 = new Office(Office.OfficeName.KASSIERER);
        Office office3 = new Office(Office.OfficeName.SYSTEMADMINISTRATOR);
        Office office4 = new Office(Office.OfficeName.VORSTANDSVORSITZENDER);

        ArrayList<Office> list = new ArrayList<>();
        list.add(office);
        list.add(office1);
        list.add(office2);
        list.add(office3);
        list.add(office4);

        return list;
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
                "karl.hansen@mail.com", adr, "123456789", false);

        mem.setOffices(offices);
        mem.setFlightAuthorization(flList);
        memberRepository.save(mem);

        Address adr1 = new Address(12345, "Hamburg", "Hafenstraße 5");
        Member mem1 = new Member("Kurt", "Krömer",
                LocalDate.of(1975, Month.DECEMBER, 2), Gender.MALE, Status.PASSIVE,
                "karl.hansen@mail.com", adr, "123456789", false);
        mem1.setAddress(adr1);

        memberRepository.save(mem1);
    }
}
