package com.beyond.teenkiri.common.data;

import com.beyond.teenkiri.event.domain.Event;
import com.beyond.teenkiri.common.domain.Address;
import com.beyond.teenkiri.event.repository.EventRepository;
import com.beyond.teenkiri.user.domain.Role;
import com.beyond.teenkiri.user.dto.UserSaveReqDto;
import com.beyond.teenkiri.user.repository.UserRepository;
import com.beyond.teenkiri.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class InitialDataLoader implements CommandLineRunner {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventRepository eventRepository;

    @Override
    public void run(String... args) throws Exception {
        // Admin 유저 추가
        if (userRepository.findByEmail("admin@teenkiri.com").isEmpty()) {
            userService.register(UserSaveReqDto.builder()
                    .name("ADMIN")
                    .nickname("ADMIN")
                    .email("admin@teenkiri.com")
                    .password("12341234")
                    .phone("07012345678")
                    .role(Role.ADMIN)
                    .address(Address.builder()
                            .city("서울")
                            .street("동작구")
                            .zipcode("보라매로 87")
                            .build())
                    .build());
        }

        if (eventRepository.count() == 0) {
            Event event1 = Event.builder()
                    .title("이달의 그린히어로 SNS 인증 이벤트!")
                    .content("개인 SNS (인스타, 페이스북, 블로그)에 리그라운드 제품을 사용하며 환경을 지키는 그린히어로 인증 후기를 남겨주세요~!!"
                            + "<br />기간: 2024. 3. 1 ~ 이벤트 종료 공지 시까지"
                            + "<br />사은품: 스타벅스 아메리카노 Tall Size 쿠폰"
                            + "<br />당첨자 발표: 매월 5일 매월 3명 선정!!"
                            + "<br /><br />이벤트에 선정 된 분들께 개별 DM & 비밀댓글로 연락드리며, 해당 이벤트의 당첨자 공지 게시물에 업로드 됩니다."
                            + "<br /><br />=========<br /><br />"
                            + "꼭 확인해 주세요!"
                            + "<br />- 본 프로모션은 카카오페이 계정 기준 '1일 1회, 기간 내 최대 2회, 카오페이머니 결제'에 한해 할인 적용 가능합니다."
                            + "<br />- 예산 소진 시 즉시 종료되며, 결제창에서 할인 내역이 보이지 않습니다."
                            + "<br />- 즉시할인은 가맹점 및 카카오페이 포인트/쿠폰 등이 적용된 최종 결제 금액 기준으로 반영되며, 카카오페이 결제 페이지와 결제창 내 '할인' 및 상세 보기를 통해 확인 가능합니다."
                            + "<br />- 할인이 적용된 실 결제 금액은 카카오페이 결제창 하단 결제금액을 꼭 확인해 주세요."
                            + "<br />- 카카오페이머니 1일 1회 최대 결제한도는 200만원으로 200만원 이상 결제금액은 카드 결제를 이용하시길 바랍니다."
                            + "<br />- 일부 상품에는 할인이 적용되지 않습니다."
                            + "<br />- 카카오페이 결제창에서 적용 가능 할인 선택 후 적용 버튼을 눌러야 할인이 적용됩니다."
                            + "<br />- 취소 안내"
                            + "<br /><br />■ 카드 결제 취소는 영업일 기준 3~5일 정도 소요되며, 카드 결제 취소가 완료된 후 할인 한도가 원복 됩니다."
                            + "<br />■ 기존 결제 전체 취소 후, 프로모션 기간 내 신규 결제 진행 시 할인 적용됩니다."
                            + "<br />■ 기존 결제 부분 취소 후, 프로모션 기간 내 신규 결제 진행 시 할인 미적용됩니다."
                            + "<br />■ 부분 취소 및 반품(품절, 배송 지연 등 판매자 사유 포함) 시 취소금액의 비율만큼 할인 금액이 회수되며, 최종 결제 금액이 프로모션 참여 조건 금액 미만인 경우 혜택받으신 금액은 환불 금액에서 차감되어 자동 회수됩니다."
                            + "<br />- 본 프로모션은 예산 소진 시 프로모션 배너 게시 여부와 상관없이 별도 공지하지 않고 조기 종료될 수 있습니다."
                            + "<br />- 본 프로모션은 당사 및 제휴사 사정으로 변경 또는 중단될 수 있습니다."
                            + "<br />- 할인 미적용 문의: 카카오페이 고객센터 1644-7405, 카카오톡 [카카오페이 고객센터] 상담 채널")
                    .imageUrl("https://example.com/image1.jpg")
                    .build();

            eventRepository.save(event1);
        }
    }
}
