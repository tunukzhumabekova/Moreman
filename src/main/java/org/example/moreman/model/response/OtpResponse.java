package org.example.moreman.model.response;

public record OtpResponse(
        String quid,
        Integer code,
        String comment,
        String displayName,
        String txnId
) {
    public OtpResponse(Integer code, String comment, String txnId) {
        this("", code, comment, "", txnId);
    }

    public OtpResponse(String quid, Integer code, String comment, String displayName) {
        this(quid, code, comment, displayName, "");
    }

    public OtpResponse(String quid, Integer code, String comment) {
        this(quid, code, comment, "", "");
    }
}