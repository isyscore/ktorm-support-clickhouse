module ktorm.support.clickhouse {
    requires ktorm.core;
    exports org.ktorm.support.clickhouse;
    provides org.ktorm.database.SqlDialect with org.ktorm.support.clickhouse.ClickhouseDialect;
}
