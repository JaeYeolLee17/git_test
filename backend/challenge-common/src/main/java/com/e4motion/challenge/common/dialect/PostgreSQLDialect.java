package com.e4motion.challenge.common.dialect;

import org.hibernate.dialect.PostgreSQL10Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

// 데이터베이스에서 실행될 함수를 변경하고자 할 때 Dialect 을 재정의하고
// 애플리케이션의 jpa dialect 설정에서 재정의된 class 를 지정한다.
public class PostgreSQLDialect extends PostgreSQL10Dialect {

    public PostgreSQLDialect() {
        super();

        registerFunction( "dayofweek", new SQLFunctionTemplate(StandardBasicTypes.INTEGER, "extract(isodow from ?1)") );
        registerFunction( "week", new SQLFunctionTemplate(StandardBasicTypes.INTEGER, "extract(week from ?1)") );
    }

}