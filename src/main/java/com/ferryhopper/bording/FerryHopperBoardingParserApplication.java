package com.ferryhopper.bording;

import com.ferryhopper.bording.converter.ConverterFromBPC;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@RequiredArgsConstructor
@SpringBootApplication
public class FerryHopperBoardingParserApplication implements CommandLineRunner {

    private final ConverterFromBPC converterFromBPC;

    public static void main(String[] args) {
        SpringApplication.run(FerryHopperBoardingParserApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        List.of("web_checkin", "paper_coupon_kiosk", "boarding_pass_kiosk")
                .forEach(converterFromBPC::run);
    }
}
