/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The SF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.apache.sling.hc.api;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

public class ResultTest {
    private ResultLog resultLog;
    private Result result;
    
    @Before
    public void setup() {
        resultLog = new ResultLog(LoggerFactory.getLogger(getClass()));
        result = new Result(null, resultLog);
    }
    
    @Test
    public void testInitiallyNotOk() {
        assertFalse(result.isOk());
    }
    
    @Test
    public void testDebugOk() {
        resultLog.debug("something");
        assertTrue(result.isOk());
    }
    
    @Test
    public void testInfoOk() {
        resultLog.info("something");
        assertTrue(result.isOk());
    }
    
    @Test
    public void testWarnNotOk() {
        resultLog.warn("something");
        assertFalse(result.isOk());
    }
    
    @Test
    public void testErrorNotOk() {
        resultLog.error("something");
        assertFalse(result.isOk());
    }
}
