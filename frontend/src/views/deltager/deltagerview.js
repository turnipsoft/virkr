import React, { Component } from 'react';
import { connect } from 'react-redux'
import PageHeader from '../common/pageheader';
import Spinner from '../common/spinner';
import DeltagerVisning from './deltager';
import { visDeltager, visVirksomhed } from '../../actions/'

class DeltagerView extends Component {

  componentWillMount() {
    if (this.state===null && this.props.enhedsnummerParam) {
      this.props.visDeltager(this.props.enhedsnummerParam, false);
    }
  }

  render() {
    const { showSpinner, deltager } = this.props;

    return(
      <div>
        <PageHeader headerText="Deltager" />
        {showSpinner && <Spinner />}
        {deltager && <DeltagerVisning deltager={deltager} onVirksomhedClick={this.props.visVirksomhed} />}
      </div>
    );
  }
}

const mapStateToProps = (state, ownProps) => {
  return {
    showSpinner: state.deltager.isFetching,
    enhedsnummerParam: ownProps.match.params.enhedsnummer,
    deltager : state.deltager.deltager
  }
}

const mapDispatchToProps = {
  visVirksomhed,
  visDeltager
}

export default connect(mapStateToProps, mapDispatchToProps)(DeltagerView);
