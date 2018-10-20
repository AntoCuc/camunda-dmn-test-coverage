package org.camunda.bpm.dmn.engine;

import org.camunda.bpm.dmn.engine.delegate.DmnDecisionEvaluationEvent;
import org.camunda.bpm.dmn.engine.delegate.DmnDecisionEvaluationListener;
import org.camunda.bpm.dmn.engine.delegate.DmnDecisionTableEvaluationEvent;
import org.camunda.bpm.dmn.engine.delegate.DmnEvaluatedDecisionRule;
import org.camunda.bpm.dmn.engine.impl.DefaultDmnEngineConfiguration;
import org.camunda.bpm.dmn.engine.impl.DmnDecisionTableImpl;
import org.camunda.bpm.dmn.engine.impl.DmnDecisionTableRuleImpl;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.verification.Times;
import org.mockito.verification.VerificationMode;

import javax.xml.bind.Marshaller;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class CoverageDmnEngineRuleTest {

    final CoverageDmnEngineRule rule = new CoverageDmnEngineRule();

    @Test
    public void apply() {
        final Statement statement = mock(Statement.class);
        final Description description = mock(Description.class);
        rule.apply(statement, description);
        final DmnEngineConfiguration config = rule.getDmnEngineConfiguration();
        int size = config.getCustomPostDecisionEvaluationListeners().size();
        assertEquals(1, size);
    }

    @Test
    public void failed() {
        final PostDecisionEvaluationListener listener = mock(PostDecisionEvaluationListener.class);
        final Map<String, Boolean> testDecisionTable = new HashMap<String, Boolean>() {{
            put("rule1", true);
            put("rule2", false);
        }};
        when(listener.getDecisionTable()).thenReturn(testDecisionTable);
        final Throwable throwable = mock(Throwable.class);
        final Description description = mock(Description.class);
        rule.setListener(listener);
        rule.failed(throwable, description);
    }

    @Test
    public void getCoverage() {
        final PostDecisionEvaluationListener listener = mock(PostDecisionEvaluationListener.class);
        rule.setListener(listener);
        rule.getCoverage();
        verify(listener, times(1)).getCoverage();
    }

    @Test
    public void testColumnNullData() {
        final Object data = null;
        assertEquals("          ", rule.column(data));
    }

    @Test
    public void testColumn() {
        final Object data = "a";
        assertEquals("a         ", rule.column(data));
    }
}