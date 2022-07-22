package com.e4motion.challenge.api.config;

import com.querydsl.core.types.Ops;
import com.querydsl.jpa.JPQLTemplates;

// DateTimeOps에 따른 데이터베이스로 보낼 함수 자체를 바꾸고자 할 때 Templates 을 재정의하고
// JPAQueryFactory 생성 시 Template 파라미터로 재정의된 Template 을 전달한다.
public class PostgreSQLTemplates  extends JPQLTemplates {

    public PostgreSQLTemplates() {
        super();

        add(Ops.DateTimeOps.YEAR_WEEK, "cast(extract(isoyear from {0}) * 100 + extract(week from {0}) as integer)");
    }

}
