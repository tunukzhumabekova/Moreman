package com.ORT.repository;

import com.ORT.model.response.Payment;
import com.ORT.model.response.QuidResponse;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;

import static com.databil.mentormind.public_.Tables.PAYMENT;
import static com.databil.mentormind.public_.Tables.USER_QUIDS;

@Repository
public class PaymentRepository {
    private final DSLContext dslContext;

    public PaymentRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public QuidResponse createAndSaveQuid(Integer userId) {
        String quid = generateQuid();

        Integer id = Objects.requireNonNull(dslContext.insertInto(USER_QUIDS)
                        .set(USER_QUIDS.USER_ID, userId)
                        .set(USER_QUIDS.QUID, quid)
                        .set(USER_QUIDS.CREATED_AT, LocalDateTime.now())
                        .returning(USER_QUIDS.ID) // Указываем, что нужно вернуть поле ID
                        .fetchOne())              // Выполняем запрос и получаем одну запись
                .getValue(USER_QUIDS.ID); // Извлекаем значение ID сразу как Integer

        return new QuidResponse(id, quid);
    }

    private String generateQuid() {
        Random random = new Random();
        StringBuilder letters = new StringBuilder(3);
        for (int i = 0; i < 3; i++) {
            letters.append((char) ('A' + random.nextInt(26)));
        }

        StringBuilder numbers = new StringBuilder(3);
        for (int i = 0; i < 3; i++) {
            numbers.append(random.nextInt(10));}

        return letters.toString() + numbers.toString();
    }

    public Integer savePayment(int month, int price, int userQuidsId) {
        return dslContext.insertInto(PAYMENT,
                        PAYMENT.MONTH,
                        PAYMENT.PRICE,
                        PAYMENT.STARTDATE,
                        PAYMENT.ENDDATE,
                        PAYMENT.USER_QUIDS_ID)
                .values(
                        month,
                        price,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusMonths(month),
                        userQuidsId
                )
                .returning(PAYMENT.ID)
                .fetchOne()
                .getValue(PAYMENT.ID);
    }

    public Payment getById(Integer id) {
        return dslContext.selectFrom(PAYMENT)
                .where(PAYMENT.ID.eq(id))
                .fetchOneInto(Payment.class);
    }
}

