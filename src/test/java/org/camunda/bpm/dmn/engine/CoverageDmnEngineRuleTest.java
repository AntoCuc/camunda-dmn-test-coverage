/*
 * Software subject to the MIT License.
 * For more information please see the LICENSE.md file.
 *
 * Copyright (c) 2018 Antonino Cucchiara.
 */

package org.camunda.bpm.dmn.engine;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CoverageDmnEngineRuleTest {

    private final CoverageDmnEngineRule rule = new CoverageDmnEngineRule();

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
    public void getMatchedRules() {
        final PostDecisionEvaluationListener listener = mock(PostDecisionEvaluationListener.class);
        rule.setListener(listener);
        rule.getMatchedRules();
        verify(listener, times(1)).getMatchedRules();
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