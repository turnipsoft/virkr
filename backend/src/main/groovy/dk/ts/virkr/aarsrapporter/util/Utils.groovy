package dk.ts.virkr.aarsrapporter.util

import dk.ts.virkr.cvr.integration.model.virksomhed.Navn

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Created by sorenhartvig on 19/05/2017.
 */
class Utils {

    static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd")

    static String toString(LocalDate localDate) {
        return dtf.format(localDate)
    }

    static String toString(LocalDateTime localDateTime) {
        return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(localDateTime)
    }

    static LocalDate toDate(String s) {
        return LocalDate.parse(s, dtf)
    }

    static LocalDateTime toDateTime(String s) {
        s=s.replace(" ","T")
        return LocalDateTime.parse(s, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }

    static <T extends Object> T findNyeste(List<T> liste) {

      T fundet=null
      liste.each { n->
        if (fundet==null || n.periode.gyldigTil==null ||
          (n.periode.gyldigTil!=null && fundet.periode.gyldigTil!=null && n.periode.gyldigTil>fundet.periode.gyldigTil) ){
          fundet = n
        }
      }

      return fundet
    }
}
