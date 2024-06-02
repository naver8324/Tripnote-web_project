package com.elice.tripnote.global.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //400
    EXCEED_SIZE_LIMIT(HttpStatus.BAD_REQUEST, "파일 크기가 범위를 넘었습니다."),
    NOT_MATCHED_TYPE(HttpStatus.BAD_REQUEST, "이미지가 아닌 파일입니다."),
    NOT_VALID_ROUTE(HttpStatus.BAD_REQUEST, "이 경로는 비공개, 삭제되었거나 유저의 경로가 아닙니다."),

    // 401
    TOKEN_MISSING_OR_INVALID(HttpStatus.UNAUTHORIZED, "토큰이 없거나 적합하지 않습니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "해당 실행을 수행할 권한이 없습니다."),

    // 404
    NO_POST(HttpStatus.NOT_FOUND, "해당하는 게시글은 존재하지 않습니다."),
    NO_MEMBER(HttpStatus.NOT_FOUND, "해당하는 유저는 존재하지 않습니다."),
    NO_SPOT(HttpStatus.NOT_FOUND, "해당하는 여행지가 존재하지 않습니다."),
    NO_ROUTE(HttpStatus.NOT_FOUND, "해당하는 경로가 존재하지 않습니다."),
    NOT_FOUND_ALGORITHM(HttpStatus.NOT_FOUND, "SHA-1 알고리즘을 찾을 수 없습니다."),
    NO_USER(HttpStatus.NOT_FOUND, "해당하는 유저는 존재하지 않습니다."),
    NO_COMMENT(HttpStatus.NOT_FOUND, "해당하는 댓글은 존재하지 않습니다."),
    NO_INTEGRATED_ROUTE_STATUS(HttpStatus.NOT_FOUND, "해당 지역은 존재하지 않습니다."),
    NO_LANDMARK(HttpStatus.NOT_FOUND, "존재하지 않는 랜드마크입니다."),
    NO_REGION(HttpStatus.NOT_FOUND, "존재하지 않는 지역입니다."),

    // 409
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
    DUPLICATE_NAME(HttpStatus.CONFLICT, "이미 존재하는 해시태그명입니다.");








    private final HttpStatus httpStatus;
    private final String message;
}
