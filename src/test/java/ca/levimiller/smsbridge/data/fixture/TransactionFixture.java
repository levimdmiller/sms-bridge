package ca.levimiller.smsbridge.data.fixture;

import ca.levimiller.smsbridge.data.model.Transaction;
import ca.levimiller.smsbridge.data.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionFixture implements Fixture<Transaction> {

  private final RandomUtil randomUtil;

  @Autowired
  public TransactionFixture(RandomUtil randomUtil) {
    this.randomUtil = randomUtil;
  }

  @Override
  public Transaction create() {
    return Transaction.builder()
        .transactionId(randomUtil.getString(255))
        .completed(randomUtil.getBoolean())
        .build();
  }
}
