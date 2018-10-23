/*
 * Software subject to the MIT License.
 * For more information please see the LICENSE.md file.
 *
 * Copyright (c) 2018 Antonino Cucchiara.
 */

package org.junit;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

/**
 * A set of assertion methods useful for writing coverage tests.
 */
public final class AssertCoverage {

    /**
     * Utility class.
     */
    private AssertCoverage() {
    }

    /**
     * Coverage assertion failed.
     */
    public static final String COVERAGE_ASSERTION_FAILED =
            "Coverage assertion failed";
    /**
     * Single rule failure message.
     */
    public static final String RULE_MATCHING_FAILED = "Rule matching failed";

    /**
     * Rules failure message.
     */
    public static final String RULES_MATCHING_FAILED = "Rules matching failed";

    /**
     * Asserts an exact coverage has been reached.
     * @param expectedCoverage of the rules
     * @param actualCoverage of the rules
     */
    public static void assertExactCoverage(
            final double expectedCoverage,
            final double actualCoverage) {
        assertEquals(
                COVERAGE_ASSERTION_FAILED,
                expectedCoverage,
                actualCoverage,
                0.0
        );
    }

    /**
     * Asserts a rules has been matched.
     * @param rule to be tested
     * @param actualRulesMatched by the decision
     */
    public static void assertRuleMatched(
            final String rule,
            final List<String> actualRulesMatched
    ) {
        assertEquals(
                RULE_MATCHING_FAILED,
                singletonList(rule),
                actualRulesMatched
        );
    }

    /**
     * Asserts all rules have been matched.
     * @param expectedRulesMatched to be tested
     * @param actualRulesMatched by the decision
     */
    public static void assertRulesMatched(
            final List<String> expectedRulesMatched,
            final List<String> actualRulesMatched
    ) {
        assertEquals(
                RULES_MATCHING_FAILED,
                expectedRulesMatched,
                actualRulesMatched
        );
    }
}
