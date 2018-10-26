package gov.va.api.health.mranderson.cdw.impl;

import gov.va.api.health.mranderson.cdw.Query;
import gov.va.api.health.mranderson.cdw.ResourceRepository;
import gov.va.api.health.mranderson.cdw.Resources.SearchFailed;
import gov.va.api.health.mranderson.util.Checks;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * The `prc_Entity_Return` stored procedure implementation of the resource repository. No error
 * checking is performed.
 */
@Component
public class SqlResourceRepository implements ResourceRepository {

  private final JdbcTemplate jdbc;

  private final String schema;

  @Autowired
  public SqlResourceRepository(JdbcTemplate jdbc, @Value("${cdw.schema:App}") String schema) {
    this.jdbc = jdbc;
    this.schema = Checks.argumentMatches(schema, "[A-Za-z0-9_]+");
  }

  @Override
  public String execute(Query query) {
    try (Connection connection = Checks.notNull(jdbc.getDataSource()).getConnection()) {
      try (CallableStatement cs = connection.prepareCall(storedProcedure())) {
        cs.closeOnCompletion();
        cs.setObject(Index.FHIR_VERSION, 1, Types.TINYINT);
        cs.setObject(Index.RETURN_TYPE, 3, Types.TINYINT);
        cs.setObject(Index.FORMAT, 1, Types.TINYINT);
        cs.setObject(Index.RECORDS_PER_PAGE, query.count(), Types.INTEGER);
        cs.setObject(Index.PAGE, query.page(), Types.INTEGER);
        cs.setObject(Index.FHIR_STRING, query.toQueryString(), Types.VARCHAR);
        cs.registerOutParameter(Index.RESPONSE_XML, Types.CLOB);
        cs.executeUpdate();
        Clob clob = (Clob) cs.getObject(Index.RESPONSE_XML);
        return clob.getSubString(1, (int) clob.length());
      }
    } catch (SQLException e) {
      throw new SearchFailed(query, e);
    }
  }

  private String storedProcedure() {
    /*
     * String concatenation is safe because the schema value is verified to contain only letters,
     * numbers, and underscores and does not allow any SQL sensitive characters that could enable
     * SQL injection.
     */
    return "{call [" + schema + "].[prc_Entity_Return](?,?,?,?,?,?,?)}";
  }

  static final class Index {
    static final int FHIR_VERSION = 1;
    static final int RETURN_TYPE = 2;
    static final int FORMAT = 3;
    static final int RECORDS_PER_PAGE = 4;
    static final int PAGE = 5;
    static final int FHIR_STRING = 6;
    static final int RESPONSE_XML = 7;
  }
}
