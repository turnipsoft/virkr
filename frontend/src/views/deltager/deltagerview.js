import React, { Component } from 'react';
import { connect } from 'react-redux'
import PageHeader from '../common/pageheader';
import Spinner from '../common/spinner';
import DeltagerVisning from './deltager';
import { visDeltager } from '../../actions/'

class DeltagerView extends Component {

  componentWillMount() {
    if (this.state===null && this.props.enhedsnummerParam) {
      this.props.dispatch(visDeltager(this.props.enhedsnummerParam, false));
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
  return {
    showSpinner: state.deltager.isFetching,
    enhedsnummerParam: ownProps.match.params.enhedsnummer,
    deltager : state.deltager.deltager
  }
}

export default connect(mapStateToProps)(DeltagerView);
