package gov.va.api.health.autoconfig.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.function.Supplier;
import javax.net.ssl.SSLContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

/**
 * This factory provides rest template that are configured for SSL communication per {@link
 * SslClientProperties}. Additionally this attaches an interceptor that will provide logging on
 * failed requests.
 */
@Configuration
@Slf4j
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class SecureRestTemplateConfig {
  private final SslClientProperties config;

  /**
   * Creates a RestTemplate that is configured to SSL. It will also have a logging interceptor that
   * will record information on a service call failure.
   */
  @Bean
  public RestTemplate restTemplate(@Autowired RestTemplateBuilder restTemplateBuilder) {

    HttpClient client =
        config.isEnableClient()
            ? HttpClients.custom().setSSLContext(sslContext()).build()
            : HttpClients.custom().build();

    return restTemplateBuilder
        .requestFactory(bufferingRequestFactory(client))
        .additionalInterceptors(loggingInterceptor())
        .build();
  }

  private String fileOrClasspath(String path) {
    if (StringUtils.startsWith(path, "file:") || StringUtils.startsWith(path, "classpath:")) {
      return path;
    }
    throw new IllegalArgumentException("Expected file or classpath resources. Got " + path);
  }

  private KeyStore loadKeyStore(String path, char[] password) {
    try {
      KeyStore keyStore = KeyStore.getInstance("JKS");
      try (InputStream keystoreStream = ResourceUtils.getURL(fileOrClasspath(path)).openStream()) {
        keyStore.load(keystoreStream, password);
      }
      return keyStore;
    } catch (IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException e) {
      throw new FailedToConfigureSsl("Cannot load: " + path, e);
    }
  }

  private SSLContext sslContext() {
    try {
      SSLContextBuilder builder =
          SSLContextBuilder.create()
              .loadKeyMaterial(
                  loadKeyStore(config.getKeyStore(), config.keyStorePassword()),
                  config.clientKeyPassword());
      if (config.isUseTrustStore()) {
        builder.loadTrustMaterial(
            loadKeyStore(config.getTrustStore(), config.trustStorePassword()),
            new TrustAllStrategy());
      }
      return builder.build();
    } catch (KeyStoreException
        | NoSuchAlgorithmException
        | UnrecoverableKeyException
        | KeyManagementException e) {
      throw new FailedToConfigureSsl(e);
    }
  }

  private ClientHttpRequestInterceptor loggingInterceptor() {
    return (request, body, execution) -> {
      log.info("{} {}", request.getMethod(), request.getURI());
      ClientHttpResponse response = execution.execute(request, body);
      if (response.getStatusCode().isError()) {
        log.error("--- REQUEST FAILED ---------------------------------");
        log.error("{} {}", request.getMethod(), request.getURI());
        log.error("Headers: {}", request.getHeaders());
        log.error("Request Body:\n{}", new String(body, StandardCharsets.UTF_8));
        log.error("--- RESPONSE ---------------------------------------");
        log.error(
            "Response: {} ({})",
            response.getStatusCode(),
            response.getStatusCode().getReasonPhrase());
        log.error("Headers: {}", response.getHeaders());
        log.error(
            "Response Body:\n{}",
            StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8));
        log.error("----------------------------------------------------");
      }
      return response;
    };
  }

  private Supplier<ClientHttpRequestFactory> bufferingRequestFactory(HttpClient client) {
    return () ->
        new BufferingClientHttpRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
  }

  public static class FailedToConfigureSsl extends RuntimeException {
    FailedToConfigureSsl(Exception cause) {
      super(cause);
    }

    FailedToConfigureSsl(String message, Exception cause) {
      super(message, cause);
    }
  }
}
