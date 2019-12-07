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

package org.apache.shardingsphere.core.rewrite.sql.token.generator;

import org.apache.shardingsphere.core.metadata.table.TableMetas;
import org.apache.shardingsphere.sql.parser.relation.statement.SQLStatementContext;
import org.apache.shardingsphere.core.rewrite.sql.token.generator.aware.ParametersAware;
import org.apache.shardingsphere.core.rewrite.sql.token.generator.aware.PreviousSQLTokensAware;
import org.apache.shardingsphere.core.rewrite.sql.token.generator.aware.TableMetasAware;
import org.apache.shardingsphere.core.rewrite.sql.token.pojo.SQLToken;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * SQL token generators.
 * 
 * @author zhangliang
 */
public final class SQLTokenGenerators {
    
    private final Collection<SQLTokenGenerator> sqlTokenGenerators = new LinkedList<>();
    
    /**
     * Add all SQL token generators.
     * 
     * @param sqlTokenGenerators SQL token generators
     */
    public void addAll(final Collection<SQLTokenGenerator> sqlTokenGenerators) {
        for (SQLTokenGenerator each : sqlTokenGenerators) {
            if (!containsClass(each)) {
                this.sqlTokenGenerators.add(each);
            }
        }
    }
    
    private boolean containsClass(final SQLTokenGenerator sqlTokenGenerator) {
        for (SQLTokenGenerator each : sqlTokenGenerators) {
            if (each.getClass() == sqlTokenGenerator.getClass()) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Generate SQL tokens.
     *
     * @param sqlStatementContext SQL statement context
     * @param parameters SQL parameters
     * @param tableMetas table metas
     * @return SQL tokens
     */
    public List<SQLToken> generateSQLTokens(final SQLStatementContext sqlStatementContext, final List<Object> parameters, final TableMetas tableMetas) {
        List<SQLToken> result = new LinkedList<>();
        for (SQLTokenGenerator each : sqlTokenGenerators) {
            setUpSQLTokenGenerator(each, parameters, tableMetas, result);
            if (!each.isGenerateSQLToken(sqlStatementContext)) {
                continue;
            }
            if (each instanceof OptionalSQLTokenGenerator) {
                SQLToken sqlToken = ((OptionalSQLTokenGenerator) each).generateSQLToken(sqlStatementContext);
                if (!result.contains(sqlToken)) {
                    result.add(sqlToken);
                }
            } else if (each instanceof CollectionSQLTokenGenerator) {
                result.addAll(((CollectionSQLTokenGenerator) each).generateSQLTokens(sqlStatementContext));
            }
        }
        return result;
    }
    
    private void setUpSQLTokenGenerator(final SQLTokenGenerator sqlTokenGenerator, final List<Object> parameters, final TableMetas tableMetas, final List<SQLToken> previousSQLTokens) {
        if (sqlTokenGenerator instanceof ParametersAware) {
            ((ParametersAware) sqlTokenGenerator).setParameters(parameters);
        }
        if (sqlTokenGenerator instanceof TableMetasAware) {
            ((TableMetasAware) sqlTokenGenerator).setTableMetas(tableMetas);
        }
        if (sqlTokenGenerator instanceof PreviousSQLTokensAware) {
            ((PreviousSQLTokensAware) sqlTokenGenerator).setPreviousSQLTokens(previousSQLTokens);
        }
    }
}
