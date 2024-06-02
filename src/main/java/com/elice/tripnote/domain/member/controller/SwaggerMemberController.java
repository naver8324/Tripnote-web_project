package com.elice.tripnote.domain.member.controller;

import com.elice.tripnote.domain.member.entity.Member;
import com.elice.tripnote.domain.member.entity.MemberRequestDTO;
import com.elice.tripnote.domain.member.entity.PasswordDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Member API", description = "회원 API입니다.")
public interface SwaggerMemberController {

    @Operation(summary = "회원 가입", description = "새로운 회원을 등록합니다.")
    @ApiResponse(responseCode = "201", description = "회원 가입에 성공하였습니다.")
    @PostMapping("/signup")
    ResponseEntity<Void> signup(@RequestBody MemberRequestDTO memberRequestDTO);


    @Operation(summary = "이메일로 회원 조회", description = "이메일을 기반으로 회원을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "회원 조회에 성공하였습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Member.class)))
    @GetMapping("/{email}")
    ResponseEntity<Member> getMemberByEmail(@PathVariable @Parameter(description = "이메일 주소", required = true) String email);


    @Operation(summary = "이메일 중복 확인", description = "입력한 이메일이 이미 등록되어 있는지 확인합니다. (이메일이 이미 존재하면 true 반환, 사용가능하면 false 반환)")
    @ApiResponse(responseCode = "200", description = "이메일 중복 확인에 성공하였습니다.")
    @GetMapping("/check-email")
    ResponseEntity<Boolean> checkEmailDuplicate(@RequestParam @Parameter(description = "이메일 주소", required = true) String email);


    @Operation(summary = "닉네임 중복 확인", description = "입력한 닉네임이 이미 등록되어 있는지 확인합니다. (닉네임이 이미 존재하면 true 반환, 사용가능하면 false 반환)")
    @ApiResponse(responseCode = "200", description = "닉네임 중복 확인에 성공하였습니다.")
    @GetMapping("/check-nickname")
    ResponseEntity<Boolean> checkNicknameDuplicate(@RequestParam @Parameter(description = "닉네임", required = true) String nickname);



    @Operation(summary = "닉네임 변경", description = "(로그인중) 회원의 닉네임을 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "닉네임 변경에 성공하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자입니다. (토큰 값이 제대로 전달되었는지 확인이 필요합니다.)", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "해당하는 유저는 존재하지 않습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "이미 사용 중인 닉네임입니다.", content = @Content(mediaType = "application/json"))
    })
    @PatchMapping("/update-nickname")
    public ResponseEntity<Void> updateNickname(@RequestHeader("Authorization") String jwt, @RequestParam @Parameter(description = "새 닉네임", required = true) String newNickname);


    @Operation(summary = "비밀번호 변경", description = "(로그인중) 회원의 비밀번호를 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 변경에 성공하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자입니다. (토큰 값이 제대로 전달되었는지 확인이 필요합니다.)", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "해당하는 유저는 존재하지 않습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "현재 비밀번호가 일치하지 않습니다.", content = @Content(mediaType = "application/json"))
    })
    @PatchMapping("/update-password")
    ResponseEntity<Void> updatePassword(@RequestHeader("Authorization") String jwt, @RequestBody @Parameter(description = "새 비밀번호(json형식으로 key는 password)", required = true) PasswordDTO newPasswordDTO);


    @Operation(summary = "회원 삭제", description = "(로그인중) 회원을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 삭제에 성공하였습니다."),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자입니다. (토큰 값이 제대로 전달되었는지 확인이 필요합니다.)", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "해당하는 유저는 존재하지 않습니다.")
    })
    @DeleteMapping("/delete-member")
    ResponseEntity<Void> deleteMember(@RequestHeader("Authorization") String jwt);


    @Operation(summary = "비밀번호 검증", description = "(로그인 중) 회원의 비밀번호를 검증합니다. (비밀번호가 일치하면 true 반환, 일치하지 않으면 false 반환)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호가 일치합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자입니다. (토큰 값이 제대로 전달되었는지 확인이 필요합니다.)", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "해당하는 유저는 존재하지 않습니다.", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/validate-password")
    ResponseEntity<Boolean> validatePassword(@RequestHeader("Authorization") String jwt, @RequestBody @Parameter(description = "검증할 비밀번호(json형식으로 key는 password)", required = true) PasswordDTO validatePasswordDTO);


}
