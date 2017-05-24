package dk.ts.virkr.aarsrapporter.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Created by sorenhartvig on 19/05/2017.
 */
class Utils {

    static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd")

    public static String toString(LocalDate localDate) {
        return dtf.format(localDate)
    }

    public static String toString(LocalDateTime localDateTime) {
        return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(localDateTime)
    }

    public static LocalDate toDate(String s) {
        return LocalDate.parse(s, dtf)
    }

    public static LocalDateTime toDateTime(String s) {
        s=s.replace(" ","T")
        return LocalDateTime.parse(s, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }
}
