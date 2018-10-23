/*
 * Software subject to the MIT License.
 * For more information please see the LICENSE.md file.
 *
 * Copyright (c) 2018 Antonino Cucchiara.
 */

package org.junit;

import org.junit.rules.ExpectedException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertTrue;
import static org.junit.AssertCoverage.*;

public class AssertCoverageTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void assertExactCoverageTest() {
        assertExactCoverage(0.5, 0.5);
    }

    @Test
    public void assertExactCoverageNegTest() {
        expectedException.expect(java.lang.AssertionError.class);
        assertExactCoverage(0.4, 0.5);
    }

    @Test
    public void assertRuleMatchedTest() {
        assertRuleMatched("testRule", singletonList("testRule"));
    }

    @Test
    public void assertRuleMatchedNeg() {
        expectedException.expect(java.lang.AssertionError.class);
        assertRuleMatched("negTestRule", singletonList("testRule"));
    }

    @Test
    public void assertRulesMatchedTest() {
        assertRulesMatched(singletonList("testRule"), singletonList("testRule"));
    }

    @Test
    public void assertRulesMatchedNegTest() {
        expectedException.expect(java.lang.AssertionError.class);
        assertRulesMatched(singletonList("negTestRule"), singletonList("testRule"));
    }

    @Test
    public void privateConstructorTest() throws
            NoSuchMethodException,
            IllegalAccessException,
            InvocationTargetException,
            InstantiationException {
        Constructor<AssertCoverage> constructor = AssertCoverage.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}