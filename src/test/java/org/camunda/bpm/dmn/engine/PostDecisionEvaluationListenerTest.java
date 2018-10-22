package org.camunda.bpm.dmn.engine;

import org.camunda.bpm.dmn.engine.delegate.DmnDecisionEvaluationEvent;
import org.camunda.bpm.dmn.engine.delegate.DmnDecisionTableEvaluationEvent;
import org.camunda.bpm.dmn.engine.delegate.DmnEvaluatedDecisionRule;
import org.camunda.bpm.dmn.engine.impl.DmnDecisionTableImpl;
import org.camunda.bpm.dmn.engine.impl.DmnDecisionTableRuleImpl;
import org.junit.Test;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class PostDecisionEvaluationListenerTest {

    private final PostDecisionEvaluationListener listener = new PostDecisionEvaluationListener();

    @Test
    public void notifyTest() {
        final DmnDecisionEvaluationEvent event = mock(DmnDecisionEvaluationEvent.class);
        final DmnDecisionTableEvaluationEvent decisionResult = mock(DmnDecisionTableEvaluationEvent.class);
        final DmnDecision decision = mock(DmnDecision.class);
        final DmnDecisionTableImpl decisionLogic = mock(DmnDecisionTableImpl.class);
        when(event.getDecisionResult()).thenReturn(decisionResult);
        when(decisionResult.getDecision()).thenReturn(decision);
        when(decision.getDecisionLogic()).thenReturn(decisionLogic);
        listener.notify(event);
        verify(decisionLogic, times(1)).getRules();
        verify(decisionResult, times(1)).getMatchingRules();
    }

    @Test
    public void getDecisionTable() {
        Map decisionTable = listener.getDecisionTable();
        assertTrue(decisionTable.isEmpty());
    }

    @Test
    public void getCoverageWithNoData() {
        double coverage = listener.getCoverage();
        assertEquals(Double.NaN, coverage, 0);
    }

    @Test
    public void getCoverageWithNoCoveredRules() {
        final DmnDecisionEvaluationEvent event = mock(DmnDecisionEvaluationEvent.class);
        final DmnDecisionTableEvaluationEvent decisionResult = mock(DmnDecisionTableEvaluationEvent.class);
        final DmnDecision decision = mock(DmnDecision.class);
        final DmnDecisionTableImpl decisionLogic = mock(DmnDecisionTableImpl.class);
        final DmnDecisionTableRuleImpl rule = mock(DmnDecisionTableRuleImpl.class);
        when(event.getDecisionResult()).thenReturn(decisionResult);
        when(decisionResult.getDecision()).thenReturn(decision);
        when(decision.getDecisionLogic()).thenReturn(decisionLogic);
        when(rule.getId()).thenReturn("Test Rule Id");
        when(decisionLogic.getRules()).thenReturn(Collections.singletonList(rule));
        listener.notify(event);
        double coverage = listener.getCoverage();
        assertEquals(0, coverage, 0);
    }

    @Test
    public void getCoverageWithPartiallyCoveredRules() {
        final DmnDecisionEvaluationEvent event = mock(DmnDecisionEvaluationEvent.class);
        final DmnDecisionTableEvaluationEvent decisionResult = mock(DmnDecisionTableEvaluationEvent.class);
        final DmnDecision decision = mock(DmnDecision.class);
        final DmnDecisionTableImpl decisionLogic = mock(DmnDecisionTableImpl.class);
        final DmnDecisionTableRuleImpl rule = mock(DmnDecisionTableRuleImpl.class);
        final DmnDecisionTableRuleImpl uncoveredRule = mock(DmnDecisionTableRuleImpl.class);
        final DmnEvaluatedDecisionRule evaluatedRule = mock(DmnEvaluatedDecisionRule.class);
        when(event.getDecisionResult()).thenReturn(decisionResult);
        when(decisionResult.getDecision()).thenReturn(decision);
        when(decisionResult.getMatchingRules()).thenReturn(Collections.singletonList(evaluatedRule));
        final String testRuleId = "Test Rule Id";
        when(evaluatedRule.getId()).thenReturn(testRuleId);
        when(decision.getDecisionLogic()).thenReturn(decisionLogic);
        when(rule.getId()).thenReturn(testRuleId);
        when(decisionLogic.getRules()).thenReturn(Arrays.asList(rule, uncoveredRule));
        listener.notify(event);
        double coverage = listener.getCoverage();
        assertEquals(0.5, coverage, 0);
    }

    @Test
    public void getCoverageWithAllCoveredRules() {
        final DmnDecisionEvaluationEvent event = mock(DmnDecisionEvaluationEvent.class);
        final DmnDecisionTableEvaluationEvent decisionResult = mock(DmnDecisionTableEvaluationEvent.class);
        final DmnDecision decision = mock(DmnDecision.class);
        final DmnDecisionTableImpl decisionLogic = mock(DmnDecisionTableImpl.class);
        final DmnDecisionTableRuleImpl rule = mock(DmnDecisionTableRuleImpl.class);
        final DmnEvaluatedDecisionRule evaluatedRule = mock(DmnEvaluatedDecisionRule.class);
        when(event.getDecisionResult()).thenReturn(decisionResult);
        when(decisionResult.getDecision()).thenReturn(decision);
        when(decisionResult.getMatchingRules()).thenReturn(Collections.singletonList(evaluatedRule));
        final String testRuleId = "Test Rule Id";
        when(evaluatedRule.getId()).thenReturn(testRuleId);
        when(decision.getDecisionLogic()).thenReturn(decisionLogic);
        when(rule.getId()).thenReturn(testRuleId);
        when(decisionLogic.getRules()).thenReturn(Collections.singletonList(rule));
        listener.notify(event);
        double coverage = listener.getCoverage();
        assertEquals(1.0, coverage, 0);
    }

    @Test
    public void getCoveredRulesNoData() {
        List<String> coveredRules = listener.getMatchedRules();
        assertEquals(0, coveredRules.size());
    }

    @Test
    public void getCoveredRulesData() throws NoSuchFieldException {
        final PostDecisionEvaluationListener mockListener = new PostDecisionEvaluationListener();
        final Field decisionTableField =
                mockListener.getClass().getDeclaredField("decisionTable");
        Map<String, Boolean> decisionTable = new HashMap<>();
        decisionTable.put("matched", true);
        decisionTable.put("unmatched", false);
        FieldSetter.setField(mockListener, decisionTableField, decisionTable);
        List<String> coveredRules = mockListener.getMatchedRules();
        assertEquals(1, coveredRules.size());
        assertTrue(coveredRules.contains("matched"));
    }
}