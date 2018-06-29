import React from 'react';

const PageHeader = ({ headerText, iconClassName, cvrdata, deltager, context, detailText }) => {

  let externalLink = '';
  let externalLinkText = '';
  let details = null;
  let tabLink = '';
  let tabText = 'Åbn i ny fane';

  if (cvrdata) {
    externalLink = `https://datacvr.virk.dk/data/visenhed?enhedstype=virksomhed&id=${cvrdata.cvrNummer}`
    externalLinkText = 'Åbn i cvr.dk';
    details = `${cvrdata.virksomhedMetadata.nyesteNavn.navn} - CVR-Nr: ${cvrdata.cvrNummer} (${cvrdata.nyesteStatus})`;
    tabLink = `./#/${context}/${cvrdata.cvrNummer}`
  }

  if (deltager) {
    externalLink = `https://datacvr.virk.dk/data/visenhed?enhedstype=person&id=${deltager.enhedsNummer}`
    externalLinkText = 'Åbn i cvr.dk';
    details = `${deltager.navn}`;
    tabLink = `./#/${context}/${deltager.enhedsNummer}`
  }

  let virkrheaderclass = 'col-1 virkr-header';
  if (details) {
    virkrheaderclass += ' virkr-header-margin'
  }

  return(
    <div className="top-bar">
      <div className="row">
        <div className="col-12 section-header">
          <div className="row">
            <div className={virkrheaderclass}>
              <a href="/">Virkr</a>
            </div>
            <div className="col-10">
              <span className={iconClassName} /> &nbsp; {headerText?headerText:null}
              {details && <div className="header-details">
                {details}
                &nbsp; <a href={externalLink} target="_blank">{externalLinkText}</a> &nbsp;
                | &nbsp;
                <a href={tabLink} target="_blank">{tabText}</a>
              </div>}
              {detailText && <div className="header-details">
                {detailText}
              </div>}
            </div>
            <div className="col-1 align-middle text-right dictionarylink">
              <a href="/#/dictionary" title="Klik for åbne XBRL opslagsværk" target="_blank"><span className="fa fa-book align-middle text-right"/></a>
            </div>

          </div>

        </div>
      </div>
    </div>
  )
};

export default PageHeader;
