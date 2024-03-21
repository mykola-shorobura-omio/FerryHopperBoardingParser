package com.ferryhopper.bording;

import com.ferryhopper.bording.converter.ConverterFromBPC;
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
        converterFromBPC.run();
//        String template = parseThymeleafTemplate();
//        generatePdfFromHtml(template);
    }
}
