package de.bdr.asset.management.booking;

import de.bdr.asset.management.booking.dto.BookingResponseDTO;
import org.mapstruct.*;

import de.bdr.asset.management.booking.dto.BookingCreateDTO;
import de.bdr.asset.management.booking.dto.BookingUpdateDTO;

@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true)
)
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
