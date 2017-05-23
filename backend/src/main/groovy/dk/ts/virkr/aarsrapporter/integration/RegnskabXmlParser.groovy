package dk.ts.virkr.aarsrapporter.integration

import dk.ts.virkr.aarsrapporter.integration.model.regnskabdata.RegnskabData
import groovy.xml.Namespace

/**
 * Created by sorenhartvig on 22/06/16.
 */
class RegnskabXmlParser {
    RegnskabData parseOgBerig(RegnskabData data, String xml) {
        XmlParser parser = new XmlParser(false, false)

        def result = parser.parseText(xml)
        String fsaNamespace = getFSANamespace(xml)

        def ns = new Namespace("http://xbrl.dcca.dk/fsa",fsaNamespace)

        data.bruttofortjeneste = getValue(result, ns.GrossProfitLoss, ns.GrossResult)
        data.driftsresultat = getValue(result, ns.ProfitLossFromOrdinaryOperatingActivities)
        data.resultatfoerskat = getValue(result, ns.ProfitLossFromOrdinaryActivitiesBeforeTax)
        data.aaretsresultat = getValue(result, ns.ProfitLoss)
        data.egenkapital = getValue(result, ns.Equity)
        data.skatafaaretsresultat = getValue(result, ns.TaxExpenseOnOrdinaryActivities)
        data.gaeldsforpligtelser = getValue(result, ns.LiabilitiesOtherThanProvisions)

        return data
    }

    String getFSANamespace(String xml) {
        int idx=xml.indexOf("http://xbrl.dcca.dk/fsa")
        xml=xml.substring(0,idx)
        idx = xml.lastIndexOf('xmlns:')
        String namespace = xml.substring(idx+6,xml.length()-2)
        return namespace
    }
    Long getValue(root, nodeName, alternateNodeName = null ) {
        def nodes = root[nodeName]
        if (nodes && nodes.size()>0) {
            Node node = nodes[0]
            return getAmount(node)
        } else if (alternateNodeName) {
            return getValue(root, alternateNodeName)
        }

        return null
    }

    Long getAmount(Node n) {
        String amount = n.text()
        return Long.valueOf(amount)
    }
}
