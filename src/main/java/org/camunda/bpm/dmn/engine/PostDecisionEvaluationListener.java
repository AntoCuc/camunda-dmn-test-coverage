/*
 * Software subject to the MIT License.
 * For more information please see the LICENSE.md file.
 *
 * Copyright (c) 2018 Antonino Cucchiara.
 */

package org.camunda.bpm.dmn.engine;

import org.camunda.bpm.dmn.engine.delegate.DmnDecisionEvaluationEvent;
import org.camunda.bpm.dmn.engine.delegate.DmnDecisionEvaluationListener;
import org.camunda.bpm.dmn.engine.delegate.DmnDecisionTableEvaluationEvent;
import org.camunda.bpm.dmn.engine.delegate.DmnEvaluatedDecisionRule;
import org.camunda.bpm.dmn.engine.impl.DmnDecisionTableImpl;
import org.camunda.bpm.dmn.engine.impl.DmnDecisionTableRuleImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Post decision listener in charge of calculating the test-level coverage
 * of decision rules.
 */
public final class PostDecisionEvaluationListener
        implements DmnDecisionEvaluationListener {

    /**
     * The decision table.
     */
    private final Map<String, Boolean> decisionTable = new HashMap<>();

    @Override
    public void notify(final DmnDecisionEvaluationEvent event) {
        final DmnDecisionTableEvaluationEvent decisionResult =
                (DmnDecisionTableEvaluationEvent) event.getDecisionResult();
        final DmnDecision dmnDecision = decisionResult.getDecision();
        final DmnDecisionTableImpl decisionLogic =
                (DmnDecisionTableImpl) dmnDecision.getDecisionLogic();

        final List<DmnDecisionTableRuleImpl> rules = decisionLogic.getRules();

        for (DmnDecisionTableRuleImpl rule : rules) {
            final String ruleId = rule.getId();
            this.decisionTable.put(ruleId, false);
        }

        final List<DmnEvaluatedDecisionRule> matchingRules =
                decisionResult.getMatchingRules();

        for (DmnEvaluatedDecisionRule matchingRule : matchingRules) {
            final String evaluatedDecisionRuleId = matchingRule.getId();
            this.decisionTable.put(evaluatedDecisionRuleId, true);
        }
    }

    /**
     * @return the decision table
     */
    Map<String, Boolean> getDecisionTable() {
        return this.decisionTable;
    }

    /**
     * @return the decision coverage
     */
    double getCoverage() {
        final double allRules = this.decisionTable.size();

        final double coveredRules = this.decisionTable
                .entrySet()
                .stream()
                .filter(Map.Entry::getValue)
                .distinct()
                .count();

        return coveredRules / allRules;
    }

    /**
     * @return list of matched rule names
     */
    List<String> getMatchedRules() {
        return this.decisionTable
                .entrySet()
                .stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
