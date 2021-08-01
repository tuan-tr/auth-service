package com.tth.auth.configuration.hibernate;

import org.hibernate.dialect.PostgreSQL10Dialect;
import org.hibernate.type.StandardBasicTypes;

public class CustomPostgreSQLDialect extends PostgreSQL10Dialect {

  public CustomPostgreSQLDialect() {
    super();
    registerFunction("bitwise_and", new BitwiseAndSQLFunction("bitwise_and", StandardBasicTypes.INTEGER));
  }

}
