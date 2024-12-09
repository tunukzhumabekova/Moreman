package org.example.moreman.repository;


import com.agro.public_.tables.records.UserInfosRecord;
import com.agro.public_.tables.records.UsersRecord;
import org.example.moreman.model.response.UserInfo;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Objects;

import static com.agro.public_.Tables.USERS;
import static com.agro.public_.Tables.USER_INFOS;

@Repository
public class UserRepository {
    private final DSLContext dslContext;

    public UserRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }


    public boolean existsById(Integer userId) {
        return dslContext.fetchExists(
                dslContext.selectFrom(USERS)
                        .where(USERS.ID.eq(userId))
        );
    }

    public int save(UsersRecord usersRecord, int userInfoId) {
        return Objects.requireNonNull(dslContext.insertInto(USERS)
                        .set(usersRecord)
                        .returningResult(USERS.ID)
                        .fetchOne())
                .getValue(USERS.ID);
    }

    public UsersRecord findByUserId(Integer userInfoId) {
        return null;
    }


    public UserInfo findUserByEmail(String email) {
        return dslContext.select(USER_INFOS.ID, USER_INFOS.EMAIL, USER_INFOS.PASSWORD, USER_INFOS.ROLE)
                .from(USER_INFOS)
                .where(USER_INFOS.EMAIL.eq(email))
                .fetchOneInto(UserInfo.class);
    }

    public UsersRecord findByUserInfoId(Integer userInfoId) {
        return dslContext.selectFrom(USERS)
                .where(USERS.USER_INFO_ID.eq(userInfoId))
                .fetchOne();
    }
}