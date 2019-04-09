package com.WWI16AMA.backend_api;

import com.WWI16AMA.backend_api.Account.AccountRepository;
import com.WWI16AMA.backend_api.Billing.BillingTask;
import com.WWI16AMA.backend_api.Credit.Credit;
import com.WWI16AMA.backend_api.Credit.CreditRepository;
import com.WWI16AMA.backend_api.Credit.Period;
import com.WWI16AMA.backend_api.Fee.Fee;
import com.WWI16AMA.backend_api.Fee.FeeRepository;
import com.WWI16AMA.backend_api.Member.*;
import com.WWI16AMA.backend_api.PilotLog.PilotLogEntry;
import com.WWI16AMA.backend_api.Plane.Plane;
import com.WWI16AMA.backend_api.Plane.PlaneRepository;
import com.WWI16AMA.backend_api.PlaneLog.PlaneLogEntry;
import com.WWI16AMA.backend_api.Service.Service;
import com.WWI16AMA.backend_api.Service.ServiceName;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.WWI16AMA.backend_api.Billing.BillingTask.getNextBillingDate;


@SpringBootApplication
@EnableScheduling
@EnableAsync
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

    private static List<Member> generateSomeMembers(MemberRepository memberRepository,
                                                    List<Office> offices,
                                                    PasswordEncoder enc, ApplicationEventPublisher publisher) {

        FlightAuthorization fl1 = new FlightAuthorization(FlightAuthorization.Authorization.PPLA,
                LocalDate.of(2017, 11, 11),
                LocalDate.of(2019, 11, 10));
        FlightAuthorization fl2 = new FlightAuthorization(FlightAuthorization.Authorization.BZFI,
                LocalDate.of(2016, 10, 13),
                LocalDate.of(2018, 10, 12));
        List<FlightAuthorization> flList = new ArrayList<>();
        flList.add(fl1);
        flList.add(fl2);

        Address adr = new Address("25524", "Itzehoe", "Twietbergstraße 53");
        Member mem = new Member("Karl", "Hansen",
                LocalDate.of(1996, Month.DECEMBER, 21), Gender.MALE, Member.Status.ACTIVE,
                "karl.hansen@mail.com", adr, "DE12345678901234567890", false,
                enc.encode("koala"));
        // publisher.publishEvent(new EmailNotificationEvent(mem));
        mem.setOffices(offices.stream().filter((of) -> of.getTitle().equals(Office.Title.VORSTANDSVORSITZENDER)).collect(Collectors.toList()));
        mem.setFlightAuthorization(flList);
        mem.setId(9999);
        generateSomePilotLogEntries(mem);
        memberRepository.save(mem);
        System.out.println("Vorstandsvorsitzender:\t" + mem.getId());

        Address adr1 = new Address("25524", "Itzehoe", "Twietbergstraße 53");
        Member mem1 = new Member("Karl", "Hansen",
                LocalDate.of(1996, Month.DECEMBER, 21), Gender.MALE, Member.Status.ACTIVE,
                "karl.hansen@mail.com", adr1, "DE12345678901234567890", false,
                enc.encode("koala"));
        // publisher.publishEvent(new EmailNotificationEvent(mem1));
        mem1.setOffices(offices.stream().filter((of) -> of.getTitle().equals(Office.Title.KASSIERER)).collect(Collectors.toList()));
        generateSomePilotLogEntries(mem1);
        memberRepository.save(mem1);
        System.out.println("Kassierer:\t\t" + mem1.getId());

        Address adr2 = new Address("25524", "Itzehoe", "Twietbergstraße 53");
        Member mem2 = new Member("Karl", "Hansen",
                LocalDate.of(1996, Month.DECEMBER, 21), Gender.MALE, Member.Status.ACTIVE,
                "karl.hansen@mail.com", adr2, "DE12345678901234567890", false,
                enc.encode("koala"));
        // publisher.publishEvent(new EmailNotificationEvent(mem2));
        mem2.setOffices(offices.stream().filter((of) -> of.getTitle().equals(Office.Title.FLUGWART)).collect(Collectors.toList()));
        generateSomePilotLogEntries(mem2);
        memberRepository.save(mem2);
        System.out.println("Flugwart:\t\t" + mem2.getId());

        Address adr3 = new Address("12345", "Hamburg", "Hafenstraße 5");
        Member mem3 = new Member("Kurt", "Krömer",
                LocalDate.of(1975, Month.DECEMBER, 2), Gender.MALE, Member.Status.ACTIVE,
                "kurt.krömer@mail.com", adr, "DE12345678901234567890", false,
                enc.encode("koala"));
        mem3.setAddress(adr3);
        generateSomePilotLogEntries(mem3);
        memberRepository.save(mem3);
        System.out.println("active MemberID:\t" + mem3.getId());

        Address adr4 = new Address("22345", "Hamburg", "Hafenstraße 5");
        Member mem4 = new Member("Kurt", "Krömer",
                LocalDate.of(1975, Month.DECEMBER, 2), Gender.MALE, Member.Status.PASSIVE,
                "kurt.krömer@mail.com", adr, "DE22345678902234567890", false,
                enc.encode("koala"));
        mem4.setAddress(adr4);
        generateSomePilotLogEntries(mem4);
        memberRepository.save(mem4);
        System.out.println("passive MemberID:\t" + mem4.getId());
        return Arrays.asList(mem, mem1, mem2, mem3, mem4);
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

        Credit c1 = new Credit(ServiceName.J_VORSTANDSMITGLIED, 200.0, Period.YEAR);
        Credit c2 = new Credit(ServiceName.J_FLUGLEHRER, 200.0, Period.YEAR);
        Credit c3 = new Credit(ServiceName.J_FLUGWART, 100.0, Period.YEAR);

        Credit c4 = new Credit(ServiceName.T_TAGESEINSATZ, 40.0, Period.DAY);
        Credit c5 = new Credit(ServiceName.T_PILOT, 40.0, Period.DAY);
        Credit[] credits = {c1, c2, c3, c4, c5};
        creditRepository.saveAll(Arrays.asList(credits));
    }


    private static void generateSomePlaneLogs(PlaneRepository planeRepository, MemberRepository memberRepository) {
        Member member = memberRepository.findAll().iterator().next();
        Plane plane = planeRepository.findById(1).get();

        PlaneLogEntry e1 = new PlaneLogEntry(LocalDateTime.now(), member.getId(), "Reilingen", 69, 88, 5);
        PlaneLogEntry e2 = new PlaneLogEntry(LocalDateTime.now(), member.getId(), "Reilingen", 88, 97, 10);
        PlaneLogEntry e3 = new PlaneLogEntry(LocalDateTime.now(), member.getId(), "Reilingen", 97, 150, 30);
        PlaneLogEntry e4 = new PlaneLogEntry(LocalDateTime.now(), member.getId(), "Reilingen", 150, 896, 5000);
        PlaneLogEntry e5 = new PlaneLogEntry(LocalDateTime.now(), member.getId(), "Reilingen", 896, 1000, 200);
        PlaneLogEntry e6 = new PlaneLogEntry(LocalDateTime.now(), member.getId(), "Reilingen", 1000, 1001, 2);
        PlaneLogEntry e7 = new PlaneLogEntry(LocalDateTime.now(), member.getId(), "Reilingen", 1001, 2000, 800);
        PlaneLogEntry[] entries = {e1, e2, e3, e4, e5, e6, e7};

        for (PlaneLogEntry entry : entries) {
            plane.addPlaneLogEntry(entry);
        }

        planeRepository.save(plane);

    }

    private static void generateSomePilotLogEntries(Member member) {
        PilotLogEntry ple1 = new PilotLogEntry("D-ERFI", "Reilingen", LocalDateTime.of(2019, Month.FEBRUARY,
                15, 10, 30), "Mannheim", LocalDateTime.of(2019, Month.FEBRUARY,
                15, 10, 45), true, 1);
        PilotLogEntry ple2 = new PilotLogEntry("D-EJEK", "Mannheim", LocalDateTime.of(2019, Month.FEBRUARY,
                20, 10, 00), "Berlin", LocalDateTime.of(2019, Month.FEBRUARY,
                20, 13, 45), true, 2);
        PilotLogEntry ple3 = new PilotLogEntry("D-EJEK", "Berlin", LocalDateTime.of(2019, Month.MARCH,
                03, 10, 00), "Reilingen", LocalDateTime.of(2019, Month.MARCH,
                03, 14, 30), true, 3);
        PilotLogEntry ple4 = new PilotLogEntry("D-EJEK", "Reilingen", LocalDateTime.of(2019, Month.MARCH,
                20, 12, 00), "Reilingen", LocalDateTime.of(2019, Month.MARCH,
                20, 13, 00), false, 1);

        PilotLogEntry[] pilotLogEntries = {ple1, ple2, ple3, ple4};
        for (PilotLogEntry entry : pilotLogEntries) {
            member.getPilotLog().addPilotLogEntry(entry);
        }
    }

    private void generateSomeServices(MemberRepository memberRepository, CreditRepository creditRepository) {
        Member mem = memberRepository.findAll().iterator().next();

        Service s0 = new Service(ServiceName.T_PILOT, LocalDate.of(1, 2, 3), LocalDate.of(1, 2, 3), 123);
        Service s1 = new Service(ServiceName.T_TAGESEINSATZ, LocalDate.of(3, 3, 3), LocalDate.of(4, 4, 4), 123);
        Service s2 = new Service(ServiceName.J_FLUGLEHRER, getNextBillingDate().minusYears(1), getNextBillingDate().minusDays(1), 123);


        Service[] sArr = {s0, s1, s2};
        mem.setServices(Arrays.asList(sArr));
        memberRepository.save(mem);
    }

    @Bean
    public CommandLineRunner demo(MemberRepository memberRepository, OfficeRepository officeRepository,
                                  PlaneRepository planeRepository, AccountRepository accountRepository,
                                  FeeRepository feeRepository, CreditRepository creditRepository,
                                  PasswordEncoder passwordEncoder, ApplicationEventPublisher publisher) {
        return (args) -> {

            List<Office> offices = initOfficeTable();
            officeRepository.saveAll(offices);

            List<Member> memberList = generateSomeMembers(memberRepository, offices, passwordEncoder, publisher);
            generateSomePlanes(planeRepository);
            generateSomeFees(feeRepository);
            generateSomeCredits(creditRepository);
            generateSomePlaneLogs(planeRepository, memberRepository);
            generateSomeServices(memberRepository, creditRepository);

            BillingTask bt = new BillingTask(accountRepository, feeRepository, memberRepository, publisher);
            memberList.stream().forEach(bt::calculateEntranceFee);
        };
    }

    @Bean
    public BillingTask startBillingTask(AccountRepository accountRepository, FeeRepository feeRepository, MemberRepository memberRepository, ApplicationEventPublisher publisher) {

        return new BillingTask(accountRepository, feeRepository, memberRepository, publisher);
    }
}
