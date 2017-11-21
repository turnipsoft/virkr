import React, { Component } from 'react';
import { connect } from 'react-redux'
import PageHeader from '../common/pageheader';
import Spinner from '../common/spinner';
import { visDeltager } from '../../actions/';
import Soegeresultat from './soegeresultat';

class SoegeresultatView extends Component {

  render() {
    return(
      <div>
        <PageHeader headerText="Søgeresultat" />
        {this.props.showSpinner && <Spinner />}
        {this.props.soegeresultat && <Soegeresultat soegeresultat={this.props.soegeresultat}
                                                    onDeltagerClick={this.props.visDeltager}/>}
      </div>
    );
  }
}

const mapStateToProps = (state) => {
  return {
    showSpinner: state.soegning.isFetching,
    soegning: state.soegning.soegning,
    soegeresultat : state.soegning.soegeresultat
  }
}

const mapDispatchToProps = {
  visDeltager
}

export default connect(mapStateToProps, mapDispatchToProps)(SoegeresultatView);
