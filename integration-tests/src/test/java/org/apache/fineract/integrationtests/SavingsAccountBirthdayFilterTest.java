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
package org.apache.fineract.integrationtests;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import java.util.HashMap;
import org.apache.fineract.integrationtests.common.ClientHelper;
import org.apache.fineract.integrationtests.common.Utils;
import org.apache.fineract.integrationtests.common.savings.SavingsAccountHelper;
import org.apache.fineract.integrationtests.common.savings.SavingsProductHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Integration Test for savings accounts birthday filtering functionality.
 *
 * @author Birthday Dashboard Team
 */
public class SavingsAccountBirthdayFilterTest {

    private static final Logger LOG = LoggerFactory.getLogger(SavingsAccountBirthdayFilterTest.class);
    public static final String MINIMUM_OPENING_BALANCE = "1000.0";
    public static final String ACCOUNT_TYPE_INDIVIDUAL = "INDIVIDUAL";

    private ResponseSpecification responseSpec;
    private RequestSpecification requestSpec;
    private SavingsAccountHelper savingsAccountHelper;

    @BeforeEach
    public void setup() {
        Utils.initializeRESTAssured();
        this.requestSpec = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        this.requestSpec.header("Authorization", "Basic " + Utils.loginIntoServerAndGetBase64EncodedAuthenticationKey());
        this.requestSpec.header("Fineract-Platform-TenantId", "default");
        this.responseSpec = new ResponseSpecBuilder().expectStatusCode(200).build();
    }

    @Test
    public void testSavingsAccountBirthdayFiltering() {
        LOG.info("-------------------------------- TEST SAVINGS ACCOUNT BIRTHDAY FILTERING ---------------------------------------");
        
        this.savingsAccountHelper = new SavingsAccountHelper(this.requestSpec, this.responseSpec);

        // Test birthday filtering endpoint with valid parameters
        String responseWithBirthdayFilter = this.savingsAccountHelper.retrieveAllSavingsAccountsWithBirthday(12, 8);
        LOG.info("Response with birthday filter (Dec 8): {}", responseWithBirthdayFilter);
        
        // Should return valid JSON response even if no results
        Assertions.assertNotNull(responseWithBirthdayFilter);
        Assertions.assertTrue(responseWithBirthdayFilter.contains("totalFilteredRecords"));
        Assertions.assertTrue(responseWithBirthdayFilter.contains("pageItems"));

        // Test normal endpoint without birthday filtering
        String responseNormal = this.savingsAccountHelper.retrieveAllSavingsAccounts();
        LOG.info("Response without birthday filter: {}", responseNormal);
        
        Assertions.assertNotNull(responseNormal);
        Assertions.assertTrue(responseNormal.contains("totalFilteredRecords"));
        Assertions.assertTrue(responseNormal.contains("pageItems"));
        
        LOG.info("Birthday filtering test completed successfully");
    }

    @Test  
    public void testSavingsAccountBirthdayFilteringWithDifferentDates() {
        LOG.info("-------------------------------- TEST DIFFERENT BIRTHDAY DATES ---------------------------------------");
        
        this.savingsAccountHelper = new SavingsAccountHelper(this.requestSpec, this.responseSpec);

        // Test various birthday combinations
        String response1 = this.savingsAccountHelper.retrieveAllSavingsAccountsWithBirthday(1, 1);
        String response2 = this.savingsAccountHelper.retrieveAllSavingsAccountsWithBirthday(6, 15);
        String response3 = this.savingsAccountHelper.retrieveAllSavingsAccountsWithBirthday(12, 31);
        
        // All should return valid responses
        Assertions.assertNotNull(response1);
        Assertions.assertNotNull(response2);
        Assertions.assertNotNull(response3);
        
        LOG.info("Different birthday dates test completed successfully");
    }
}