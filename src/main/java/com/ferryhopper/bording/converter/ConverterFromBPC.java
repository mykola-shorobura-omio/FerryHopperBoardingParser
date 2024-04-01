package com.ferryhopper.bording.converter;

import com.lowagie.text.pdf.BaseFont;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.rythmengine.RythmEngine;
import org.rythmengine.resource.ClasspathResourceLoader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import static java.util.Locale.ENGLISH;
import static org.rythmengine.conf.RythmConfigurationKey.ENGINE_ID;
import static org.rythmengine.conf.RythmConfigurationKey.ENGINE_MODE;
import static org.rythmengine.conf.RythmConfigurationKey.FEATURE_SMART_ESCAPE_ENABLED;
import static org.rythmengine.conf.RythmConfigurationKey.I18N_LOCALE;
import static org.rythmengine.conf.RythmConfigurationKey.RESOURCE_LOADER_IMPLS;

@Component
public class ConverterFromBPC {

    private static final String RYTHM_ENGINE_ID = "GENERIC_API_TICKET_ENGINE";
    private static final Resource ARIAL_UNICODE_MS_FONT = new ClassPathResource("/templates/fonts/ArialUnicodeMS.ttf");

    private Path arialUnicodeMSFontPath;

    @SneakyThrows
    public void run(String template) {
        init();
        String templatePath = resolveResource("templates/" + template + ".rythm", "ticket template");
        Map<String, Object> context = new HashMap<>();
        context.put("departure", "Naples Beverello");
        context.put("arrival", "Port of Capri");
        context.put("reservationNumber", "23512342352323");
        context.put("departureTime", "12:45");
        context.put("arrivalTime", "16:25");

        File file = generateDocument(context, templatePath, ENGLISH, template);
        String resourcesPath = Paths.get("src/main/resources/instructions/").toString();
        // Construct the path where you want to save the file within the resources folder
        File targetFile = new File(resourcesPath + File.separator + file.getName());
        // Make sure the directories exist
        targetFile.getParentFile().mkdirs();
        // Copy the file content to the target location (Java 7+ should use Files.copy())
        try (OutputStream out = new FileOutputStream(targetFile)) {
            Files.copy(file.toPath(), out);
            out.flush();
        }
    }

    public void init() {
        try {
            Path temporaryDirForFont = Files.createTempDirectory("ticketWriter");
            temporaryDirForFont.toFile().deleteOnExit();
            arialUnicodeMSFontPath = temporaryDirForFont.resolve("ArialUnicodeMS.ttf");
            Files.copy(ARIAL_UNICODE_MS_FONT.getInputStream(), arialUnicodeMSFontPath);
        } catch (IOException e) {
            throw new IllegalStateException("Error while initializing DefaultTicketGenerator", e);
        }
    }

    public File generateDocument(
        Map<String, Object> context, String templatePath, Locale locale, String filePrefix)
        throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        generate(context, templatePath, locale, out);
        return writeToFile(out.toByteArray(), filePrefix);
    }

    @SneakyThrows
    public void generate(
        Map<String, Object> context, String templatePath, Locale locale, OutputStream out) {
        String html = renderAsString(context, templatePath, locale);
        convertToPdf(html, out);
    }

    public void convertToPdf(String htmlContent, OutputStream outputStream)
        throws com.itextpdf.text.DocumentException, IOException {
        ITextRenderer renderer = new ITextRenderer();
        SharedContext sharedContext = renderer.getSharedContext();
        sharedContext.setMedia("pdf");
        sharedContext.setUserAgentCallback(
            new UriResolver(renderer.getOutputDevice(), sharedContext));

        ITextFontResolver fontResolver = renderer.getFontResolver();
        fontResolver.addFont(
            arialUnicodeMSFontPath.toAbsolutePath().toString(),
            BaseFont.IDENTITY_H,
            BaseFont.NOT_EMBEDDED);

        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        renderer.createPDF(outputStream);
    }

    public String renderAsString(Map<String, Object> context, String templatePath, Locale locale) {
        Map<String, Object> engineConfig = new HashMap<>(context);

        engineConfig.put(ENGINE_ID.getKey(), RYTHM_ENGINE_ID);
        engineConfig.put(I18N_LOCALE.getKey(), locale);
        engineConfig.put(ENGINE_MODE.getKey(), "prod");
        engineConfig.put(RESOURCE_LOADER_IMPLS.getKey(), new ClasspathResourceLoader(null, "/"));
        engineConfig.put(FEATURE_SMART_ESCAPE_ENABLED.getKey(), false);

        RythmEngine engine = new RythmEngine(engineConfig);


        return engine.renderIfTemplateExists(templatePath, context);
    }

    private File writeToFile(byte[] bytes, String filePrefix) throws IOException {

        File ticketFile = File.createTempFile(filePrefix, ".pdf");
        @Cleanup FileOutputStream out = new FileOutputStream(ticketFile);
        out.write(bytes);

        return ticketFile;
    }

    private String resolveResource(String path, String resourceName) {
        return Optional.ofNullable(path)
            .filter(p -> new ClassPathResource(p).isReadable())
            .orElseThrow(() -> new IllegalArgumentException("Missing or unreadable " + resourceName));
    }
}
