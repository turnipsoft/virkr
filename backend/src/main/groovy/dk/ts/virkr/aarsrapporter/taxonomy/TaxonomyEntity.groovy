package dk.ts.virkr.aarsrapporter.taxonomy

import dk.ts.virkr.aarsrapporter.model.Regnskabstal

/**
 * Created by shartvig2 on 2/15/18.
 */
class TaxonomyEntity {
  String ref
  String label
  String text
  String order
  Regnskabstal value
  String stringValue
  Map<String, TaxonomyEntity> children = [:]

  TaxonomyEntity(String ref, String label) {
    this.ref = ref
    this.label = label
  }

  String toString() {

    String vaerdi = this.value ? this.value.vaerdi : this.stringValue
    StringWriter sw = new StringWriter()
    if (this.children) {
      this.children.values().sort{ it.order}.each {
        if (it.value || it.stringValue) {
          sw << it.toString()
        }
      }
      sw << " - " + this.text + "(" + this.label +") = "+vaerdi + '\n'

    } else {
      sw << this.text + "(" + this.label +") = "+vaerdi + '\n'
    }

    return sw.toString()
  }
}
