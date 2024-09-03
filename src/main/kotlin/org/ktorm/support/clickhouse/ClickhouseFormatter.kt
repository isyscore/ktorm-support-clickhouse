package org.ktorm.support.clickhouse

import org.ktorm.database.Database
import org.ktorm.expression.*
import org.ktorm.schema.IntSqlType

/**
 * [SqlFormatter] implementation for Clickhouse, formatting SQL expressions as strings with their execution arguments.
 */
open class ClickhouseFormatter(database: Database, beautifySql: Boolean, indentSize: Int) : SqlFormatter(database, beautifySql, indentSize), ClickhouseExpressionVisitor {

    override fun visit(expr: SqlExpression): SqlExpression {
        val result = super<ClickhouseExpressionVisitor>.visit(expr)
        check(result === expr) { "SqlFormatter cannot modify the expression tree." }
        return result
    }

    override fun visitQuerySource(expr: QuerySourceExpression): QuerySourceExpression = super<SqlFormatter>.visitQuerySource(expr)

    override fun visitSelect(expr: SelectExpression): SelectExpression {
        super<SqlFormatter>.visitSelect(expr)
        return expr
    }

    override fun writePagination(expr: QueryExpression) {
        newLine(Indentation.SAME)
        writeKeyword("limit ?, ? ")
        _parameters += ArgumentExpression(expr.offset ?: 0, IntSqlType)
        _parameters += ArgumentExpression(expr.limit ?: Int.MAX_VALUE, IntSqlType)
    }

    override fun visitBulkInsert(expr: BulkInsertExpression): BulkInsertExpression {
        writeKeyword("insert into ")
        visitTable(expr.table)
        writeInsertColumnNames(expr.assignments[0].map { it.column })
        writeKeyword("values ")

        for ((i, assignments) in expr.assignments.withIndex()) {
            if (i > 0) {
                removeLastBlank()
                write(", ")
            }
            writeInsertValues(assignments)
        }

        if (expr.updateAssignments.isNotEmpty()) {
            writeKeyword("on duplicate key update ")
            writeColumnAssignments(expr.updateAssignments)
        }

        return expr
    }
}
