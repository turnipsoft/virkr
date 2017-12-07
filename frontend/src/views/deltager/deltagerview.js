import React, { Component } from 'react';
import { connect } from 'react-redux'
import PageHeader from '../common/pageheader';
import Spinner from '../common/spinner';
import DeltagerVisning from './deltager';
import { visDeltager, visVirksomhed, visDeltagerGraf } from '../../actions/'

class DeltagerView extends Component {

  componentWillMount() {
    const {enhedsnummer, enhedsnummerParam, visDeltager} = this.props;

    if (!enhedsnummer && enhedsnummerParam) {
      visDeltager(enhedsnummerParam, false);
    }
  }

  render() {
    console.log('DeltagerView-Render');
    const { showSpinner, deltager, visVirksomhed, visDeltagerGraf } = this.props;

    return(
      <div>
        <PageHeader iconClassName="fa fa-user" headerText="Deltager" deltager={deltager} />
        {showSpinner && <Spinner />}
        {deltager && <DeltagerVisning deltager={deltager} onVirksomhedClick={visVirksomhed} visDeltagerGraf={visDeltagerGraf} />}
      </div>
    );
  }
}

const mapStateToProps = (state, ownProps) => {
  console.log('DeltagerView');
  const {deltager} = state;

  return {
    showSpinner: deltager.isFetching,
    enhedsnummerParam: ownProps.match.params.enhedsnummer,
    enhedsnummer: deltager.enhedsnummer,
    deltager: deltager.deltager
  }
}

const mapDispatchToProps = {
  visVirksomhed,
  visDeltager,
  visDeltagerGraf
}

export default connect(mapStateToProps, mapDispatchToProps)(DeltagerView);
