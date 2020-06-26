package com.amlo.shopping.util;

import org.junit.Assert;
import org.junit.Test;

public class TestAuthenticatorUtil {
  @Test
  public void testGenerateHash() {
    Assert.assertEquals("e80b5017098950fc58aad83c8c14978e", AuthenticatorUtil.generateHash("abcdef"));
  }
}
