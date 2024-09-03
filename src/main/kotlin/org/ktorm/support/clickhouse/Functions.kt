package org.ktorm.support.clickhouse

import org.ktorm.expression.ArgumentExpression
import org.ktorm.expression.FunctionExpression
import org.ktorm.schema.*
import java.time.LocalDate

fun rand(): FunctionExpression<Long> = FunctionExpression("rand", emptyList(), LongSqlType)

fun rand32(): FunctionExpression<Long> = FunctionExpression("rand32", emptyList(), LongSqlType)

fun rand64(): FunctionExpression<Long> = FunctionExpression("rand64", emptyList(), LongSqlType)

fun <T : Comparable<T>> greatest(vararg columns: ColumnDeclaring<T>): FunctionExpression<T> = FunctionExpression("greatest", columns.map { it.asExpression() }, columns[0].sqlType)

fun <T : Comparable<T>> greatest(left: ColumnDeclaring<T>, right: T): FunctionExpression<T> = greatest(left, left.wrapArgument(right))

fun <T : Comparable<T>> greatest(left: T, right: ColumnDeclaring<T>): FunctionExpression<T> = greatest(right.wrapArgument(left), right)

fun <T : Comparable<T>> least(vararg columns: ColumnDeclaring<T>): FunctionExpression<T> = FunctionExpression("least", columns.map { it.asExpression() }, columns[0].sqlType)

fun <T : Comparable<T>> least(left: ColumnDeclaring<T>, right: T): FunctionExpression<T> = least(left, left.wrapArgument(right))

fun <T : Comparable<T>> least(left: T, right: ColumnDeclaring<T>): FunctionExpression<T> = least(right.wrapArgument(left), right)

fun <T : Any> ColumnDeclaring<T>.ifNull(right: ColumnDeclaring<T>): FunctionExpression<T> = FunctionExpression("ifnull", listOf(this, right).map { it.asExpression() }, sqlType)

fun <T : Any> ColumnDeclaring<T>.ifNull(right: T?): FunctionExpression<T> = this.ifNull(wrapArgument(right))

@Suppress("FunctionNaming", "FunctionName")
fun <T : Any> IF(condition: ColumnDeclaring<Boolean>, then: ColumnDeclaring<T>, otherwise: ColumnDeclaring<T>): FunctionExpression<T> = FunctionExpression("if", listOf(condition, then, otherwise).map { it.asExpression() }, then.sqlType)

@Suppress("FunctionNaming", "FunctionName")
inline fun <reified T : Any> IF(condition: ColumnDeclaring<Boolean>, then: T, otherwise: T, sqlType: SqlType<T> = SqlType.of() ?: error("Cannot detect the param's SqlType, please specify manually.")): FunctionExpression<T> = FunctionExpression("if", listOf(condition.asExpression(), ArgumentExpression(then, sqlType), ArgumentExpression(otherwise, sqlType)), sqlType)

fun dateDiff(unit: String, left: ColumnDeclaring<LocalDate>, right: ColumnDeclaring<LocalDate>): FunctionExpression<Int> = FunctionExpression("datediff", listOf(ArgumentExpression(unit, SqlType.of<String>()!!), left.asExpression(), right.asExpression()), IntSqlType)

fun dateDiff(unit: String, left: ColumnDeclaring<LocalDate>, right: LocalDate): FunctionExpression<Int> = dateDiff(unit, left, left.wrapArgument(right))

fun dateDiff(unit: String, left: LocalDate, right: ColumnDeclaring<LocalDate>): FunctionExpression<Int> = dateDiff(unit, right.wrapArgument(left), right)

fun ColumnDeclaring<String>.replaceAll(oldValue: String, newValue: String): FunctionExpression<String> = FunctionExpression("replaceAll", listOf(this.asExpression(), ArgumentExpression(oldValue, VarcharSqlType), ArgumentExpression(newValue, VarcharSqlType)), VarcharSqlType)

fun ColumnDeclaring<String>.toLowerCase(): FunctionExpression<String> = FunctionExpression("lower", listOf(this.asExpression()), VarcharSqlType)

fun ColumnDeclaring<String>.toUpperCase(): FunctionExpression<String> = FunctionExpression("upper", listOf(this.asExpression()), VarcharSqlType)
