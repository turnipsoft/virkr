import React, { Component } from 'react';
import { connect } from 'react-redux'
import PageHeader from '../common/pageheader';
import Spinner from '../common/spinner';
import { visDeltager, visVirksomhed } from '../../actions/';
import Soegeresultat from './soegeresultat';

class SoegeresultatView extends Component {

  render() {
    const {soegning, visDeltager, soegeresultat, visVirksomhed } = this.props;

    return(
      <div>
        <PageHeader iconClassName="fa fa-search" headerText="Søgeresultat" />
        {this.props.showSpinner && <Spinner />}
        {this.props.soegeresultat && <Soegeresultat soegning={soegning}
                                                    soegeresultat={soegeresultat}
                                                    visDeltager={visDeltager}
                                                    visVirksomhed={visVirksomhed} />}
      </div>
    );
  }
}

const mapStateToProps = (state) => {
  return {
    showSpinner: state.soegning.isFetching,
    soegning: state.soegning.soegetext,
    soegeresultat : state.soegning.soegeresultat
  }
}

const mapDispatchToProps = {
  visDeltager,
  visVirksomhed
}

export default connect(mapStateToProps, mapDispatchToProps)(SoegeresultatView);
