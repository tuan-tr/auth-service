package com.tth.auth.configuration.jpa;

import java.io.Serializable;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class NanoidGenerator implements IdentifierGenerator {

  @Override
  public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
    return NanoIdUtils.randomNanoId();
  }
  
}
