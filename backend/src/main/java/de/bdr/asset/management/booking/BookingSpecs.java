package de.bdr.asset.management.booking;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class BookingSpecs {
    public static Specification<Booking> withFilter(BookingFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getStatus() != null)
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));

            if (filter.getUserId() != null)
                predicates.add(cb.equal(root.get("user").get("id"), filter.getUserId()));

            if (filter.getAssetId() != null)
                predicates.add(cb.equal(root.get("asset").get("id"), filter.getAssetId()));

            if (filter.getCategoryId() != null)
                predicates.add(cb.equal(root.get("asset").get("category").get("id"), filter.getCategoryId()));

            if (filter.getBookingStart() != null)
                predicates.add(cb.greaterThanOrEqualTo(root.get("bookingStart"), filter.getBookingStart()));

            if (filter.getBookingEnd() != null)
                predicates.add(cb.lessThanOrEqualTo(root.get("bookingEnd"), filter.getBookingEnd()));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
