package kdt.web_ide.common.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "ACCOUNT-001", "사용자를 찾을 수 없습니다."),
  HAS_ID(HttpStatus.CONFLICT, "ACCOUNT-002", "존재하는 아이디입니다."),
  INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "ACCOUNT-003", "비밀번호가 일치하지 않습니다."),
  DUPLICATE_NAME(HttpStatus.CONFLICT, "ACCOUNT-004", "중복된 닉네임입니다."),

  PASSWORD_NOT_MATCHED(HttpStatus.BAD_REQUEST, "ACCOUNT-005", "비밀번호가 일치하지 않습니다."),
  INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "ACCOUNT-006", "유효하지 않은 토큰입니다."),
  TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "ACCOUNT-007", "토큰이 만료되었습니다."),
  LOGINDID_NOT_FOUND(HttpStatus.NOT_FOUND, "ACCOUNT-008", "존재하지 않는 회원 아이디입니다."),
  INVALID_LOGINID(HttpStatus.NOT_FOUND, "ACCOUNT-009", "유효하지 않은 아이디입니다."),
  INVALID_IMAGE(HttpStatus.BAD_REQUEST, "ACCOUNT-010", "유효하지 않은 이미지입니다."),
  INVALID_NICKNAME(HttpStatus.BAD_REQUEST, "ACCOUNT-011", "유효하지 않은 이미지입니다."),
  KAKAO_REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "ACCOUNT-0012", "유효하지 않은 토큰입니다."),
  INVALID_KAKAO_ID(HttpStatus.BAD_REQUEST, "ACCOUNT-013", "유효하지 않은 카카오 아이디입니다."),

  // 채팅방
  CHATROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "ACCOUNT-008", "채팅방을 찾을 수 없습니다."),

  // 게시판
  MEMBER_ALREADY_IN_BOARD(HttpStatus.BAD_REQUEST, "BOARD-001", "이미 게시판에 초대된 멤버입니다."),
  NO_PERMISSION(HttpStatus.UNAUTHORIZED, "BOARD-002", "권한이 없습니다."),
  BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "BOARD-003", "존재하지 않는 게시판입니다."),
  MEMBER_NOT_IN_BOARD(HttpStatus.NOT_FOUND, "BOARD-004", "게시판에 존재하지 않는 멤버입니다."),
  INVALID_TITLE(HttpStatus.BAD_REQUEST, "BOARD-005", "제목 입력은 필수입니다."),
  ALREADY_IN_BOARD(HttpStatus.BAD_REQUEST, "BOARD-006", "이미 게시판에 존재합니다."),

  // S3
  S3_UPLOAD_ERROR(HttpStatus.BAD_REQUEST, "S3-001", "S3 업로드 실패"),
  FILE_EXECUTION_ERROR(HttpStatus.BAD_REQUEST, "S3-002", "파일 실행 실패"),
  FILE_EXTENSION_ERROR(HttpStatus.BAD_REQUEST, "S3-003", "파일 확장자가 올바르지 않습니다."),
  S3_FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "S3-004", "S3 파일을 찾을 수 없습니다."),
  S3_ACCESS_ERROR(HttpStatus.BAD_REQUEST, "S3-005", "S3 접근 권한이 없습니다."),
  S3_DELETE_ERROR(HttpStatus.BAD_REQUEST, "S3-006", "S3 삭제 실패"),

  // 게시글
  POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST-001", "게시글을 찾을 수 없습니다."),
  JSON_PROCESSING_ERROR(HttpStatus.BAD_REQUEST, "POST-002", "JSON 파싱 에러"),

  // 일정
  SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "SCHEDULE-001", "일정을 찾을 수 없습니다."),
  SCHEDULE_ACCESS_ERROR(HttpStatus.UNAUTHORIZED, "SCHEDULE-002", "권한이 없습니다."),
  MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "SCHEDULE-003", "일정에 참여하지 않는 멤버입니다."),
  MEMBER_ALREADY_IN_SCHEDULE(HttpStatus.BAD_REQUEST, "SCHEDULE-004", "일정에 이미 참여하는 멤버입니다."),
  INVALID_SCHEDULE_FORMAT(HttpStatus.BAD_REQUEST, "SCHEDULE-005", "일정은 5분 단위로 등록해야 합니다."),
  DATETIME_ERROR(HttpStatus.BAD_REQUEST, "SCHEDULE-006", "시작 시간은 종료 시간보다 이전이어야 합니다.");

  private final HttpStatus httpStatus; // HttpStatus
  private final String code; // ACCOUNT-001
  private final String message; // 설명
}
