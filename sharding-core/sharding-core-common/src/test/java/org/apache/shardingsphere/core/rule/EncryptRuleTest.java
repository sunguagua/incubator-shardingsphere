/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.core.rule;

import org.apache.shardingsphere.api.config.encrypt.EncryptColumnRuleConfiguration;
import org.apache.shardingsphere.api.config.encrypt.EncryptRuleConfiguration;
import org.apache.shardingsphere.api.config.encrypt.EncryptTableRuleConfiguration;
import org.apache.shardingsphere.api.config.encrypt.EncryptorRuleConfiguration;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;

public final class EncryptRuleTest {
    
    private final String table = "table";
    
    private final String column = "column";
    
    private EncryptRuleConfiguration encryptRuleConfig;
    
    @Before
    public void setUp() {
        Properties props = new Properties();
        EncryptorRuleConfiguration encryptorConfig = new EncryptorRuleConfiguration("assistedTest", props);
        EncryptColumnRuleConfiguration columnConfig = new EncryptColumnRuleConfiguration("plain_pwd", "cipher_pwd", "", "aes");
        EncryptTableRuleConfiguration tableConfig = new EncryptTableRuleConfiguration(Collections.singletonMap(column, columnConfig));
        encryptRuleConfig = new EncryptRuleConfiguration();
        encryptRuleConfig.getEncryptors().put("aes", encryptorConfig);
        encryptRuleConfig.getTables().put(table, tableConfig);
    }
    
    @Test
    public void assertGetEncryptAssistedQueryValues() {
        List<Object> encryptAssistedQueryValues = new EncryptRule(encryptRuleConfig).getEncryptAssistedQueryValues(table, column, Collections.singletonList(null));
        for (final Object value : encryptAssistedQueryValues) {
            assertNull(value);
        }
    }
    
    @Test
    public void assertGetEncryptValues() {
        List<Object> encryptAssistedQueryValues = new EncryptRule(encryptRuleConfig).getEncryptValues(table, column, Collections.singletonList(null));
        for (final Object value : encryptAssistedQueryValues) {
            assertNull(value);
        }
    }
    
    @Test
    public void assertFindEncryptTable() {
        assertTrue(new EncryptRule(encryptRuleConfig).findEncryptTable(table).isPresent());
    }
    
    @Test
    public void assertGetLogicColumn() {
        assertThat(new EncryptRule(encryptRuleConfig).getLogicColumn(table, "cipher_pwd"), is(column));
    }
    
    @Test
    public void assertFindPlainColumn() {
        assertFalse(new EncryptRule(encryptRuleConfig).findPlainColumn(table, "cipher_pwd").isPresent());
    }
    
    @Test(expected = NullPointerException.class)
    public void assertGetCipherColumnWhenNoEncryptColumn() {
        new EncryptRule(encryptRuleConfig).getCipherColumn(table, "cipher_pwd");
    }
    
    @Test
    public void assertGetCipherColumnWhenEncryptColumnExist() {
        assertThat(new EncryptRule(encryptRuleConfig).getCipherColumn(table, column), is("cipher_pwd"));
    }
    
    @Test
    public void assertIsCipherColumn() {
        assertTrue(new EncryptRule(encryptRuleConfig).isCipherColumn(table, "cipher_pwd"));
    }
    
    @Test
    public void assertFindAssistedQueryColumn() {
        assertFalse(new EncryptRule(encryptRuleConfig).findAssistedQueryColumn(table, "cipher_pwd").isPresent());
    }
    
    @Test
    public void assertGetAssistedQueryColumns() {
        assertTrue(new EncryptRule(encryptRuleConfig).getAssistedQueryColumns(table).isEmpty());
    }
    
    @Test
    public void assertGetAssistedQueryAndPlainColumns() {
        assertFalse(new EncryptRule(encryptRuleConfig).getAssistedQueryAndPlainColumns(table).isEmpty());
    }
    
    @Test
    public void assertGetLogicAndCipherColumns() {
        assertFalse(new EncryptRule(encryptRuleConfig).getLogicAndCipherColumns(table).isEmpty());
    }
    
    @Test
    public void assertGetLogicAndPlainColumns() {
        assertFalse(new EncryptRule(encryptRuleConfig).getLogicAndPlainColumns(table).isEmpty());
    }
    
    @Test
    public void assertGetEncryptTableNames() {
        assertFalse(new EncryptRule(encryptRuleConfig).getEncryptTableNames().isEmpty());
    }
}
