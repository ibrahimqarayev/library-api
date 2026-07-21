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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberMapper memberMapper;

    @Mock
    private PageMapper pageMapper;

    @InjectMocks
    private MemberService memberService;

    @Test
    void shouldCreateMember() {
        CreateMemberRequest request = new CreateMemberRequest();
        Member member = new Member();
        Member savedMember = new Member();
        MemberResponse response = new MemberResponse();

        when(memberMapper.toEntity(request)).thenReturn(member);
        when(memberRepository.save(member)).thenReturn(savedMember);
        when(memberMapper.toResponse(savedMember)).thenReturn(response);

        MemberResponse result = memberService.create(request);

        assertEquals(response, result);

        verify(memberMapper).toEntity(request);
        verify(memberRepository).save(member);
        verify(memberMapper).toResponse(savedMember);
    }

    @Test
    void shouldReturnPagedMembers() {
        int page = 0;
        int size = 10;
        String sortBy = "id";
        String sortDirection = "asc";

        Page<Member> memberPage = mock(Page.class);

        PageResponse<MemberResponse> pageResponse = new PageResponse<>(List.of(), page, size, 0L, 0, true);

        when(memberRepository.findAll(any(Pageable.class))).thenReturn(memberPage);

        when(pageMapper.toPageResponse(eq(memberPage), ArgumentMatchers.<Function<Member, MemberResponse>>any()))
                .thenReturn(pageResponse);

        PageResponse<MemberResponse> result = memberService.getAll(page, size, sortBy, sortDirection);

        assertEquals(pageResponse, result);

        verify(memberRepository).findAll(any(Pageable.class));

        verify(pageMapper).toPageResponse(eq(memberPage), ArgumentMatchers.<Function<Member, MemberResponse>>any());
    }

    @Test
    void shouldReturnMemberById() {
        Long memberId = 1L;

        Member member = new Member();
        MemberResponse response = new MemberResponse();

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        when(memberMapper.toResponse(member)).thenReturn(response);

        MemberResponse result = memberService.getById(memberId);

        assertEquals(response, result);

        verify(memberRepository).findById(memberId);
        verify(memberMapper).toResponse(member);
    }

    @Test
    void shouldThrowExceptionWhenMemberNotFound() {
        Long memberId = 1L;

        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> memberService.getById(memberId));

        verify(memberRepository).findById(memberId);
        verify(memberMapper, never()).toResponse(any());
    }

    @Test
    void shouldUpdateMember() {
        Long memberId = 1L;

        UpdateMemberRequest request = new UpdateMemberRequest();
        request.setName("Updated Name");
        request.setEmail("updated@email.com");
        request.setMembershipDate(LocalDate.now());

        Member member = new Member();
        MemberResponse response = new MemberResponse();

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        when(memberRepository.save(member)).thenReturn(member);

        when(memberMapper.toResponse(member)).thenReturn(response);

        MemberResponse result = memberService.update(memberId, request);

        assertEquals(response, result);
        assertEquals(request.getName(), member.getName());
        assertEquals(request.getEmail(), member.getEmail());
        assertEquals(request.getMembershipDate(), member.getMembershipDate());

        verify(memberRepository).findById(memberId);
        verify(memberRepository).save(member);
        verify(memberMapper).toResponse(member);
    }

    @Test
    void shouldDeleteMember() {
        Long memberId = 1L;

        Member member = new Member();

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        memberService.delete(memberId);

        verify(memberRepository).findById(memberId);
        verify(memberRepository).delete(member);
    }
}