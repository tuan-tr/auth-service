package com.tth.auth.configuration.hibernate;

import java.io.Serializable;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class NanoidGenerator implements IdentifierGenerator {

  public static final String NAME = "com.tth.auth.configuration.hibernate.NanoidGenerator";

  @Override
  public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
    return NanoIdUtils.randomNanoId();
  }
  
}
