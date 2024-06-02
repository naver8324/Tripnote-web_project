package com.elice.tripnote.domain.comment.repository;


import com.elice.tripnote.domain.comment.entity.Comment;
import com.elice.tripnote.domain.comment.entity.CommentResponseDTO;
import com.elice.tripnote.domain.comment.entity.QComment;
import com.elice.tripnote.domain.post.entity.QPost;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomCommentRepositoryImpl implements CustomCommentRepository{

    private final EntityManager em;
    private final JPAQueryFactory query;


    private final QComment comment = QComment.comment;


    public Page<CommentResponseDTO> customFindNotDeletedCommentsByPostId(Long postId, int page, int size){

        page = page > 0 ? page - 1 : 0;

        Long totalCount =(long) query
                .from(comment)
                .where(comment.post.id.eq(postId)
                        .and(comment.isDeleted.isFalse()))
                .fetch().size();


        List<CommentResponseDTO> commentResponseDTOs = query
                .select(Projections.constructor(CommentResponseDTO.class,
                                comment.id,
                                comment.content,
                                comment.report,
                                comment.isDeleted
                ))
                .from(comment)
                .where(comment.post.id.eq(postId)
                        .and(comment.isDeleted.isFalse()))
                .orderBy(comment.id.desc())
                .offset(page * size)
                .limit(size)
                .fetch();

        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(commentResponseDTOs, pageRequest, totalCount);
    }


    public Page<CommentResponseDTO> customFindComments(int page, int size){

        page = page > 0 ? page - 1 : 0;

        Long totalCount =(long) query
                .from(comment)
                .fetch().size();


        List<CommentResponseDTO> commentResponseDTOs = query
                .select(Projections.constructor(CommentResponseDTO.class,
                        comment.id,
                        comment.content,
                        comment.report,
                        comment.isDeleted
                ))
                .from(comment)
                .orderBy(comment.id.desc())
                .offset(page * size)
                .limit(size)
                .fetch();

        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(commentResponseDTOs, pageRequest, totalCount);
    }


    public Page<CommentResponseDTO> customFindCommentsByMemberId(Long memberId, int page, int size){

        page = page > 0 ? page - 1 : 0;

        Long totalCount = (long) query
                .from(comment)
                .where(comment.member.id.eq(memberId))
                .fetch().size();


        List<CommentResponseDTO> commentResponseDTOs = query
                .select(Projections.constructor(CommentResponseDTO.class,
                        comment.id,
                        comment.content,
                        comment.report,
                        comment.isDeleted
                ))
                .from(comment)
                .where(comment.member.id.eq(memberId))
                .orderBy(comment.id.desc())
                .offset(page * size)
                .limit(size)
                .fetch();

        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(commentResponseDTOs, pageRequest, totalCount);
    }


    public void customDeleteCommentsByPostId(Long postId){


        Long count = query
                .update(comment)
                .set(comment.isDeleted, true)
                .where(comment.post.id.eq(postId))
                .execute();
        em.clear();
        em.flush();


    }



}
