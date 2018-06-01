import React, { Component } from 'react';
import { connect } from 'react-redux'
import PageHeader from '../common/pageheader';
import Spinner from '../common/spinner';
import { visDeltager, visVirksomhed, soeg } from '../../actions/';
import Soegeresultat from './soegeresultat';

class SoegeresultatView extends Component {

  componentWillMount() {
    const {soeg, soegningParam, soegning} = this.props;

    if (soegning!==soegningParam && soegningParam) {
      soeg(soegningParam, false)
    }
  }

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

const mapStateToProps = (state, ownProps) => {
  return {
    showSpinner: state.soegning.isFetching,
    soegning: state.soegning.soegetext,
    soegeresultat : state.soegning.soegeresultat,
    soegningParam: ownProps.match.params.soegning,
  }
}

const mapDispatchToProps = {
  visDeltager,
  visVirksomhed,
  soeg
}

export default connect(mapStateToProps, mapDispatchToProps)(SoegeresultatView);
