package gov.va.api.health.argonaut.service.mranderson.client;

import gov.va.api.health.argonaut.service.config.WithJaxb;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class RestMrAndersonClient implements MrAndersonClient {
  @Autowired @WithJaxb private RestTemplate restTemplate;

  @Value("${mranderson.url}")
  private String baseUrl;

  private HttpEntity<Void> requestEntity() {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_XML));
    return new HttpEntity<>(headers);
  }

  @Override
  public <T> T search(Query<T> query) {
    String url = urlOf(query);
    ResponseEntity<T> entity =
        restTemplate.exchange(
            url, HttpMethod.GET, requestEntity(), ParameterizedTypeReference.forType(query.type()));
    if (entity.getStatusCode() == HttpStatus.NOT_FOUND) {
      throw new NotFound(query);
    }
    if (entity.getStatusCode() == HttpStatus.BAD_REQUEST) {
      throw new BadRequest(query);
    }
    return entity.getBody();
  }

  private String urlOf(Query<?> query) {
    return baseUrl + "/api/v1/resources" + query.toQueryString();
  }
}
