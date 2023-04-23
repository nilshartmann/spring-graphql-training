package nh.publy.backend.util;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.*;
import java.util.Arrays;

/**
 * Maps Arrays from/to Postgres.
 *
 * <p>Source: <a href="https://thorben-janssen.com/mapping-arrays-with-hibernate/">Taken from: "Mapping Arrays with Hibernate" by Thorben Janssen</a></p>
 *
 * @see <a href="https://thorben-janssen.com/mapping-arrays-with-hibernate/">Taken from: "Mapping Arrays with Hibernate" by Thorben Janssen</a>
 */
public class PostgreSqlStringArrayType implements UserType<String[]> {

  @Override
  public int getSqlType() {
    return Types.ARRAY;
  }

  @Override
  public Class<String[]> returnedClass() {
    return String[].class;
  }

  @Override
  public boolean equals(String[] x, String[] y) throws HibernateException {
    if (x instanceof String[] && y instanceof String[]) {
      return Arrays.deepEquals((String[]) x, (String[]) y);
    } else {
      return false;
    }
  }

  @Override
  public int hashCode(String[] x) throws HibernateException {
    return Arrays.hashCode((String[]) x);
  }

  @Override
  public String[] nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner) throws SQLException {
    var array = rs.getArray(position);
    return array != null ? (String[])array.getArray() : null;
  }

  @Override
  public void nullSafeSet(PreparedStatement st, String[] value, int index, SharedSessionContractImplementor session)
    throws HibernateException, SQLException {
    if (value != null && st != null) {
      Array array = st.getConnection().createArrayOf("text", (String[]) value);
      st.setArray(index, array);
    } else {
      st.setNull(index, Types.ARRAY);
    }
  }

  @Override
  public String[] deepCopy(String[] value) throws HibernateException {
    String[] a = (String[]) value;
    return Arrays.copyOf(a, a.length);
  }

  @Override
  public boolean isMutable() {
    return false;
  }

  @Override
  public Serializable disassemble(String[] value) throws HibernateException {
    return (Serializable) value;
  }

  @Override
  public String[] assemble(Serializable cached, Object owner) throws HibernateException {
    return (String[])cached;
  }

  @Override
  public String[] replace(String[] original, String[] target, Object owner) throws HibernateException {
    return original;
  }
}