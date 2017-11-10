package io.projectreactor.demos.in.action.testing.support;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author Simon Baslé
 */
public class Trigger1 implements Condition {

	@Override
	public boolean matches(ConditionContext context,
			AnnotatedTypeMetadata metadata) {
		return (System.currentTimeMillis() / 1_000) % 2 == 0;
	}

}
