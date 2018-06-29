package dk.ts.virkr.aarsrapporter.dictionary

/**
 * Created by sorenhartvig on 29/06/2018.
 */
class XbrlDictionary {
  static Map<String, XbrlElement> xbrlElementMap = [:]
  static Map<String, XbrlElement> fieldMap = [:]

  static boolean locked = false

  static void lock() {
    locked = true
  }

  static void addElement(XbrlElement xbrlElement) {
    if (locked) return;

    if (!fieldMap.containsKey(xbrlElement.translatedDanish)) {
      fieldMap.put(xbrlElement.translatedDanish, xbrlElement)
    }

    xbrlElement.xbrlElementName.each { it->
      if (!xbrlElementMap.containsKey(it)) {
        xbrlElementMap.put(it, xbrlElement)
      }
    }

  }

  static OrganizedReport getOrganizedReport() {
    OrganizedReport organizedReport = new OrganizedReport()
    organizedReport.resultatopgoerelse = fieldMap.values().findAll { it.placement == 'resultatopgoerelse' }
    organizedReport.aktiver = fieldMap.values().findAll { it.placement == 'aktiver' }
    organizedReport.passiver = fieldMap.values().findAll { it.placement == 'passiver' }
    organizedReport.revision = fieldMap.values().findAll { it.placement == 'revision' }
    organizedReport.fieldMap = fieldMap
    organizedReport.xbrlMap = xbrlElementMap

    return organizedReport
  }


}
