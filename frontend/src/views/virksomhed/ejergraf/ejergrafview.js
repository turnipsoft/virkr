import React, { Component } from 'react';
import { connect } from 'react-redux';
import EjerGraf from './ejergraf';
import DeltagerGraf from '../../deltager/graf/deltagergraf';
import PageHeader from '../../common/pageheader';
import Spinner from '../../common/spinner';
import { visVirksomhed, visDeltager, visEjerGraf } from '../../../actions/';

class EjerGrafView extends Component {

  componentWillMount() {
    const {cvrnummer, cvrnummerParam, visEjerGraf} = this.props;

    if (!cvrnummer && cvrnummerParam) {
      visEjerGraf(cvrnummerParam, false);
    }
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.ejerGraf) {
      const virksomhed = nextProps.ejerGraf.virksomhed;
      document.title = `virkr.dk - ${virksomhed.virksomhedMetadata.nyesteNavn.navn} - Ejergraf`;
    }
  }

  render() {
    const { showSpinner, ejerGraf, deltagerGraf, visVirksomhed, visDeltager, error } = this.props;

    const virksomhed = ejerGraf?ejerGraf.virksomhed:null;

    return(
      <div>
        <PageHeader headerText="Ejergraf" iconClassName="fa fa-sitemap" cvrdata={virksomhed} context="ejergraf" />
        {showSpinner && <Spinner />}
        {ejerGraf && <EjerGraf ejerGraf={ejerGraf}
                               visVirksomhed={visVirksomhed}
                               visDeltager={visDeltager} />}
        {this._renderDeltagerGraf(deltagerGraf, visVirksomhed, visDeltager)}
        {error && <div className="alert alert-danger alert-margin-top" role="alert">Der er opstået en fejl under hentning ejergrafen</div>}
      </div>
    );
  }

  _renderDeltagerGraf(deltagerGraf, visVirksomhed, visDeltager) {
    if (deltagerGraf && deltagerGraf.unikkeEjere && deltagerGraf.unikkeEjere.length > 0 ) {
      const navn = deltagerGraf.virksomhed.virksomhedMetadata.nyesteNavn.navn;

      return (
        <div>
          <div className="row">
            <div className="col-12 section-header">
              Virksomheder ejet af {navn}
            </div>
          </div>
          <DeltagerGraf deltagerGraf={deltagerGraf}
                        visVirksomhed={visVirksomhed}
                        visDeltager={visDeltager} />

        </div>
      )
    }
  }

}

const mapStateToProps = (state, ownProps) => {
  const { ejerGraf } = state;

  return {
    ejerGraf: ejerGraf.ejerGraf,
    deltagerGraf: ejerGraf.deltagerGraf,
    showSpinner: ejerGraf.isFetching,
    cvrnummer: ejerGraf.cvrnummer,
    cvrnummerParam: ownProps.match.params.cvrnummer,
    error: ejerGraf.error
  }
}

const mapDispatchToProps = {
  visVirksomhed,
  visDeltager,
  visEjerGraf
}

export default connect(mapStateToProps, mapDispatchToProps)(EjerGrafView);
