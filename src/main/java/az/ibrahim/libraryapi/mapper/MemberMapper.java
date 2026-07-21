package az.ibrahim.libraryapi.mapper;

import az.ibrahim.libraryapi.dto.member.CreateMemberRequest;
import az.ibrahim.libraryapi.dto.member.MemberResponse;
import az.ibrahim.libraryapi.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MemberMapper {

    Member toEntity(CreateMemberRequest request);

    MemberResponse toResponse(Member member);
}
