package gov.va.api.health.mranderson.cdw.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import gov.va.api.health.mranderson.Samples;
import gov.va.api.health.mranderson.cdw.Query;
import gov.va.api.health.mranderson.cdw.Resources.SearchFailed;
import gov.va.api.health.mranderson.cdw.impl.SqlResourceRepository.Index;
import gov.va.api.health.mranderson.util.Parameters;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

public class SqlResourceRepositoryTest {

  @Rule public ExpectedException thrown = ExpectedException.none();
  @Mock DataSource dataSource;
  @Mock Connection connection;
  @Mock CallableStatement cs;
  SqlResourceRepository rr;

  @Before
  @SneakyThrows
  public void _init() {
    MockitoAnnotations.initMocks(this);
    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareCall(Mockito.anyString())).thenReturn(cs);
    JdbcTemplate template = new JdbcTemplate(dataSource);
    rr = new SqlResourceRepository(template, "schema");
  }

  @SneakyThrows
  private void mockResults(String payload) {
    Clob clob = mock(Clob.class);
    when(clob.getSubString(Mockito.anyLong(), Mockito.anyInt())).thenReturn(payload);
    when(cs.getObject(Index.RESPONSE_XML)).thenReturn(clob);
  }

  private Query forAnything() {
    return Query.builder()
        .resource("Whatever")
        .version("1.23")
        .page(1)
        .count(2)
        .parameters(Parameters.builder().add("any", "thing").build())
        .build();
  }

  @Test
  public void storedProcedureResultsAreReturnedWhenGood() {
    mockResults(Samples.create().patient());
    String xml = rr.execute(forAnything());
    assertThat(xml).isEqualTo(Samples.create().patient());
  }

  @Test
  @SneakyThrows
  public void searchFailedIsReturnedWhenJdbcExceptionOccurs() {
    when(cs.getObject(Index.RESPONSE_XML)).thenThrow(new SQLException("mock"));
    thrown.expect(SearchFailed.class);
    rr.execute(forAnything());
  }
}
