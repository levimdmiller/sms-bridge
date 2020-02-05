package ca.levimiller.smsbridge.util.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NoopIdentifiableService implements InvocationHandler {

  /**
   * Creates a noop service that implements the given interface and logs an
   * INFO level log whenever a method is called.
   * @param interfaceClass - interface to implement.
   * @param <T> - interface type
   * @return - noop proxy
   */
  public static <T> T getProxy(Class<T> interfaceClass) {
    return (T) Proxy.newProxyInstance(NoopIdentifiableService.class.getClassLoader(),
        new Class[]{ interfaceClass },
        new NoopIdentifiableService());
  }

  private NoopIdentifiableService() {
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    if("getIdentifier".equals(method.getName()) && args.length == 0) {
      return IdentifiableService.ANY_ID;
    }
    log.info("Method ignored: {}, {}", method.getName(), Arrays.toString(args));
    return null;
  }
}
