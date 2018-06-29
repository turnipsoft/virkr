package dk.ts.virkr.aarsrapporter.dictionary

/**
 * Created by sorenhartvig on 29/06/2018.
 */
class OrganizedReport {
  List<XbrlElement> resultatopgoerelse = []
  List<XbrlElement> aktiver = []
  List<XbrlElement> passiver = []
  List<XbrlElement> revision = []

  Map<String, XbrlElement> xbrlMap = [:]
  Map<String, XbrlElement> fieldMap = [:]
}
