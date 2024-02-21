package com.ferryhopper.bording.parser;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class KiosksParser {

    public List<KioskInfo> parse(String url) {
        List<KioskInfo> kioskInfos = new ArrayList<>();
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            // Set any necessary headers found in the Network tab
            request.addHeader("Accept", "application/json");
            // Execute the request
            var response = client.execute(request);
            // Get the response as a string
            String htmlContent = EntityUtils.toString(response.getEntity());
            // Parse the HTML content
            Document doc = Jsoup.parse(htmlContent);
            Elements kioskElements = doc.select("div.col-xs-12.col-sm-6.mb10");

            for (Element element : kioskElements) {
                KioskInfo kioskInfo = new KioskInfo();
                kioskInfo.setUrl(element.select("a").attr("href"));
                kioskInfo.setTitle(element.select(".confirmation__section-title").text());

                Elements spotAddresses = element.select(".spot-address");
                if (!spotAddresses.isEmpty()) {
                    kioskInfo.setSpotAddressName(spotAddresses.get(0).text());
                    if (spotAddresses.size() > 1) {
                        kioskInfo.setSpotAddressDetails(spotAddresses.get(1).text());
                    }
                }

                Elements spotPhones = element.select(".spot-phone");
                List<String> phones = new ArrayList<>();
                spotPhones.forEach(phone -> phones.add(phone.text()));
                kioskInfo.setSpotPhones(phones);

                Element spotNote = element.select(".spot-notes").first();
                if (spotNote != null) {
                    kioskInfo.setSpotNotes(spotNote.text());
                }

                kioskInfos.add(kioskInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return kioskInfos;
    }

    @Data
    public static class KioskInfo {
        private String url;
        private String title;
        private String spotAddressName;
        private String spotAddressDetails;
        private List<String> spotPhones;
        private String spotNotes;
    }

}
