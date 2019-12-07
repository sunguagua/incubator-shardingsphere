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

package org.apache.shardingsphere.core.rewrite.feature.encrypt.parameter.impl;

import lombok.Setter;
import org.apache.shardingsphere.core.metadata.table.TableMetas;
import org.apache.shardingsphere.sql.parser.relation.statement.SQLStatementContext;
import org.apache.shardingsphere.core.rewrite.feature.encrypt.EncryptCondition;
import org.apache.shardingsphere.core.rewrite.feature.encrypt.EncryptConditionEngine;
import org.apache.shardingsphere.core.rewrite.feature.encrypt.aware.QueryWithCipherColumnAware;
import org.apache.shardingsphere.core.rewrite.feature.encrypt.parameter.EncryptParameterRewriter;
import org.apache.shardingsphere.core.rewrite.parameter.builder.ParameterBuilder;
import org.apache.shardingsphere.core.rewrite.parameter.builder.impl.StandardParameterBuilder;
import org.apache.shardingsphere.core.rewrite.sql.token.generator.aware.TableMetasAware;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Predicate parameter rewriter for encrypt.
 *
 * @author zhangliang
 */
@Setter
public final class EncryptPredicateParameterRewriter extends EncryptParameterRewriter implements TableMetasAware, QueryWithCipherColumnAware {
    
    private TableMetas tableMetas;
    
    private boolean queryWithCipherColumn;
    
    @Override
    protected boolean isNeedRewriteForEncrypt(final SQLStatementContext sqlStatementContext) {
        return true;
    }
    
    @Override
    public void rewrite(final ParameterBuilder parameterBuilder, final SQLStatementContext sqlStatementContext, final List<Object> parameters) {
        List<EncryptCondition> encryptConditions = new EncryptConditionEngine(getEncryptRule(), tableMetas).createEncryptConditions(sqlStatementContext);
        if (encryptConditions.isEmpty()) {
            return;
        }
        for (EncryptCondition each : encryptConditions) {
            if (queryWithCipherColumn) {
                encryptParameters(parameterBuilder, each.getPositionIndexMap(), getEncryptedValues(each, each.getValues(parameters)));
            }
        }
    }
    
    private List<Object> getEncryptedValues(final EncryptCondition encryptCondition, final List<Object> originalValues) {
        String tableName = encryptCondition.getTableName();
        String columnName = encryptCondition.getColumnName();
        return getEncryptRule().findAssistedQueryColumn(tableName, columnName).isPresent()
                ? getEncryptRule().getEncryptAssistedQueryValues(tableName, columnName, originalValues) : getEncryptRule().getEncryptValues(tableName, columnName, originalValues);
    }
    
    private void encryptParameters(final ParameterBuilder parameterBuilder, final Map<Integer, Integer> positionIndexes, final List<Object> encryptValues) {
        if (!positionIndexes.isEmpty()) {
            for (Entry<Integer, Integer> entry : positionIndexes.entrySet()) {
                ((StandardParameterBuilder) parameterBuilder).addReplacedParameters(entry.getValue(), encryptValues.get(entry.getKey()));
            }
        }
    }
}
