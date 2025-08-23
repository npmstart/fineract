/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.fineract.infrastructure.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit test for birthday filtering functionality in SearchParameters.
 */
public class SearchParametersBirthdayTest {

    @Test
    public void testForSavingsWithBirthdayParameters() {
        // Test creating SearchParameters with birthday filtering
        SearchParameters searchParams = SearchParameters.forSavings(
                "test", // sqlSearch
                "ext123", // externalId
                0, // offset
                10, // limit
                "id", // orderBy
                "ASC", // sortOrder
                12, // birthdayMonth
                8 // birthdayDay
        );

        assertTrue(searchParams.isBirthdayFilteringEnabled());
        assertEquals(Integer.valueOf(12), searchParams.getBirthdayMonth());
        assertEquals(Integer.valueOf(8), searchParams.getBirthdayDay());
    }

    @Test
    public void testForSavingsWithoutBirthdayParameters() {
        // Test creating SearchParameters without birthday filtering (backward
        // compatibility)
        SearchParameters searchParams = SearchParameters.forSavings(
                "test", // sqlSearch
                "ext123", // externalId
                0, // offset
                10, // limit
                "id", // orderBy
                "ASC" // sortOrder
        );

        assertFalse(searchParams.isBirthdayFilteringEnabled());
        assertNull(searchParams.getBirthdayMonth());
        assertNull(searchParams.getBirthdayDay());
    }

    @Test
    public void testBirthdayFilteringEnabledOnlyWhenBothParametersProvided() {
        // Test with only month provided
        SearchParameters searchParams1 = SearchParameters.forSavings(
                null, null, 0, 10, null, null, 12, null);
        assertFalse(searchParams1.isBirthdayFilteringEnabled());

        // Test with only day provided
        SearchParameters searchParams2 = SearchParameters.forSavings(
                null, null, 0, 10, null, null, null, 8);
        assertFalse(searchParams2.isBirthdayFilteringEnabled());

        // Test with both provided
        SearchParameters searchParams3 = SearchParameters.forSavings(
                null, null, 0, 10, null, null, 12, 8);
        assertTrue(searchParams3.isBirthdayFilteringEnabled());
    }
}