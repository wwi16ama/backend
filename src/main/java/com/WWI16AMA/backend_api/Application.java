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
            flights.setDateOfIssue(new Date(20170909));
            flights.setExpires(new Date(20180909));
            member.setFlightAuthorization(flights);



			repository.save(member);
		};
	}
}
