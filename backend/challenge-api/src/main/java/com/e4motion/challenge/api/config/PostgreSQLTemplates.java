package com.e4motion.challenge.api.config;

import com.querydsl.core.types.Ops;
import com.querydsl.jpa.JPQLTemplates;

// Set this template class to JPAQueryFactory
// When you want to change the command itself to send to the database.
public class PostgreSQLTemplates  extends JPQLTemplates {

    public PostgreSQLTemplates() {
        super();

        add(Ops.DateTimeOps.YEAR_WEEK, "cast(extract(isoyear from {0}) * 100 + extract(week from {0}) as integer)");
    }

}
