package org.example.moreman.repository;

import com.agro.public_.tables.records.UserInfosRecord;
import org.example.moreman.model.response.UserInfo;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import static com.agro.public_.Tables.USER_INFOS;

@Repository
public class UserRepository {
    private final DSLContext dslContext;

    public UserRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }






    public UserInfo findUserByEmail(String email) {
        return dslContext.select(USER_INFOS.ID, USER_INFOS.EMAIL, USER_INFOS.PASSWORD)
                .from(USER_INFOS)
                .where(USER_INFOS.EMAIL.eq(email))
                .fetchOneInto(UserInfo.class);
    }

    public UserInfosRecord findByUserInfoId(Integer userInfoId) {
        return dslContext.selectFrom(USER_INFOS)
                .where(USER_INFOS.ID.eq(userInfoId))
                .fetchOne();
    }
}