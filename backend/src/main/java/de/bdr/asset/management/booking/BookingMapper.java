package de.bdr.asset.management.booking;

import de.bdr.asset.management.booking.dto.BookingResponseDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import de.bdr.asset.management.booking.dto.BookingCreateDTO;
import de.bdr.asset.management.booking.dto.BookingUpdateDTO;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "asset", ignore = true)
    Booking toEntity(BookingCreateDTO request);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "assetId", source = "asset.id")
    BookingResponseDTO toResponse(Booking entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "asset", ignore = true)
    void updateBookingFromDTO(BookingUpdateDTO request, @MappingTarget Booking entity);
}
