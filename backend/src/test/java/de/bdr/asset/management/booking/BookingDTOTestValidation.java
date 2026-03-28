package de.bdr.asset.management.booking;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class BookingDTOTestValidation {

    private static Validator validator;

    @BeforeAll
    static void setValidator(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator=factory.getValidator();
    }

    private BookingDTO validDTO(){;
        return  new BookingDTO(
                1L,
                2L,
                1L,
                BookingStatusEnum.ACTIVE,
                LocalDateTime.of(2026, 4, 1, 9, 0).toInstant(ZoneOffset.UTC),
                LocalDateTime.of(2026, 4, 14, 9, 0).toInstant(ZoneOffset.UTC),
                "text"
        );
    }

    private Set<ConstraintViolation<BookingDTO>> violationsFor(String field, BookingDTO dto){
        return validator.validateProperty(dto, field);
    }

    // All valid fields should produce no validation errors
    @Test
    void validDTO_shouldHaveNoViolations(){
        assertThat(validator.validate(validDTO())).isEmpty();
    }

    //userId

    //UserId is null
    @Test
    void nullUserId_shouldFailNotNull(){
        BookingDTO dto=new BookingDTO(1L, null, 1L, BookingStatusEnum.ACTIVE, LocalDateTime.of(2026, 4, 1, 9, 0).toInstant(ZoneOffset.UTC), LocalDateTime.of(2026, 4, 14, 9, 0).toInstant(ZoneOffset.UTC), "text");
        assertThat(validator.validate(dto)).anyMatch(v->v.getPropertyPath().toString().equals("userId"));
    }

    //asserId

    //AssetId is null

    @Test
    void nullAssetId_shouldFailNotNull(){
        BookingDTO dto=new BookingDTO(1L, 2L, null, BookingStatusEnum.ACTIVE, LocalDateTime.of(2026, 4, 1, 9, 0).toInstant(ZoneOffset.UTC), LocalDateTime.of(2026, 4, 14, 9, 0).toInstant(ZoneOffset.UTC), "text");
        assertThat(validator.validate(dto)).anyMatch(v->v.getPropertyPath().toString().equals("assetId"));
    }

    //status

    //Status is null

    @Test
    void nullStatus_shouldFailNotNull(){
        BookingDTO dto=new BookingDTO(1L, 2L, 1L, null, LocalDateTime.of(2026, 4, 1, 9, 0).toInstant(ZoneOffset.UTC), LocalDateTime.of(2026, 4, 14, 9, 0).toInstant(ZoneOffset.UTC), "text");
        assertThat(validator.validate(dto)).anyMatch(v->v.getPropertyPath().toString().equals("status"));
    }

    //bookimgStartTime

    //BookingStartTime is null

    @Test
    void nullBookimgStartTime_shouldFailNotNull(){
        BookingDTO dto=new BookingDTO(1L, 2L, 1L, BookingStatusEnum.ACTIVE, null, LocalDateTime.of(2026, 4, 14, 9, 0).toInstant(ZoneOffset.UTC), "text");
        assertThat(validator.validate(dto)).anyMatch(v->v.getPropertyPath().toString().equals("bookingStartTime"));
    }

    //bookingEndTime

    //BookingEndTime is null

    @Test
    void nullBookimgEndTime_shouldFailNotNull(){
        BookingDTO dto=new BookingDTO(1L, 2L, 1L, BookingStatusEnum.ACTIVE, LocalDateTime.of(2026, 4, 1, 9, 0).toInstant(ZoneOffset.UTC), null, "text");
        assertThat(validator.validate(dto)).anyMatch(v->v.getPropertyPath().toString().equals("bookingEndTime"));
    }

    //notes

    //Notes is too long
    @Test
    void notesTooLong_shouldFailSize(){
        BookingDTO dto=new BookingDTO(1L, 2L, 1L, BookingStatusEnum.ACTIVE, LocalDateTime.of(2026, 4, 1, 9, 0).toInstant(ZoneOffset.UTC), LocalDateTime.of(2026, 4, 14, 9, 0).toInstant(ZoneOffset.UTC), "t".repeat(2300));
        assertThat(validator.validate(dto)).anyMatch(v -> v.getPropertyPath().toString().equals("notes"));
    }

    // Notes is null, should be allowed
    @Test
    void nullNotes_shouldBeValid() {
        BookingDTO dto=new BookingDTO(1L, 2L, 1L, BookingStatusEnum.ACTIVE, LocalDateTime.of(2026, 4, 1, 9, 0).toInstant(ZoneOffset.UTC), LocalDateTime.of(2026, 4, 14, 9, 0).toInstant(ZoneOffset.UTC), null);
        assertThat(validator.validate(dto)).noneMatch(v -> v.getPropertyPath().toString().equals("notes"));
    }

}
