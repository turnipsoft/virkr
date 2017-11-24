import React, { Component } from 'react';
import { connect } from 'react-redux';
import DeltagerGraf from './deltagergraf';
import PageHeader from '../../common/pageheader';
import Spinner from '../../common/spinner';
import { visVirksomhed, visDeltager, visDeltagerGraf } from '../../../actions/';

class DeltagerGrafView extends Component {

  componentWillMount() {
    const {enhedsnummer, enhedsnummerParam, visDeltagerGraf} = this.props;

    if (!enhedsnummer && enhedsnummerParam) {
      visDeltagerGraf(enhedsnummerParam, false);
    }
  }

  render() {
    console.log('DeltagerGrafView-render');
    const { showSpinner, deltagerGraf, visVirksomhed, visDeltager } = this.props;

    return(
      <div>
        <PageHeader headerText="Deltager Graf" iconClassName="fa fa-sitemap" />
        {showSpinner && <Spinner />}
        {deltagerGraf && <DeltagerGraf deltagerGraf={deltagerGraf}
                                       visVirksomhed={visVirksomhed}
                                       visDeltager={visDeltager} />}
      </div>
    );
  }

}

const mapStateToProps = (state, ownProps) => {
  console.log('DeltagerGrafView');
  const { deltagerGraf } = state;

  return {
    deltagerGraf: deltagerGraf.deltagerGraf,
    showSpinner: deltagerGraf.isFetching,
    enhedsnummer: deltagerGraf.enhedsnummer,
    enhedsnummerParam: ownProps.match.params.enhedsnummer,
    router: state.router
  }
}

const mapDispatchToProps = {
  visVirksomhed,
  visDeltager
}

export default connect(mapStateToProps, mapDispatchToProps)(DeltagerGrafView);
