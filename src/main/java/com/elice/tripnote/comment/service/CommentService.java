package com.elice.tripnote.comment.service;

import com.elice.tripnote.comment.entity.CommentRequestDTO;
import com.elice.tripnote.comment.entity.Comment;
import com.elice.tripnote.comment.entity.CommentResponseDTO;
import com.elice.tripnote.comment.repository.CommentRepository;
import com.elice.tripnote.member.entity.Member;
import com.elice.tripnote.member.repository.MemberRepository;
import com.elice.tripnote.post.exception.NoSuchAuthorizationException;
import com.elice.tripnote.post.exception.NoSuchCommentException;
import com.elice.tripnote.post.exception.NoSuchPostException;
import com.elice.tripnote.post.entity.Post;
import com.elice.tripnote.post.exception.NoSuchUserException;
import com.elice.tripnote.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;



    // 게시글에서 게시글에 해당하는 댓글을 페이지 형태로 불러올 때 사용하는 메서드. 삭제되지 않은 댓글만 불러옵니다.

    @Transactional(readOnly = true)
    public Page<CommentResponseDTO> getCommentsByPostId(Long postId, int page, int size){

        Post post = postOrElseThrowsException(postId);


        return commentRepository.findByPost_IdAndIsDeletedIsFalse(postId, PageRequest.of(page, size, Sort.by("id").descending())).map(Comment::toDTO);


    }




    // 관리자가 모든 댓글을 불러올 때 사용하는 메서드. 삭제된 댓글도 불러옵니다.
    @Transactional(readOnly = true)
    public Page<CommentResponseDTO> getCommentsAll(int page, int size){

        return commentRepository.findAll(PageRequest.of(page, size, Sort.by("id").descending())).map(Comment::toDTO);


    }

    // 관리자가 한 유저의 전체 댓글을 불러올 때 사용하는 메서드. 삭제된 댓글도 불러옵니다.
    public Page<CommentResponseDTO> getCommentsByMemberId(Long memberId, int page, int size){

        Member member = memberOrElseThrowsException(memberId);

        return commentRepository.findByMember_Id(memberId, PageRequest.of(page, size, Sort.by("id").descending())).map(Comment::toDTO);



    }


    // 댓글을 저장하는 메서드입니다.
    public CommentResponseDTO saveComment(CommentRequestDTO commentDTO, Long postId, Long memberId){

        Post post = postOrElseThrowsException(postId);
        Member member = memberOrElseThrowsException(memberId);


        Comment comment = Comment.builder()
                .content(commentDTO.getContent())
                .post(post)
                .member(member)
                .build();

        // comment를 먼저 저장하고 add를 저장해야 EntityNotFoundException이 발생하지 않는다.

        commentRepository.save(comment);
        post.getComments().add(comment);
        member.getComments().add(comment);

        return comment.toDTO();


    }


    // 댓글을 수정하는 메서드입니다.
    public CommentResponseDTO updateComment(CommentRequestDTO commentDTO, Long postId){

        Comment comment = commentOrElseThrowsException(commentDTO.getId());

        comment.updateContent(commentDTO);

        return comment.toDTO();


    }

    // 댓글의 신고수를 늘리는 메서드입니다. 유저가 신고 버튼을 누를 때 사용합니다.
    public CommentResponseDTO addReportCount(Long commentId){

        Comment comment = commentOrElseThrowsException(commentId);
        comment.addReport();

        return comment.toDTO();
    }

    // 댓글의 신고수를 줄이는 메서드입니다. 유저가 신고를 취소할 때 사용합니다.
    public CommentResponseDTO removeReportCount(Long commentId){

        Comment comment = commentOrElseThrowsException(commentId);
        comment.removeReport();

        return comment.toDTO();
    }


    // 댓글을 삭제하는 메서드입니다. 댓글을 쓴 유저가 사용합니다.
    public void deleteComment(Long commentId, Long memberId){

        Comment comment = commentOrElseThrowsException(commentId);

        if(comment.getMember().getId() == memberId){
            NoSuchAuthorizationException ex = new NoSuchAuthorizationException();
            log.error("에러 발생: {}", ex.getMessage(), ex);
            throw ex;
        }

        comment.delete();

    }

    // 댓글을 삭제하는 메서드입니다. 관리자만 사용할 수 있습니다.
    public void deleteComment(Long commentId){


        Comment comment = commentOrElseThrowsException(commentId);
        comment.delete();

    }










    //여기서부터는 service 내부에서만 사용할 수 있는 메서드입니다.


    // post id로 post를 불러 올 때 존재하면 post 객체를 반환하고 없으면 에러를 반환하는 메서드입니다.
    private Post postOrElseThrowsException(Long postId) {

        return postRepository.findById(postId)
                .orElseThrow(() -> {
                    NoSuchPostException ex = new NoSuchPostException();
                    log.error("에러 발생: {}", ex.getMessage(), ex);
                    throw ex;
                });
    }

    // comment id로 comment를 불러 올 때 존재하면 comment 객체를 반환하고 없으면 에러를 반환하는 메서드입니다.
    private Comment commentOrElseThrowsException(Long commentId) {

        return commentRepository.findById(commentId)
                .orElseThrow(() -> {
                    NoSuchCommentException ex = new NoSuchCommentException();
                    log.error("에러 발생: {}", ex.getMessage(), ex);
                    throw ex;
                });
    }

    // member id로 member를 불러 올 때 존재하면 member 객체를 반환하고 없으면 에러를 반환하는 메서드입니다.
    private Member memberOrElseThrowsException(Long memberId) {

        return memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    NoSuchUserException ex = new NoSuchUserException();
                    log.error("에러 발생: {}", ex.getMessage(), ex);
                    throw ex;
                });
    }

}
