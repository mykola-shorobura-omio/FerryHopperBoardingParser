package com.ferryhopper.bording;

import com.ferryhopper.bording.parser.KiosksParser;
import com.ferryhopper.bording.parser.KiosksParser.KioskInfo;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FerryHopperBoardingParserApplication implements CommandLineRunner {

    private final KiosksParser kiosksParser;

    public FerryHopperBoardingParserApplication(KiosksParser kiosksParser) {
        this.kiosksParser = kiosksParser;
    }

    public static void main(String[] args) {
        SpringApplication.run(FerryHopperBoardingParserApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        var urlGri = "https://www.ferryhopper.com/en/reservation/pickups/FH12UM8584TJ";
        var urlFin = "https://www.ferryhopper.com/en/reservation/pickups/FH58JX1251GP";
        var urlLfg = "https://www.ferryhopper.com/en/reservation/pickups/FH52RR3546PQ";
        var urlSes = "https://www.ferryhopper.com/en/reservation/pickups/FH89SK4160ML";
        var urlTra = "https://www.ferryhopper.com/en/reservation/pickups/FH49BW4632IO";
        var urlVnn = "https://www.ferryhopper.com/en/reservation/pickups/FH93PU1032LH";
        var urlSnavc = "https://www.ferryhopper.com/en/reservation/pickups/FH25VB6300CJ";
        var urlVik = "https://www.ferryhopper.com/en/reservation/pickups/FH52YD1061IR";
        var urlTni = "https://www.ferryhopper.com/en/reservation/pickups/FH75LV1060SO";
        var urlEckr = "https://www.ferryhopper.com/en/reservation/pickups/FH09NL7084ZF";
        var urlAnsfad = "https://www.ferryhopper.com/en/reservation/pickups/FH34XL3292XS";
        var urlFjrd = "https://www.ferryhopper.com/en/reservation/pickups/FH96SR2265TR";
        var urlTur = "https://www.ferryhopper.com/en/reservation/pickups/FH75YS2671HA";
        var urlAdr = "https://www.ferryhopper.com/en/reservation/pickups/FH18ON0977DS";
        var urlTtli = "https://www.ferryhopper.com/en/reservation/pickups/FH41PV1028LL";
        var urlSfo = "https://www.ferryhopper.com/en/reservation/pickups/FH19OU2445BP";
        var urlYsm = "https://www.ferryhopper.com/en/reservation/pickups/FH21MF6659EX";
        var urlJdlji = "https://www.ferryhopper.com/en/reservation/pickups/FH38HF0592UA";
        var urlLmrd = "https://www.ferryhopper.com/en/reservation/pickups/FH66AE3497BG";
        List<String> urls = List.of(
            urlGri, urlFin, urlLfg, urlSes, urlTra, urlVnn, urlSnavc, urlVik, urlTni, urlEckr, urlAnsfad, urlFjrd, urlTur, urlAdr,
            urlTtli, urlSfo, urlYsm, urlJdlji, urlLmrd);

        System.out.println("---------------------------------------------");
        for (String url : urls) {
            List<KioskInfo> kiosks = kiosksParser.parse(url);;
            for (KioskInfo kiosk : kiosks) {
                System.out.println("Kiosk URL: " + kiosk.getUrl());
                System.out.println("Kiosk Title: " + kiosk.getTitle());
                System.out.println("Spot Address Name: " + kiosk.getSpotAddressName());
                System.out.println("Spot Address Details: " + kiosk.getSpotAddressDetails());
                System.out.println("Spot Phones: " + String.join(", ", kiosk.getSpotPhones()));
                System.out.println("Spot Notes: " + String.join(", ", kiosk.getSpotNotes()));
                System.out.println("---------------------------------------------");
            }
            System.out.println("---------------------------------------------");
            System.out.println("---------------------------------------------");
        }
    }
}
