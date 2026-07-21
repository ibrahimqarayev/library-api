package az.ibrahim.libraryapi.service;

import az.ibrahim.libraryapi.dto.member.CreateMemberRequest;
import az.ibrahim.libraryapi.dto.member.MemberResponse;
import az.ibrahim.libraryapi.dto.member.UpdateMemberRequest;
import az.ibrahim.libraryapi.entity.Member;
import az.ibrahim.libraryapi.exception.MemberNotFoundException;
import az.ibrahim.libraryapi.mapper.MemberMapper;
import az.ibrahim.libraryapi.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    public MemberResponse create(CreateMemberRequest request) {
        Member member = memberMapper.toEntity(request);
        Member savedMember = memberRepository.save(member);
        return memberMapper.toResponse(savedMember);
    }

    public List<MemberResponse> getAll() {
        return memberRepository.findAll()
                .stream().map(memberMapper::toResponse)
                .collect(Collectors.toList());
    }

    public MemberResponse getById(Long id) {
        Member member = findMemberById(id);
        return memberMapper.toResponse(member);
    }

    public MemberResponse update(Long id, UpdateMemberRequest request) {
        Member member = findMemberById(id);

        member.setName(request.getName());
        member.setEmail(request.getEmail());
        member.setMembershipDate(request.getMembershipDate());

        Member updatedMember = memberRepository.save(member);
        return memberMapper.toResponse(updatedMember);
    }

    public void delete(Long id) {
        Member member = findMemberById(id);
        memberRepository.delete(member);
    }

    private Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + id));
    }
}
