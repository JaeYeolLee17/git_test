package com.e4motion.challenge.common.dialect;

import org.hibernate.dialect.PostgreSQL10Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

// Set this class to the database dialect when you want to change the function to be executed in the database.
public class PostgreSQLDialect extends PostgreSQL10Dialect {

    public PostgreSQLDialect() {
        super();

        registerFunction( "dayofweek", new SQLFunctionTemplate(StandardBasicTypes.INTEGER, "extract(isodow from ?1)") );
        registerFunction( "week", new SQLFunctionTemplate(StandardBasicTypes.INTEGER, "extract(week from ?1)") );
    }

}