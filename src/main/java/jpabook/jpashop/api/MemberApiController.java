package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@RestController //responsbody 데이터자체를 json으로 보내자.
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/api/v1/members")
    public List<Member> memberV1(){
        return  memberService.findMembers();
    }

    @GetMapping("/api/v2/members")
    public Result memberV2(){
        List<Member> findmembers = memberService.findMembers();
        List<MemberDto> collect = findmembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());

        return new Result(collect);
        //절대 엔티티를 받거나 노출하지말고 필요한 데이터로 이루어진 dto를 만들어라!.
    }

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private  T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto{
        private String name;

    }
    
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member){
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request){
        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request){

        memberService.update(id,request.getName()); //로직의 분리를 위해 쿼리와 트랜잭션을 끝내고 다시 쿼리를 날린다.
        Member findMember = memberService.findOne(id); // 유지보수에 용이하다
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    @Data //넘기거나 넘겨받는값을 가진 클래스
    static class UpdateMemberRequest{
        private String name;
    }
    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse{
        private Long id;
        private String name;
    }

    @Data
    static class CreateMemberRequest {
        @NotEmpty //api스펙에 선언해서 유지보수가 편해진다.
        private String name;
    }

    @Data
    static class CreateMemberResponse{
        private Long id;

        public CreateMemberResponse(Long id){
            this.id=id;
        }
    }

}
