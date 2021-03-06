/*
 * Software subject to the MIT License.
 * For more information please see the LICENSE.md file.
 *
 * Copyright (c) 2018 Antonino Cucchiara.
 */

package org.camunda.bpm.dmn.engine;

import org.camunda.bpm.dmn.engine.test.DmnEngineRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;


/**
 * Dmn Engine JUnit TestWatcher Rule extended with decision coverage.
 */
public final class CoverageDmnEngineRule extends DmnEngineRule {

    /**
     * Bridged logger.
     */
    private static final Logger LOGGER =
            LoggerFactory.getLogger(CoverageDmnEngineRule.class);

    /**
     * Cross-platform line separator.
     */
    private static final String LINE_SEPARATOR = System.lineSeparator();

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
        errorLog.append(LINE_SEPARATOR + "Execution summary");
        errorLog.append(LINE_SEPARATOR + column("Rule") + column("Executed"));

        final Map<String, Boolean> decisionTable =
                this.listener.getDecisionTable();
        for (Map.Entry<String, Boolean> entry : decisionTable.entrySet()) {
            final String formattedKey = column(entry.getKey());
            final String formattedValue = column(entry.getValue());
            errorLog.append(LINE_SEPARATOR + formattedKey + formattedValue);
        }

        LOGGER.error(errorLog.toString());
        super.failed(e, description);
    }

    /**
     * @return delegates the retrieval of decision rules coverage.
     */
    public double getCoverage() {
        return this.listener.getCoverage();
    }

    /**
     * @return delegates the retrieval of decision rules.
     */
    public List<String> getMatchedRules() {
        return this.listener.getMatchedRules();
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
