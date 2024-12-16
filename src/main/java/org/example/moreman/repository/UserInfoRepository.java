package org.example.moreman.repository;
import com.agro.public_.tables.records.UserInfosRecord;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import static com.agro.public_.Tables.USER_INFOS;

@Repository
public class UserInfoRepository {
    private final DSLContext dslContext;

    public UserInfoRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public UserInfosRecord findByEmail(String email) {
        return dslContext.selectFrom(USER_INFOS)
                .where(USER_INFOS.EMAIL.eq(email))
                .fetchOne();
    }
}