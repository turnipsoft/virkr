package dk.ts.virkr.aarsrapporter.dictionary

/**
 * Created by sorenhartvig on 29/06/2018.
 */
class XbrlElement {
  List<String> xbrlElementName
  String translatedDanish
  String placement

  String getXbrlElementNames() {
    return xbrlElementName.join(",")
  }

  XbrlElement(String xbrlElementName, String translation, String placement) {
    this.xbrlElementName = []
    this.xbrlElementName << xbrlElementName
    this.translatedDanish = translation
    this.placement = placement
  }

  XbrlElement(List<String> xbrlElementNames, String translation, String placement) {
    this.xbrlElementName = xbrlElementNames
    this.translatedDanish = translation
    this.placement = placement
  }

  void addXbrlElement(String xbrlElement) {
    if (this.xbrlElementName == null) {
      this.xbrlElementName = []
    }

    this.xbrlElementName << xbrlElement
  }
}
