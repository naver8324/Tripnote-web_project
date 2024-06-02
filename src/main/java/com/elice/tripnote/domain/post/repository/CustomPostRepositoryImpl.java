package com.elice.tripnote.domain.post.repository;


import com.elice.tripnote.domain.link.bookmark.entity.QBookmark;
import com.elice.tripnote.domain.link.likePost.entity.QLikePost;
import com.elice.tripnote.domain.link.reportPost.entity.QReportPost;
import com.elice.tripnote.domain.member.entity.QMember;
import com.elice.tripnote.domain.post.entity.PostDetailResponseDTO;
import com.elice.tripnote.domain.post.entity.PostResponseDTO;
import com.elice.tripnote.domain.post.entity.QPost;
import com.elice.tripnote.domain.route.entity.LikeBookmarkResponseDTO;
import com.elice.tripnote.domain.route.entity.QRoute;
import com.elice.tripnote.domain.route.status.RouteStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository {

    private final JPAQueryFactory query;


    private final QMember member = QMember.member;


    private final QPost post = QPost.post;
    private final QRoute route = QRoute.route;

    private final QLikePost likePost = QLikePost.likePost;
    private final QReportPost reportPost = QReportPost.reportPost;

    private final QBookmark bookmark = QBookmark.bookmark;


    public Page<PostResponseDTO> customFindNotDeletedPosts(int page, int size){

        page = page > 0 ? page - 1 : 0;

        long totalCount = query
                .select(post.count())
                .from(post)
                .where(post.isDeleted.isFalse())
                .fetchFirst();


        List<PostResponseDTO> postResponseDTOs = query
                .select(Projections.constructor(PostResponseDTO.class,
                        post.id,
                        post.title,
                        post.content,
                        post.isDeleted
                ))
                .from(post)
                .where(post.isDeleted.isFalse())
                .orderBy(post.id.desc())
                .offset(page * size)
                .limit(size)
                .fetch();

        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(postResponseDTOs, pageRequest, totalCount);
    }

    public Page<PostResponseDTO> customFindPosts(int page, int size){

        page = page > 0 ? page - 1 : 0;

        long totalCount =query
                .select(post.count())
                .from(post)
                .fetchFirst();


        List<PostResponseDTO> postResponseDTOs = query
                .select(Projections.constructor(PostResponseDTO.class,
                        post.id,
                        post.title,
                        post.content,
                        post.isDeleted
                ))
                .from(post)
                .orderBy(post.id.desc())
                .offset(page * size)
                .limit(size)
                .fetch();

        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(postResponseDTOs, pageRequest, totalCount);
    }


    public Page<PostResponseDTO> customFindNotDeletedPostsByMemberId(Long memberId, int page, int size){

        page = page > 0 ? page - 1 : 0;

        long totalCount = query
                .select(post.count())
                .from(post)
                .where(post.member.id.eq(memberId)
                        .and(post.isDeleted.isFalse()))
                .fetchFirst();


        List<PostResponseDTO> postResponseDTOs = query
                .select(Projections.constructor(PostResponseDTO.class,
                        post.id,
                        post.title,
                        post.content,
                        post.isDeleted
                ))
                .from(post)
                .where(post.member.id.eq(memberId)
                        .and(post.isDeleted.isFalse()))
                .orderBy(post.id.desc())
                .offset(page * size)
                .limit(size)
                .fetch();

        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(postResponseDTOs, pageRequest, totalCount);
    }

    public Page<PostResponseDTO> customFindNotDeletedPostsWithLikesByMemberId(Long memberId, int page, int size){

        page = page > 0 ? page - 1 : 0;

        long totalCount = query
                .select(post.count())
                .from(post)
                .join(post.likePosts, likePost)
                .on(likePost.likedAt.isNotNull())
                .join(likePost.member, member)
                .on(member.id.eq(memberId))
                .where(post.isDeleted.isFalse())
                .fetchFirst();

        List<PostResponseDTO> postResponseDTOs = query
                .select(Projections.constructor(PostResponseDTO.class,
                        post.id,
                        post.title,
                        post.content,
                        post.isDeleted
                ))
                .from(post)
                .join(post.likePosts, likePost)
                .on(likePost.likedAt.isNotNull())
                .join(likePost.member, member)
                .on(member.id.eq(memberId))
                .where(post.isDeleted.isFalse())
                .orderBy(post.id.desc())
                .offset(page * size)
                .limit(size)
                .fetch();

        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(postResponseDTOs, pageRequest, totalCount);

    }


    public Page<PostResponseDTO> customFindNotDeletedPostsWithMarkByMemberId(Long memberId, int page, int size){

        page = page > 0 ? page - 1 : 0;

        long totalCount = query
                .select(post.count())
                .from(post)
                .join(post.bookmarks, bookmark)
                .on(bookmark.markedAt.isNotNull())
                .join(bookmark.member, member)
                .on(member.id.eq(memberId))
                .where(post.isDeleted.isFalse())
                .fetch().size();

        List<PostResponseDTO> postResponseDTOs = query
                .select(Projections.constructor(PostResponseDTO.class,
                        post.id,
                        post.title,
                        post.content,
                        post.isDeleted
                ))
                .from(post)
                .join(post.bookmarks, bookmark)
                .on(bookmark.markedAt.isNotNull())
                .join(bookmark.member, member)
                .on(member.id.eq(memberId))
                .where(post.isDeleted.isFalse())
                .orderBy(post.id.desc())
                .offset(page * size)
                .limit(size)
                .fetch();

        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(postResponseDTOs, pageRequest, totalCount);

    }


    public PostDetailResponseDTO customFindPost(Long postId){

        return query
                .select(Projections.constructor(PostDetailResponseDTO.class,
                        post.id,
                        post.title,
                        post.content,
                        post.likes,
                        post.report,
                        post.isDeleted,
                        likePost.likedAt,
                        reportPost.reportedAt,
                        route.id
                ))
                .from(post)
                .join(post.route, route)
                .leftJoin(post.likePosts, likePost)
                .leftJoin(post.reportPosts, reportPost)
                .where(post.id.eq(postId)
                        .and(post.isDeleted.isFalse()))
                .fetchOne();

    }

    public boolean customCheckIfRouteHasPost(Long routeId){

        return query
                .select(post.count())
                .from(post)
                .join(post.route, route)
                .where(route.id.eq(routeId)
                        .and(route.routeStatus.eq(RouteStatus.PUBLIC))
                )
                .fetchOne() != null;


    }


    public boolean customCheckIfRouteIsAvailable(Long routeId, Long memberId){

        return query
                .select(Expressions.ONE)
                .from(route)
                .leftJoin(post)
                .on(post.route.id.eq(route.id))
                .where( route.id.eq(routeId)
                        .and(post.id.isNull())
                        .and(route.member.id.eq(memberId))
                        .and(route.routeStatus.eq(RouteStatus.PUBLIC)))
                .fetchFirst() != null;

    }

    public int getLikeCount(Long integratedRouteId){
        return query
                .select(post.likes.sum())
                .from(post)
                .join(route).on(post.route.id.eq(route.id)
                        .and(route.integratedRoute.id.eq(integratedRouteId)))
                .fetchOne();
    }
    
}
