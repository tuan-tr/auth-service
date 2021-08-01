package com.tth.auth;

import com.tth.auth.context.InitializationListener;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@MockBean({InitializationListener.class}) // for unprocessing when running test
class AuthApplicationTests {

  @Test
  void contextLoads() {
  }

}
