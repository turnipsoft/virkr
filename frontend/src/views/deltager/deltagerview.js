import React, { Component } from 'react';
import { connect } from 'react-redux'
import { visDeltager } from '../../actions/';
import PageHeader from '../common/pageheader';
import Spinner from '../common/spinner';
import DeltagerVisning from './deltager';

class DeltagerView extends Component {

  componentDidMount() {
    if (!this.state) {
      this.props.dispatch(visDeltager(this.props.match.params.enhedsnummer));
    }
  }

  render() {

    const { showSpinner, deltager } = this.props;

    return(
      <div>
        <PageHeader headerText="Deltager" />
        {showSpinner && <Spinner />}
        {deltager && <DeltagerVisning deltager={deltager} />}
      </div>
    );
  }
}

const mapStateToProps = (state, ownProps) => {
  console.log(state);
  console.log(ownProps);
  return {
    showSpinner: state.deltager.isFetching,
    enhedsnummer: ownProps.match.params.enhedsnummer,
    deltager : state.deltager.deltager
  }
}

export default connect(mapStateToProps)(DeltagerView);
