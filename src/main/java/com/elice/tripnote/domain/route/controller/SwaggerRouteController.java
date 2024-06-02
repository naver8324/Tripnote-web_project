package com.elice.tripnote.domain.route.controller;

import com.elice.tripnote.domain.route.entity.LikeBookmarkResponseDTO;
import com.elice.tripnote.domain.route.entity.SaveRequestDTO;
import com.elice.tripnote.domain.route.entity.SpotResponseDTO;
import com.elice.tripnote.global.entity.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "route API", description = "경로 관련 api입니다.")
public interface SwaggerRouteController {
    @Operation(summary = "경로 생성", description = "경로를 추가합니다.")
    @Parameters({
            @Parameter(name = "requestDto", description = "경로를 만드는 유저 id, 총 경비, 여행지 id 리스트, 해시태그 id 리스트")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(schema = @Schema(implementation = Long.class))}),
            @ApiResponse(responseCode = "404", description = "해당 유저 ID 또는 Order Id가 존재하지 않습니다."),
    })
    ResponseEntity<Long> save(SaveRequestDTO requestDto);

    @Operation(summary = "경로 비공개", description = "경로를 비공개 상태로 변경합니다.")
    @Parameters({
            @Parameter(name = "routeId", description = "비공개하려는 경로의 id")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(schema = @Schema(implementation = Long.class))}),
            @ApiResponse(responseCode = "404", description = "해당 Route Id가 존재하지 않습니다."),
    })
    ResponseEntity<Long> setRouteToPrivate(Long routeId);

    @Operation(summary = "경로 공개", description = "경로를 공개 상태로 변경합니다.")
    @Parameters({
            @Parameter(name = "routeId", description = "공개하려는 경로의 id")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(schema = @Schema(implementation = Long.class))}),
            @ApiResponse(responseCode = "404", description = "해당 Route Id가 존재하지 않습니다."),
    })
    ResponseEntity<Long> setRouteToPublic(Long routeId);

    @Operation(summary = "경로 삭제", description = "경로를 삭제합니다.")
    @Parameters({
            @Parameter(name = "routeId", description = "삭제하려는 경로의 id")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(schema = @Schema(implementation = Long.class))}),
            @ApiResponse(responseCode = "404", description = "해당 Route Id가 존재하지 않습니다."),
    })
    ResponseEntity<Long> deleteRoute(Long routeId);

    @Operation(summary="특정 지역의 경로 리턴", description= "지역을 입력받아서서 해당 지역 안을 여행하는 경로 id 리스트를 반환받을 수 있다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공하였습니다.",  content = @Content(mediaType = "application/json")),
    })
    @Parameters(value = {
            @Parameter(name="region", description = "지역"),
            @Parameter(name="hashtags", description = "해시태그 리스트"),
    })
    ResponseEntity<List<Long>> getRegion(String region, List<Long> hashtags);

    @Operation(summary="경로에 포함된 여행지 리스트 리턴", description= "특정 경로 id를 이용해서 해당 경로에 포함된 여행지들을 리스트로 반환받을 수 있다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공하였습니다.",  content = @Content(mediaType = "application/json")),
    })
    @Parameters(value = {
            @Parameter(name="routeId", description = "경로 번호"),
    })
    ResponseEntity<List<SpotResponseDTO>> getSpots( Long integratedRouteId);

    @Operation(summary="경로의 좋아요 수, 북마크 수 리턴", description= "특정 경로 id를 이용해서 해당 경로의 좋아요 수, 북마크 수를 반환받을 수 있다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공하였습니다.",  content = @Content(mediaType = "application/json")),
    })
    @Parameters(value = {
            @Parameter(name="routeId", description = "경로 번호"),
    })
    ResponseEntity<LikeBookmarkResponseDTO> getLikeBookmark(Long integratedRouteId);

    @Operation(summary="여행지를 포함한 경로 리턴", description= "특정 여행지 id를 이용해서 해당 여행지를 지나가는 경로 id 리스트를 반환받을 수 있다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공하였습니다.",  content = @Content(mediaType = "application/json")),
    })
    @Parameters(value = {
            @Parameter(name="hashtags", description = "해시태그 리스트"),
            @Parameter(name="spots", description = "여행지 리스트"),
    })
    ResponseEntity<List<Long>> getRoutesThroughSpot(List<Long> hashtags, List<Long> spots);
}
