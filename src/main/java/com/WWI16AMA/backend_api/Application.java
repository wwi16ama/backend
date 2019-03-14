package com.WWI16AMA.backend_api;

import com.WWI16AMA.backend_api.Account.AccountRepository;
import com.WWI16AMA.backend_api.Credit.Credit;
import com.WWI16AMA.backend_api.Credit.CreditRepository;
import com.WWI16AMA.backend_api.Credit.Period;
import com.WWI16AMA.backend_api.Fee.Fee;
import com.WWI16AMA.backend_api.Fee.FeeRepository;
import com.WWI16AMA.backend_api.Member.*;
import com.WWI16AMA.backend_api.Plane.Plane;
import com.WWI16AMA.backend_api.Plane.PlaneRepository;
import com.WWI16AMA.backend_api.PlaneLog.PlaneLogEntry;
import com.WWI16AMA.backend_api.Service.ServiceName;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    private static void generateSomeMembers(MemberRepository memberRepository,
                                            List<Office> offices,
                                            PasswordEncoder enc) {

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
                "karl.hansen@mail.com", adr, "DE12345678901234567890", false,
                enc.encode("koala"));

        mem.setOffices(offices);
        mem.setFlightAuthorization(flList);
        memberRepository.save(mem);
        System.out.println("MemberID: " + mem.getId());

        Address adr1 = new Address(12345, "Hamburg", "Hafenstraße 5");
        Member mem1 = new Member("Kurt", "Krömer",
                LocalDate.of(1975, Month.DECEMBER, 2), Gender.MALE, Status.PASSIVE,
                "kurt.krömer@mail.com", adr, "DE12345678901234567890", false,
                enc.encode("koala"));
        mem1.setAddress(adr1);

        memberRepository.save(mem1);
        System.out.println("MemberID: " + mem1.getId());
    }

    private static void generateSomePlanes(PlaneRepository planeRepository) throws Exception {

        FlightAuthorization.Authorization auth = FlightAuthorization.Authorization.PPLA;
        FlightAuthorization.Authorization auth1 = FlightAuthorization.Authorization.PPLB;
        Plane plane1 = new Plane("D-ERFI", "Diamond DA-40 TDI", auth, "Halle 1",
                new URL("https://static.independent.co.uk/s3fs-public/thumbnails/image/2017/03/23/17/electricplane.jpg?w968h681"),
                4.60, 1.60);
        Plane plane2 = new Plane("D-EJEK", "DR 400 Remorqueur", auth, "Halle 1",
                new URL("https://static.independent.co.uk/s3fs-public/thumbnails/image/2017/03/23/17/electricplane.jpg?w968h681"),
                6.0, 1.8);
        Plane plane3 = new Plane("D-KNIF", "SF25C Falke", auth1, "Halle 2",
                new URL("https://static.independent.co.uk/s3fs-public/thumbnails/image/2017/03/23/17/electricplane.jpg?w968h681"),
                2.40, 0.65);
        Plane plane4 = new Plane("D-KMGA", "Diamond HK36 Dimona", auth1, "Halle 2",
                new URL("https://static.independent.co.uk/s3fs-public/thumbnails/image/2017/03/23/17/electricplane.jpg?w968h681"),
                3.60, 0.85);
        generateSomePlaneLogs(plane1);
        Plane[] planes = {plane1, plane2, plane3, plane4};
        planeRepository.saveAll(Arrays.asList(planes));
    }

    private static void generateSomeFees(FeeRepository feeRepository) {

        Fee fee1 = new Fee(Fee.Status.ACTIVE, 220);
        Fee fee2 = new Fee(Fee.Status.U20ACTIVE, 150);
        Fee fee3 = new Fee(Fee.Status.PASSIVE, 80);
        Fee fee4 = new Fee(Fee.Status.HONORARYMEMBER, 0);
        Fee[] fees = {fee1, fee2, fee3, fee4};
        feeRepository.saveAll(Arrays.asList(fees));
    }

    private static void generateSomeCredits(CreditRepository creditRepository) {

        Credit c1 = new Credit(ServiceName.VORSTANDSMITGLIED, 200.0, Period.YEAR);
        Credit c2 = new Credit(ServiceName.FLUGLEHRER, 200.0, Period.YEAR);
        Credit c3 = new Credit(ServiceName.FLUGWART, 100.0, Period.YEAR);

        Credit c4 = new Credit(ServiceName.TAGESEINSATZ, 40.0, Period.DAY);
        Credit c5 = new Credit(ServiceName.PILOT, 40.0, Period.DAY);
        Credit[] credits = {c1, c2, c3, c4, c5};
        creditRepository.saveAll(Arrays.asList(credits));
    }

    private static void generateSomePlaneLogs(Plane plane) {

        PlaneLogEntry e1 = new PlaneLogEntry(LocalDateTime.now(), "Zuhause", 69, 88, 5);
        PlaneLogEntry e2 = new PlaneLogEntry(LocalDateTime.now(), "Zuhause", 88, 97, 50);
        PlaneLogEntry e3 = new PlaneLogEntry(LocalDateTime.now(), "Zuhause", 97, 150, 500);
        PlaneLogEntry e4 = new PlaneLogEntry(LocalDateTime.now(), "Zuhause", 150, 896, 5000);
        PlaneLogEntry e5 = new PlaneLogEntry(LocalDateTime.now(), "Zuhause", 896, 1000, 5000);
        PlaneLogEntry e6 = new PlaneLogEntry(LocalDateTime.now(), "Zuhause", 1000, 1001, 50000);
        PlaneLogEntry e7 = new PlaneLogEntry(LocalDateTime.now(), "Zuhause", 1001, 2000, 500000);
        PlaneLogEntry[] entries = {e1, e2, e3, e4, e5, e6, e7};

        for (PlaneLogEntry entry : entries) {
            plane.getPlaneLog().addPlaneLogEntry(entry);
        }

    }

    @Bean
    public CommandLineRunner demo(MemberRepository memberRepository, OfficeRepository officeRepository,
                                  PlaneRepository planeRepository, AccountRepository accountRepository,
                                  FeeRepository feeRepository, CreditRepository creditRepository, PasswordEncoder passwordEncoder) {
        return (args) -> {

            List<Office> offices = initOfficeTable();
            officeRepository.saveAll(offices);

            generateSomeMembers(memberRepository, offices, passwordEncoder);
            generateSomePlanes(planeRepository);
            generateSomeFees(feeRepository);
            generateSomeCredits(creditRepository);

        };
    }
}
