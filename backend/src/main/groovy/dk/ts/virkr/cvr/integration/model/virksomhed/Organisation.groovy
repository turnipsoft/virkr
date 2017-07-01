package dk.ts.virkr.cvr.integration.model.virksomhed

/**
 * Created by sorenhartvig on 01/07/2017.
 */
class Organisation {
  String enhedsNummerOrganisation
  String hovedtype
  List<OrganisationsNavn> organisationsNavn
  List<Attribut> attributter
  List<Medlemsdata> medlemsData
}
