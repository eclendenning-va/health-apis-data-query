package gov.va.api.health.mranderson.cdw.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.va.api.health.mranderson.Samples;
import gov.va.api.health.mranderson.cdw.Profile;
import gov.va.api.health.mranderson.cdw.Query;
import gov.va.api.health.mranderson.cdw.Resources.SearchFailed;
import gov.va.api.health.mranderson.cdw.impl.SqlResourceRepository.Index;
import gov.va.api.health.mranderson.util.Parameters;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
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

  @Rule public final ExpectedException thrown = ExpectedException.none();
  @Mock DataSource dataSource;
  @Mock Connection connection;
  @Mock CallableStatement cs;
  private SqlResourceRepository rr;

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
    return forAnything(Profile.ARGONAUT);
  }

  private Query forAnything(Profile profile) {
    return Query.builder()
        .profile(profile)
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

  @Test
  @SneakyThrows
  public void profileArgonautUsesFhirVersion1() {
    mockResults(Samples.create().patient());
    String xml = rr.execute(forAnything(Profile.ARGONAUT));
    assertThat(xml).isEqualTo(Samples.create().patient());
    verify(cs).setObject(Index.FHIR_VERSION, 1, Types.TINYINT);
  }

  @Test
  @SneakyThrows
  public void profileDstu2UsesFhirVersion2() {
    mockResults(Samples.create().patient());
    String xml = rr.execute(forAnything(Profile.DSTU2));
    assertThat(xml).isEqualTo(Samples.create().patient());
    verify(cs).setObject(Index.FHIR_VERSION, 2, Types.TINYINT);
  }

  @Test
  @SneakyThrows
  public void profileStu3UsesFhirVersion3() {
    mockResults(Samples.create().patient());
    String xml = rr.execute(forAnything(Profile.STU3));
    assertThat(xml).isEqualTo(Samples.create().patient());
    verify(cs).setObject(Index.FHIR_VERSION, 3, Types.TINYINT);
  }
}
