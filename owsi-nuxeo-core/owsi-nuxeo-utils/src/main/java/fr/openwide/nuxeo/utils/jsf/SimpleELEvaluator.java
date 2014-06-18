package fr.openwide.nuxeo.utils.jsf;

import org.jboss.el.ExpressionFactoryImpl;
import org.nuxeo.ecm.platform.el.ExpressionContext;
import org.nuxeo.ecm.platform.el.ExpressionEvaluator;

public class SimpleELEvaluator {

	private ExpressionContext expressionContext;
	private ExpressionEvaluator expressionEvaluator;

	public SimpleELEvaluator() {
		expressionContext = new ExpressionContext();
		expressionEvaluator = new ExpressionEvaluator(new ExpressionFactoryImpl());
	}

	public void set(String key, Object value) {
		expressionEvaluator.bindValue(expressionContext, key, value);
	}

	public String evaluate(String elString) {
		if (elString != null) {
			return expressionEvaluator.evaluateExpression(expressionContext,
				elString, String.class);
		}
		else {
			return null;
		}
	}

}
