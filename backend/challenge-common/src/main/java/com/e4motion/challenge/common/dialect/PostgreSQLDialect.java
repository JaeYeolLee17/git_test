package com.e4motion.challenge.common.dialect;

import org.hibernate.dialect.PostgreSQL10Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

public class PostgreSQLDialect extends PostgreSQL10Dialect {

    public PostgreSQLDialect() {
        super();
        registerFunction( "dayofweek", new SQLFunctionTemplate(StandardBasicTypes.INTEGER, "extract(isodow from ?1)") );
    }

}