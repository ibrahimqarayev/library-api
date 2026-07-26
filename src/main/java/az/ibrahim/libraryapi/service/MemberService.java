package az.ibrahim.libraryapi.service;

import az.ibrahim.libraryapi.dto.member.CreateMemberRequest;
import az.ibrahim.libraryapi.dto.member.MemberResponse;
import az.ibrahim.libraryapi.dto.member.UpdateMemberRequest;
import az.ibrahim.libraryapi.dto.pagination.PageResponse;
import az.ibrahim.libraryapi.entity.Member;
import az.ibrahim.libraryapi.exception.MemberNotFoundException;
import az.ibrahim.libraryapi.mapper.MemberMapper;
import az.ibrahim.libraryapi.mapper.PageMapper;
import az.ibrahim.libraryapi.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final PageMapper pageMapper;

    @Transactional
    public MemberResponse create(CreateMemberRequest request) {
        Member member = memberMapper.toEntity(request);
        Member savedMember = memberRepository.save(member);
        return memberMapper.toResponse(savedMember);
    }

    public PageResponse<MemberResponse> getAll(
            int page,
            int size,
            String sortBy,
            String sortDirection) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Member> memberPage = memberRepository.findAll(pageable);
        return pageMapper.toPageResponse(memberPage, memberMapper::toResponse);
    }

    public MemberResponse getById(Long id) {
        Member member = findMemberById(id);
        return memberMapper.toResponse(member);
    }

    @Transactional
    public MemberResponse update(Long id, UpdateMemberRequest request) {
        Member member = findMemberById(id);

        member.setName(request.getName());
        member.setEmail(request.getEmail());
        member.setMembershipDate(request.getMembershipDate());

        Member updatedMember = memberRepository.save(member);
        return memberMapper.toResponse(updatedMember);
    }

    @Transactional
    public void delete(Long id) {
        Member member = findMemberById(id);
        memberRepository.delete(member);
    }

    private Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + id));
    }
}
