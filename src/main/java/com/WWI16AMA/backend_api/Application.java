package com.WWI16AMA.backend_api;

import com.WWI16AMA.backend_api.Member.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CommandLineRunner demo(MemberRepository repository) {
		return (args) -> {
            Address address = new Address(71706,"Markgröningen","HabIchDirSchonMalGeschrieben-Gasse",123);
			Member member = new Member("Matthis","Gördel", LocalDate.of(1996, Month.NOVEMBER, 22),"m",Status.ACTIVE,"marg667@outlook.com",address,"32323232142",true);
            FlightAuthorization flights = new FlightAuthorization();
            flights.setAuthorization("PPLA");
            flights.setDateOfIssue(LocalDate.of(1993, Month.MAY, 15));
            flights.setExpires(LocalDate.of(1993, Month.MAY, 15));
            member.setFlightAuthorization(flights);



			repository.save(member);

			Address address1 = new Address(71705,"Möglingen","HabIchDirSchonMalGeschrieben-Strasse",321);
			Member member1 = new Member("Jörg","Granini", LocalDate.of(1993, Month.MAY, 15),"m",Status.PASSIVE,"marg667@web.com",address1,"334324322142",true);
			FlightAuthorization flights1 = new FlightAuthorization();
			flights1.setAuthorization("PPLA");
			flights1.setDateOfIssue(LocalDate.of(1993, Month.MAY, 15));
			flights1.setExpires(LocalDate.of(1993, Month.MAY, 15));
			member1.setFlightAuthorization(flights1);



			repository.save(member1);
		};
	}
}
