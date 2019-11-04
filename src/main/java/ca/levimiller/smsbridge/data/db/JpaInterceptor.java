package ca.levimiller.smsbridge.data.db;

import ca.levimiller.smsbridge.data.model.BaseModel;
import java.io.Serializable;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import org.hibernate.EmptyInterceptor;
import org.hibernate.HibernateException;
import org.hibernate.type.Type;
import org.springframework.stereotype.Component;

@Component
public class JpaInterceptor extends EmptyInterceptor {

  @Override
  public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState,
      Object[] previousState, String[] propertyNames, Type[] types) {

    if (entity instanceof BaseModel) {
      List<String> properties = Arrays.asList(propertyNames);
      Instant now = Instant.now();
      currentState[getCreatedIndex(properties)] = now;
      currentState[getModifiedIndex(properties)] = now;
    }
    return true;
  }

  @Override
  public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames,
      Type[] types) {

    if (entity instanceof BaseModel) {
      List<String> properties = Arrays.asList(propertyNames);
      Instant now = Instant.now();
      state[getCreatedIndex(properties)] = now;
      state[getModifiedIndex(properties)] = now;
    }
    return true;
  }

  private int getCreatedIndex(List<String> properties) {
    int idx = properties.indexOf("createdDate");
    if (idx < 0) {
      throw new HibernateException("Cant find created property.");
    }
    return idx;
  }

  private int getModifiedIndex(List<String> properties) {
    int idx = properties.indexOf("modifiedDate");
    if (idx < 0) {
      throw new HibernateException("Cant find modified property.");
    }
    return idx;
  }
}
