import React, { Component } from 'react';
import { connect } from 'react-redux'
import PageHeader from '../common/pageheader';
import Spinner from '../common/spinner';
import Soegeresultat from './soegeresultat';

class SoegeresultatView extends Component {

  render() {
    return(
      <div>
        <PageHeader headerText="Søgeresultat" />
        {this.props.showSpinner && <Spinner />}
        {this.props.soegeresultat && <Soegeresultat soegeresultat={this.props.soegeresultat} />}
      </div>
    );
  }
}

const mapStateToProps = (state, ownProps) => {
  console.log(state);
  console.log(ownProps);
  return {
    showSpinner: state.soegning.isFetching,
    soegning: ownProps.match.params.soegning,
    soegeresultat : state.soegning.soegeresultat
  }
}

export default connect(mapStateToProps)(SoegeresultatView);
