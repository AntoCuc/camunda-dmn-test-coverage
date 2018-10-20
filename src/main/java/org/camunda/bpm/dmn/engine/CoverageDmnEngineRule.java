package org.camunda.bpm.dmn.engine;

import org.camunda.bpm.dmn.engine.test.DmnEngineRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


/**
 * Dmn Engine JUnit TestWatcher Rule extended with decision coverage.
 */
public final class CoverageDmnEngineRule extends DmnEngineRule {

    /**
     * Bridged logger.
     */
    private static Logger logger =
            LoggerFactory.getLogger(CoverageDmnEngineRule.class);

    /**
     * Cross-platform line separator.
     */
    private static String lineSeparator = System.lineSeparator();

    /**
     * Decision listener calculating coverage.
     */
    private PostDecisionEvaluationListener listener;

    /**
     * Creates a DMNEngineRule extended to account for decisions coverage.
     */
    public CoverageDmnEngineRule() {
        this.listener = new PostDecisionEvaluationListener();
    }

    @Override
    public Statement apply(
            final Statement base,
            final Description description
    ) {
        super.dmnEngineConfiguration
                .getCustomPostDecisionEvaluationListeners()
                .add(listener);
        return super.apply(base, description);
    }

    @Override
    protected void failed(final Throwable e, final Description description) {

        final StringBuilder errorLog = new StringBuilder();
        errorLog.append(lineSeparator + "Execution summary");
        errorLog.append(lineSeparator + column("Rule") + column("Executed"));

        final Map<String, Boolean> decisionTable =
                this.listener.getDecisionTable();
        for (Map.Entry<String, Boolean> entry : decisionTable.entrySet()) {
            final String formattedKey = column(entry.getKey());
            final String formattedValue = column(entry.getValue());
            errorLog.append(lineSeparator + formattedKey + formattedValue);
        }

        logger.error(errorLog.toString());
        super.failed(e, description);
    }

    /**
     * @return delegates the retrieval of decision rules coverage.
     */
    public double getCoverage() {
        return this.listener.getCoverage();
    }

    /**
     * @return engine configuration
     */
    DmnEngineConfiguration getDmnEngineConfiguration() {
        return this.dmnEngineConfiguration;
    }

    /**
     * @param testListener used for injections.
     */
    void setListener(final PostDecisionEvaluationListener testListener) {
        this.listener = testListener;
    }

    /**
     * @param data to be formatted
     * @return a 10 spaces-padded column of data
     */
    String column(final Object data) {
        String sanitisedData = "";
        if (data != null) {
            sanitisedData = data.toString();
        }
        return String.format("%1$-10s", sanitisedData);
    }
}
