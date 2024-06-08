package com.elice.tripnote.domain.route.controller;

import com.elice.tripnote.domain.route.entity.*;
import com.elice.tripnote.domain.route.service.RouteService;
import com.elice.tripnote.domain.spot.constant.Region;
import com.elice.tripnote.global.annotation.MemberRole;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RouteController implements SwaggerRouteController {
    private final RouteService routeService;

    /**
     * 경로 생성
     *
     * @param requestDto 총 경비, 경로 이름, 여행지 id 리스트(NotEmpty), 해시태그 id 리스트
     * @return 생성된 경로 id
     */
    @Override
    @MemberRole
    @PostMapping("/member/routes")
    public ResponseEntity<Long> save(@RequestBody SaveRequestDTO requestDto) {
        return ResponseEntity.ok(routeService.save(requestDto));
    }


    /**
     * 경로 공개/비공개
     *
     * @return 공개 여부를 변경하려는 경로 id
     */
    @Override
    @MemberRole
    @PatchMapping("/member/routes/status/{routeId}")
    public ResponseEntity<Long> setRouteStatus(@PathVariable("routeId") Long routeId) {
        return ResponseEntity.ok(routeService.setRouteToStatus(routeId));
    }

    /**
     * 경로 삭제
     *
     * @return '삭제 상태'로 변경된 경로 id
     */
    @Override
    @MemberRole
    @DeleteMapping("/member/routes/{routeId}")
    public ResponseEntity<Long> deleteRoute(@PathVariable("routeId") Long routeId) {
        return ResponseEntity.ok(routeService.deleteRoute(routeId));
    }

    /**
     * 특정 지역 내에서 여행하는 경로(지역 기반 경로 추천) (회원)
     * 지역에 맞는 경로 알아내기
     *
     * @param region
     * @return 해당하는 경로들의 id 리스트
     */
    @Override
    @MemberRole
    @GetMapping("/member/routes/region")
    public ResponseEntity<List<RecommendedRouteResponseDTO>> getRegion(@RequestParam("region") String region/*,
                                                                       @RequestParam(value = "hashtags", required = false) List<Long> hashtags*/) {
        //TODO: 경로에 해시태그 안붙이기
//        if (hashtags == null) hashtags = Collections.emptyList();
        Region status = Region.englishToRegion(region);
        return ResponseEntity.ok(routeService.getRegionMember(status));
        /*
        아래 값 5개
        {
           route id
          여행지 리스트 - 순서 정리된 채로, (id, region 필요 없음)
          likes: (해당 경로의 좋아요 개수)
          likedAt(경로 조회한 유저가 좋아요 눌렀는지)
          markedAt(경로 조회한 유저가 북마크를 눌렀는지)
        }

         */
    }

    /**
     * 특정 지역 내에서 여행하는 경로(지역 기반 경로 추천) (비회원)
     */
    @Override
    @GetMapping("/guest/routes/region")
    public ResponseEntity<List<RecommendedRouteResponseDTO>> getRegionGuest(@RequestParam("region") String region) {
        Region status = Region.englishToRegion(region);
        return ResponseEntity.ok(routeService.getRegionGuest(status));

    }

    // 여행지 선택했을 때, 해당 여행지를 지나가는 경로 id 리턴

    /**
     * 특정 여행지를 지나가는 경로(여행지 기반 경로 추천) (회원)
     *
     * @param spots
     * @return
     */
    @Override
    @MemberRole
    @GetMapping("/member/routes/spot")
    public ResponseEntity<List<RecommendedRouteResponseDTO>> getRoutesThroughSpot(@RequestParam(value = "spots", required = false) List<Long> spots) {
        return ResponseEntity.ok(routeService.getRoutesThroughSpotMember(spots));
        /*
        아래 값 5개
        {
           route id
          여행지 리스트 - 순서 정리된 채로, (id, region 필요 없음)
          likes: (해당 경로의 좋아요 개수)
          likedAt(경로 조회한 유저가 좋아요 눌렀는지)
          markedAt(경로 조회한 유저가 북마크를 눌렀는지)
        }

         */
    }

    /**
     * 특정 여행지를 지나가는 경로(여행지 기반 경로 추천) 비회원)
     */
    @Override
    @GetMapping("/guest/routes/spot")
    public ResponseEntity<List<RecommendedRouteResponseDTO>> getRoutesThroughSpotGuest(
            @RequestParam(value = "spots", required = false) List<Long> spots) {
        return ResponseEntity.ok(routeService.getRoutesThroughSpotGuest(spots));
    }

    /**
     * 특정 경로의 여행지 리스트 반환
     * 게시글에서 경로 보여줄 때 사용?? -> 통합경로가 아닌, route id 입력받기
     *
     * @param routeId 여행지 리스트가 궁금한 경로의 id
     * @return 특정 경로의 여행지 리스트
     */
    @Override
    @GetMapping("/member/routes/{routeId}/spots")
    public ResponseEntity<List<SpotResponseDTO>> getSpots(@PathVariable("routeId") Long routeId) {
        return ResponseEntity.ok(routeService.getSpots(routeId));
    /*
    {
      spots: [스팟 이름, 경도 , 위도]
    }

     */
    }


    /**
     * 좋아요 추가/취소
     *
     * @param integratedId 좋아요하고 싶은 경로 ic
     */
    @MemberRole
    @PatchMapping("/member/routes/like/{routeId}")
    public ResponseEntity<Void> addOrRemoveLike(@PathVariable("routeId") Long integratedId) {
        routeService.addOrRemoveLike(integratedId);
        return ResponseEntity.ok().build();
    }


    /**
     * 북마크 추가/취소
     *
     * @param integratedId 북마크하고 싶은 경로 id
     */
    @MemberRole
    @PatchMapping("/member/routes/bookmark/{routeId}")
    public ResponseEntity<Void> addOrRemoveBookmark(@PathVariable("routeId") Long integratedId) {
        routeService.addOrRemoveBookmark(integratedId);
        return ResponseEntity.ok().build();
    }


    /**
     * 자신이 좋아요한 경로 리스트
     * @return [경로 id, 경로 이름, 해당되는 경로의 여행지 리스트] 리스트 리턴
     */
    //TODO: 필요없음 삭제하기
//    @MemberRole
//    @GetMapping("/member/routes/like")
//    public ResponseEntity<List<RouteDetailResponseDTO>> findLike() {
//        return ResponseEntity.ok(routeService.findLike());
//        /*
//        route {
//            routeId:
//            name : string
//            spots : [
//            spot,spot,spot (순서 정리된채로)
//            ]
//        }
//         */
//    }

    /**
     * 자신이 북마크한 경로 리스트
     *
     * @return [경로 id, 경로 이름, 해당되는 경로의 여행지 리스트] 리스트 리턴
     */
    @MemberRole
    @GetMapping("/member/routes/bookmark")
    public ResponseEntity<Page<RouteDetailResponseDTO>> findBookmark(
            @PageableDefault(page = 0, size = 3, sort = "route.id", direction = Sort.Direction.ASC) Pageable pageable) {
        //pageable 사용법
        //request param으로 page, size 조절 가능
        return ResponseEntity.ok(routeService.findBookmark(pageable));
        /*
        route {
            routeId:
            name : string
            spots : [
            spot,spot,spot (순서 정리된채로)
            ]
        }
         */
    }

    /**
     * 자신이 생성한 경로 리스트
     *
     * @return [경로 id, 경로 이름, 해당되는 경로의 여행지 리스트] 리스트 리턴
     */
    @MemberRole
    @GetMapping("/member/routes")
    public ResponseEntity<Page<RouteDetailResponseDTO>> findMyRoute(
            @PageableDefault(page = 0, size = 3, sort = "route.id", direction = Sort.Direction.ASC) Pageable pageable) {
        //pageable 사용법
        //request param으로 page, size 조절 가능
        /*
        route {
            routeId:
            name : string
            spots : [
            spot,spot,spot (순서 정리된채로)
            ]
        }
         */
        // 경로 이름 때문에 여기서 통합 경로 리턴하는 건 안될듯
        return ResponseEntity.ok(routeService.findMyRoute(pageable));
    }


    /**
     * 경로 이름 수정
     *
     * @param requestDto 경로 id, 새로운 경로 이름
     */
    @MemberRole
    @PatchMapping("/member/routes/name")
    public ResponseEntity<Void> updateName(@RequestBody UpdateRouteNameRequestDTO requestDto) {
        routeService.updateName(requestDto);
        return ResponseEntity.ok().build();
    }
}
