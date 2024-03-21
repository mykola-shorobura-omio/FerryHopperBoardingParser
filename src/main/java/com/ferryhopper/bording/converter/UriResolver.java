package com.ferryhopper.bording.converter;

import com.google.common.annotations.VisibleForTesting;
import java.io.IOException;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.UrlResource;
import org.xhtmlrenderer.pdf.ITextOutputDevice;
import org.xhtmlrenderer.pdf.ITextUserAgent;

@Slf4j
class UriResolver extends ITextUserAgent {

  UriResolver(ITextOutputDevice outputDevice, int dotsPerPixel) {
    super(outputDevice, dotsPerPixel);
  }

  @Override
  public String resolveURI(String uri) {
    return uri;
  }

  @Override
  protected InputStream resolveAndOpenStream(String uri) {
    try {
      if (uri.startsWith("http:") || uri.startsWith("https:")) {
        return getUrlResource(uri);
      }
      return new ClassPathResource(uri).getInputStream();
    } catch (Exception ex) {
      log.error("Error resolving uri {}", uri, ex);
    }

    return null;
  }

  @VisibleForTesting
  InputStream getUrlResource(String uri) throws IOException {
    return new UrlResource(uri).getInputStream();
  }
}
