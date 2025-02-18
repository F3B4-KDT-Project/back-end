package kdt.web_ide.schedules.dto.request;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import kdt.web_ide.members.entity.Member;
import kdt.web_ide.schedules.entity.Schedule;
import kdt.web_ide.schedules.entity.ScheduleMember;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ScheduleUpdateRequestDto {

  @NotBlank(message = "일정 제목은 필수입니다.")
  private String title;

  @NotNull(message = "시작 시간은 필수입니다.")
  private ZonedDateTime startAt;

  @NotNull(message = "종료 시간은 필수입니다.")
  private ZonedDateTime endAt;

  @NotBlank(message = "장소 이름은 필수입니다.")
  private String locationName;

  @NotNull(message = "장소 ID는 필수입니다.")
  private String locationId;

  @NotBlank(message = "주소는 필수입니다.")
  private String address;

  @NotNull(message = "위도는 필수입니다.")
  private Double latitude;

  @NotNull(message = "경도는 필수입니다.")
  private Double longitude;

  @NotEmpty(message = "참여자 목록은 필수입니다.")
  @Size(min = 1, message = "적어도 한 명의 참여자가 필요합니다.")
  private List<MemberDto> members;

  @Getter
  @Setter
  @NoArgsConstructor
  public static class MemberDto {
    @NotNull(message = "식별 코드는 필수입니다.")
    private String identificationCode;
  }

  public void applyChangesToSchedule(Schedule schedule) {
    schedule.update(
        this.title,
        this.startAt,
        this.endAt,
        this.locationName,
        this.locationId,
        this.address,
        this.latitude,
        this.longitude);
  }

  public List<String> getIdentificationCodes() {
    return members.stream().map(MemberDto::getIdentificationCode).collect(Collectors.toList());
  }

  public List<ScheduleMember> toMemberList(Schedule schedule, List<Member> memberList) {
    return memberList.stream()
        .map(member -> ScheduleMember.builder().schedule(schedule).member(member).build())
        .toList();
  }
}
