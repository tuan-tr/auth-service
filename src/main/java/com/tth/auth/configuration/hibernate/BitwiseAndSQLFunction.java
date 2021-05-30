package com.tth.auth.configuration.hibernate;

import java.util.List;

import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.Type;

public class BitwiseAndSQLFunction extends StandardSQLFunction {

  public BitwiseAndSQLFunction(String name) {
    super(name);
  }

  public BitwiseAndSQLFunction(String name, Type registeredType) {
    super(name, registeredType);
  }

  @Override
  public String render(Type firstArgumentType, List args, SessionFactoryImplementor factory) {
    if (args.size() != 2) {
      throw new IllegalArgumentException("the function bitwise_and requires 2 arguments");
    }

    StringBuffer buffer = new StringBuffer()
        .append(args.get(0).toString())
        .append(" & ")
        .append(args.get(1));

    return buffer.toString();
  }

}
