import React from 'react';

const PageHeader = ({ headerText, iconClassName, cvrdata, deltager }) => {

  let externalLink = '';
  let externalLinkText = '';
  let details = null;

  if (cvrdata) {
    externalLink = `https://datacvr.virk.dk/data/visenhed?enhedstype=virksomhed&id=${cvrdata.cvrNummer}`
    externalLinkText = 'Åbn i cvr.dk';
    details = `${cvrdata.virksomhedMetadata.nyesteNavn.navn} - CVR-Nr: ${cvrdata.cvrNummer} (${cvrdata.nyesteStatus})`;
  }

  if (deltager) {
    externalLink = `https://datacvr.virk.dk/data/visenhed?enhedstype=person&id=${deltager.enhedsNummer}`
    externalLinkText = 'Åbn i cvr.dk';
    details = `${deltager.navn}`;
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
                &nbsp; <a href={externalLink} target="_blank">{externalLinkText}</a>
              </div>}
            </div>
            <div className="col-1">
            </div>
          </div>

        </div>
      </div>
    </div>
  )
};

export default PageHeader;
