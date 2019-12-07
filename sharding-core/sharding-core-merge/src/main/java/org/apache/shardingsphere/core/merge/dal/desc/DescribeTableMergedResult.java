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

package org.apache.shardingsphere.core.merge.dal.desc;

import com.google.common.base.Optional;
import org.apache.shardingsphere.core.execute.sql.execute.result.QueryResult;
import org.apache.shardingsphere.core.merge.dql.common.MemoryMergedResult;
import org.apache.shardingsphere.core.merge.dql.common.MemoryQueryResultRow;
import org.apache.shardingsphere.core.metadata.table.TableMetas;
import org.apache.shardingsphere.core.rule.ShardingRule;
import org.apache.shardingsphere.core.strategy.encrypt.EncryptTable;
import org.apache.shardingsphere.sql.parser.relation.statement.SQLStatementContext;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Merged result for desc table.
 *
 * @author liya
 */
public final class DescribeTableMergedResult extends MemoryMergedResult {
    
    public DescribeTableMergedResult(final ShardingRule shardingRule, final List<QueryResult> queryResults, final SQLStatementContext sqlStatementContext) throws SQLException {
        super(shardingRule, null, sqlStatementContext, queryResults);
    }
    
    @Override
    protected List<MemoryQueryResultRow> init(final ShardingRule shardingRule, final TableMetas tableMetas, 
                                              final SQLStatementContext sqlStatementContext, final List<QueryResult> queryResults) throws SQLException {
        List<MemoryQueryResultRow> result = new LinkedList<>();
        for (QueryResult each : queryResults) {
            while (each.next()) {
                Optional<MemoryQueryResultRow> memoryQueryResultRow = optimize(shardingRule, sqlStatementContext, each);
                if (memoryQueryResultRow.isPresent()) {
                    result.add(memoryQueryResultRow.get());
                }
            }
        }
        return result;
    }
    
    private Optional<MemoryQueryResultRow> optimize(final ShardingRule shardingRule, final SQLStatementContext sqlStatementContext, final QueryResult queryResult) throws SQLException {
        MemoryQueryResultRow memoryQueryResultRow = new MemoryQueryResultRow(queryResult);
        Optional<EncryptTable> encryptTable = null == shardingRule.getEncryptRule()
                ? Optional.<EncryptTable>absent() : shardingRule.getEncryptRule().findEncryptTable(sqlStatementContext.getTablesContext().getSingleTableName());
        if (encryptTable.isPresent()) {
            String columnName = memoryQueryResultRow.getCell(1).toString();
            if (encryptTable.get().getAssistedQueryColumns().contains(columnName) || encryptTable.get().getPlainColumns().contains(columnName)) {
                return Optional.absent();
            }
            if (encryptTable.get().getCipherColumns().contains(columnName)) {
                memoryQueryResultRow.setCell(1, encryptTable.get().getLogicColumn(columnName));
            }
        }
        return Optional.of(memoryQueryResultRow);
    }
}
