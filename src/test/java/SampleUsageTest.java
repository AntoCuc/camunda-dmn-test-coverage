/*
 * Software subject to the MIT License.
 * For more information please see the LICENSE.md file.
 *
 * Copyright (c) 2018 Antonino Cucchiara.
 */

import org.camunda.bpm.dmn.engine.CoverageDmnEngineRule;
import org.camunda.bpm.dmn.engine.DmnDecision;
import org.camunda.bpm.dmn.engine.DmnDecisionResult;
import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.junit.Rule;
import org.junit.Test;

import java.io.InputStream;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

public class SampleUsageTest {

    @Rule
    public CoverageDmnEngineRule dmnEngineRule = new CoverageDmnEngineRule();

    @Test
    public void testBronze() {
        DmnEngine dmnEngine = dmnEngineRule.getDmnEngine();
        InputStream inputStream = SampleUsageTest.class.getResourceAsStream("Example.dmn");

        VariableMap variables = Variables.createVariables();
        variables.put("status", "bronze");
        variables.put("sum", 0);

        DmnDecision decision = dmnEngine.parseDecision("orderDecision", inputStream);
        DmnDecisionResult result = dmnEngine.evaluateDecision(decision, variables);

        assertEquals(1, result.getResultList().size());
        assertEquals("notok", result.getSingleResult().get("result"));
        assertEquals("work on your status first, as bronze you're not going to get anything", result.getSingleResult().get("reason"));

        assertEquals(0.25, dmnEngineRule.getCoverage(), 0.0);
        assertEquals(singletonList("rule1"), dmnEngineRule.getMatchedRules());
    }

    @Test
    public void testSilverPositive() {
        DmnEngine dmnEngine = dmnEngineRule.getDmnEngine();
        InputStream inputStream = SampleUsageTest.class.getResourceAsStream("Example.dmn");

        VariableMap variables = Variables.createVariables();
        variables.put("status", "silver");
        variables.put("sum", 999.99);

        DmnDecision decision = dmnEngine.parseDecision("orderDecision", inputStream);
        DmnDecisionResult result = dmnEngine.evaluateDecision(decision, variables);

        assertEquals(1, result.getResultList().size());
        assertEquals("ok", result.getSingleResult().get("result"));
        assertEquals("you little fish will get what you want", result.getSingleResult().get("reason"));

        assertEquals(0.25, dmnEngineRule.getCoverage(), 0.0);
        assertEquals(singletonList("rule2"), dmnEngineRule.getMatchedRules());
    }

    @Test
    public void testSilverNegative() {
        DmnEngine dmnEngine = dmnEngineRule.getDmnEngine();
        InputStream inputStream = SampleUsageTest.class.getResourceAsStream("Example.dmn");

        VariableMap variables = Variables.createVariables();
        variables.put("status", "silver");
        variables.put("sum", 1000);

        DmnDecision decision = dmnEngine.parseDecision("orderDecision", inputStream);
        DmnDecisionResult result = dmnEngine.evaluateDecision(decision, variables);

        assertEquals(1, result.getResultList().size());
        assertEquals("notok", result.getSingleResult().get("result"));
        assertEquals("you took too much man, you took too much!", result.getSingleResult().get("reason"));

        assertEquals(0.25, dmnEngineRule.getCoverage(), 0.0);
        assertEquals(singletonList("rule3"), dmnEngineRule.getMatchedRules());
    }

    @Test
    public void testGold() {
        DmnEngine dmnEngine = dmnEngineRule.getDmnEngine();
        InputStream inputStream = SampleUsageTest.class.getResourceAsStream("Example.dmn");

        VariableMap variables = Variables.createVariables();
        variables.put("status", "gold");
        variables.put("sum", 1000000);

        DmnDecision decision = dmnEngine.parseDecision("orderDecision", inputStream);
        DmnDecisionResult result = dmnEngine.evaluateDecision(decision, variables);

        assertEquals(1, result.getResultList().size());
        assertEquals("ok", result.getSingleResult().get("result"));
        assertEquals("you get anything you want", result.getSingleResult().get("reason"));

        assertEquals(0.25, dmnEngineRule.getCoverage(), 0.0);
        assertEquals(singletonList("rule4"), dmnEngineRule.getMatchedRules());
    }
}
