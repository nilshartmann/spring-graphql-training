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
public class PostgreSqlStringArrayType implements UserType {

  @Override
  public int[] sqlTypes() {
    return new int[]{Types.ARRAY};
  }

  @Override
  public Class returnedClass() {
    return String[].class;
  }

  @Override
  public boolean equals(Object x, Object y) throws HibernateException {
    if (x instanceof String[] && y instanceof String[]) {
      return Arrays.deepEquals((String[]) x, (String[]) y);
    } else {
      return false;
    }
  }

  @Override
  public int hashCode(Object x) throws HibernateException {
    return Arrays.hashCode((String[]) x);
  }

  @Override
  public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)
    throws HibernateException, SQLException {
    Array array = rs.getArray(names[0]);
    return array != null ? array.getArray() : null;
  }

  @Override
  public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
    throws HibernateException, SQLException {
    if (value != null && st != null) {
      Array array = session.connection().createArrayOf("text", (String[]) value);
      st.setArray(index, array);
    } else {
      st.setNull(index, sqlTypes()[0]);
    }
  }

  @Override
  public Object deepCopy(Object value) throws HibernateException {
    String[] a = (String[]) value;
    return Arrays.copyOf(a, a.length);
  }

  @Override
  public boolean isMutable() {
    return false;
  }

  @Override
  public Serializable disassemble(Object value) throws HibernateException {
    return (Serializable) value;
  }

  @Override
  public Object assemble(Serializable cached, Object owner) throws HibernateException {
    return cached;
  }

  @Override
  public Object replace(Object original, Object target, Object owner) throws HibernateException {
    return original;
  }
}