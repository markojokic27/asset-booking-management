package constants;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TestDates {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ISO_LOCAL_DATE;

    public static String futureDateFrom() {
        LocalDate today = LocalDate.now();
        LocalDate from = today.withDayOfMonth(
                Math.max(today.getDayOfMonth(), today.lengthOfMonth() - 2));
        return from.format(FMT);
    }

    public static String futureDateTo() {
        LocalDate from = LocalDate.parse(futureDateFrom());
        LocalDate to = from.plusMonths(1).withDayOfMonth(
                Math.min(5, from.plusMonths(1).lengthOfMonth()));
        return to.format(FMT);
    }

    public static String parkingTestDate() {
        return LocalDate.parse(futureDateFrom()).plusDays(1).format(FMT);
    }

    public static String smokeDateFrom() {
        return LocalDate.parse(futureDateTo()).plusDays(1).format(FMT);
    }

    public static String smokeDateTo() {
        return LocalDate.parse(smokeDateFrom()).plusDays(15).format(FMT);
    }

    public static String parkingSmoke() {
        return LocalDate.now().plusDays(84).format(FMT);
    }

    public static String approvalSmokeFrom() {
        return LocalDate.now().plusDays(68).format(FMT);
    }

    public static String approvalSmokeTo() {
        return LocalDate.parse(approvalSmokeFrom()).plusDays(11).format(FMT);
    }
}