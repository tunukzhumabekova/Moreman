package com.ORT.repository;

import com.databil.mentormind.public_.tables.records.UserInfosRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.databil.mentormind.public_.Tables.USER_INFOS;

@Repository
public class UserInfoRepository {
    private final DSLContext dslContext;

    public UserInfoRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public int save(UserInfosRecord userInfosRecord) {
        return Objects.requireNonNull(dslContext.insertInto(USER_INFOS)
                        .set(userInfosRecord)
                        .returningResult(USER_INFOS.ID)
                        .fetchOne())
                .getValue(USER_INFOS.ID);
    }

    public UserInfosRecord findByEmail(String email) {
        return dslContext.selectFrom(USER_INFOS)
                .where(USER_INFOS.EMAIL.eq(email))
                .fetchOne();
    }

    public void saveCode(String email, String code) {
        dslContext.update(USER_INFOS)
                .set(USER_INFOS.CODE, code)
                .set(USER_INFOS.END_DATE, LocalDateTime.now().plusMinutes(30))
                .where(USER_INFOS.EMAIL.eq(email))
                .execute();
    }

    public void resetPassword(String email, String password) {
        dslContext.update(USER_INFOS)
                .set(USER_INFOS.CODE, (String) null)
                .set(USER_INFOS.END_DATE, (LocalDateTime) null)
                .set(USER_INFOS.RESET, false)
                .set(USER_INFOS.PASSWORD, password)
                .where(USER_INFOS.EMAIL.eq(email))
                .execute();
    }

    public void confirmResetPassword(String email) {
        dslContext.update(USER_INFOS)
                .set(USER_INFOS.RESET, true)
                .where(USER_INFOS.EMAIL.eq(email))
                .execute();

    }
}