package org.ktorm.support.clickhouse

import org.ktorm.database.Database
import org.ktorm.database.SqlDialect
import org.ktorm.expression.SqlExpressionVisitor
import org.ktorm.expression.SqlExpressionVisitorInterceptor
import org.ktorm.expression.SqlFormatter
import org.ktorm.expression.newVisitorInstance

/**
 * [SqlDialect] implementation for Clickhouse database.
 */
open class ClickhouseDialect : SqlDialect {

    override fun createExpressionVisitor(interceptor: SqlExpressionVisitorInterceptor): SqlExpressionVisitor =
        ClickhouseExpressionVisitor::class.newVisitorInstance(interceptor)
    
    override fun createSqlFormatter(database: Database, beautifySql: Boolean, indentSize: Int): SqlFormatter =
        ClickhouseFormatter(database, beautifySql, indentSize)

}
