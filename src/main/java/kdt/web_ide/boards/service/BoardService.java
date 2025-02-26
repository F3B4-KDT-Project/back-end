package kdt.web_ide.boards.service;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import kdt.web_ide.boards.dto.request.BoardSaveRequesetDto;
import kdt.web_ide.boards.dto.request.BoardUpdateRequestDto;
import kdt.web_ide.boards.dto.request.BoardUserInviteRequestDto;
import kdt.web_ide.boards.dto.response.BoardResponseDto;
import kdt.web_ide.boards.dto.response.BoardUserResponseDto;
import kdt.web_ide.boards.entity.Board;
import kdt.web_ide.boards.entity.BoardRepository;
import kdt.web_ide.boards.entity.BoardUser;
import kdt.web_ide.boards.entity.BoardUserRepository;
import kdt.web_ide.common.exception.CustomException;
import kdt.web_ide.common.exception.ErrorCode;
import kdt.web_ide.members.entity.Member;
import kdt.web_ide.members.entity.repository.MemberRepository;
import kdt.web_ide.notification.entity.Notification;
import kdt.web_ide.notification.entity.NotificationType;
import kdt.web_ide.notification.entity.repository.NotificationRepository;
import kdt.web_ide.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BoardService {

  private final BoardRepository boardRepository;
  private final BoardUserRepository boardUserRepository;
  private final MemberRepository memberRepository;
  private final NotificationService notificationService;
  private final NotificationRepository notificationRepository;

  // 게시판 생성
  @Transactional
  public BoardResponseDto saveBoard(BoardSaveRequesetDto requestDto, Member member) {
    Board createdBoard = requestDto.toEntity();
    boardRepository.save(createdBoard);
    BoardUser createdUser = requestDto.toEntity(createdBoard, member);
    boardUserRepository.save(createdUser);
    return new BoardResponseDto(createdBoard);
  }

  // 게시판 수정
  @Transactional
  public BoardResponseDto updateBoard(
      BoardUpdateRequestDto requestDto, Long boardId, Member member) {
    Board board =
        boardRepository
            .findById(boardId)
            .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
    // 수정 권한 확인 (리더만 수정 가능)
    BoardUser boardUser =
        boardUserRepository
            .findByMemberAndBoardAndIsLeaderTrue(member, board)
            .orElseThrow(() -> new CustomException(ErrorCode.NO_PERMISSION));
    board.update(requestDto.getTitle());
    boardRepository.save(board);
    return new BoardResponseDto(board);
  }

  // 게시판 삭제
  @Transactional
  public void deleteBoard(Long boardId, Member member) {
    Board board =
        boardRepository
            .findById(boardId)
            .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
    // 삭제 권한 확인 (리더만 삭제 가능)
    BoardUser boardUser =
        boardUserRepository
            .findByMemberAndBoardAndIsLeaderTrue(member, board)
            .orElseThrow(() -> new CustomException(ErrorCode.NO_PERMISSION));
    boardRepository.delete(board);
  }

  @Transactional
  public void inviteMember(
      BoardUserInviteRequestDto requestDto, Long boardId, Member currentMember) {
    Board board =
        boardRepository
            .findById(boardId)
            .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

    boardUserRepository
        .findByMemberAndBoardAndIsLeaderTrue(currentMember, board)
        .orElseThrow(() -> new CustomException(ErrorCode.NO_PERMISSION));

    Member memberToInvite =
        memberRepository
            .findByKakaoId(requestDto.getKakaoId())
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    boardUserRepository
        .findByMemberAndBoard(memberToInvite, board)
        .ifPresent(
            member -> {
              throw new CustomException(ErrorCode.ALREADY_IN_BOARD);
            });

    notificationService.sendNotification(
        memberToInvite, NotificationType.INVITATION, board.getTitle());
  }

  @Transactional
  public void acceptInvitation(Long notificationId, Long boardId, Member member) {
    Notification notification =
        notificationRepository
            .findById(notificationId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOTIFICATION_NOT_FOUND));

    if (!notification.getMember().getMemberId().equals(member.getMemberId())) {
      throw new CustomException(ErrorCode.NO_PERMISSION);
    }

    Board board =
        boardRepository
            .findById(boardId)
            .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

    boardUserRepository
        .findByMemberAndBoard(member, board)
        .ifPresent(
            m -> {
              throw new CustomException(ErrorCode.ALREADY_IN_BOARD);
            });

    notificationRepository.delete(notification);

    BoardUser newUser = BoardUser.builder().board(board).member(member).isLeader(false).build();
    boardUserRepository.save(newUser);

    notificationService.sendInvitationAcceptedNotification(member, board.getTitle());
  }

  @Transactional
  public void rejectInvitation(Long notificationId, Long boardId, Member member) {
    Notification notification =
        notificationRepository
            .findById(notificationId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOTIFICATION_NOT_FOUND));

    if (!notification.getMember().getMemberId().equals(member.getMemberId())) {
      throw new CustomException(ErrorCode.NO_PERMISSION);
    }

    Board board =
        boardRepository
            .findById(boardId)
            .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

    notificationRepository.delete(notification);

    notificationService.sendInvitationRejectedNotification(member, board.getTitle());
  }

  // 게시판 인원 조회
  @Transactional
  public List<BoardUserResponseDto> findBoardUsersById(Long boardId, Member member) {
    Board board =
        boardRepository
            .findById(boardId)
            .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

    List<BoardUser> boardUsers = boardUserRepository.findByBoard(board);
    return boardUsers.stream().map(BoardUserResponseDto::new).collect(Collectors.toList());
  }

  // 게시판 멤버 삭제
  @Transactional
  public void removeMember(Long boardId, Long memberId, Member currentMember) {
    Board board =
        boardRepository
            .findById(boardId)
            .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

    // 삭제 권한 확인 (리더만 가능)
    BoardUser leader =
        boardUserRepository
            .findByMemberAndBoardAndIsLeaderTrue(currentMember, board)
            .orElseThrow(() -> new CustomException(ErrorCode.NO_PERMISSION));

    // 삭제 대상 멤버 찾기
    Member memberToRemove =
        memberRepository
            .findById(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    BoardUser boardUser =
        boardUserRepository
            .findByMemberAndBoard(memberToRemove, board)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_IN_BOARD));
    boardUserRepository.delete(boardUser);
  }

  // 게시판 상세 조회
  @Transactional
  public BoardResponseDto getBoardDetails(Long boardId) {
    Board board =
        boardRepository
            .findById(boardId)
            .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
    return new BoardResponseDto(board);
  }

  // 내 게시판 목록 조회
  @Transactional
  public List<BoardResponseDto> getMyBoards(Member member) {
    List<BoardUser> boardUsers = boardUserRepository.findByMember(member);
    return boardUsers.stream()
        .map(boardUser -> new BoardResponseDto(boardUser.getBoard()))
        .collect(Collectors.toList());
  }
}
