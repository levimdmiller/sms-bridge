package ca.levimiller.smsbridge.data.db;

import ca.levimiller.smsbridge.data.model.VoiceCall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface VoiceCallRepository extends JpaRepository<VoiceCall, Long>,
    QuerydslPredicateExecutor<VoiceCall> {
}
