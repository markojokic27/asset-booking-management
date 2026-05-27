package de.bdr.asset.management.booking;

import de.bdr.asset.management.asset.Asset;
import de.bdr.asset.management.assetcategory.AssetCategory;
import de.bdr.asset.management.booking.dto.*;
import de.bdr.asset.management.user.User;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true),
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface BookingMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "asset", ignore = true)
    Booking toEntity(BookingCreateDTO request);

    BookingResponseDTO toResponse(Booking entity);

    UserSummaryDTO toUserSummary(User user);

    AssetSummaryDTO toAssetSummary(Asset asset);

    CategorySummaryDTO toCategorySummary(AssetCategory category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "asset", ignore = true)
    void updateBookingFromDTO(BookingUpdateDTO request, @MappingTarget Booking entity);
}
