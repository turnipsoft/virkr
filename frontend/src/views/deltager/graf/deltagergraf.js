import React, { Component } from 'react';
import Graph from 'react-graph-vis';
import Modal from '../../common/modal.js';
import EjerVisning from '../../common/ejervisning';
import { wrap } from '../../../utils/utils';

export default class DeltagerGraf extends React.Component {

  openModal() {
    this.setState({ isModalOpen: true })
  }

  closeModal() {
    this.setState({ isModalOpen: false, specifikEjer:null})
  }

  constructor(props) {
    super(props);
    this.state = {};

    var allMap = new Map();

    this.props.deltagerGraf.grupperedeEjere.forEach((ejer) =>{
      allMap.set(ejer.enhedsnummer, ejer);
    });

    this.state = {ejerAllMap: allMap, isModalOpen: false};
    this._visEnhed = this._visEnhed.bind(this);
    this.openModal = this.openModal.bind(this);
    this.closeModal = this.closeModal.bind(this);
  }

  _visEnhed(enhedsnr) {
    const ejer = this.state.ejerAllMap.get(enhedsnr);
    //skal ikke vise oversigten men bare navigere direkte
    if (ejer.cvrnummer) {
      window.open('./#/virksomhed/'+ejer.cvrnummer,'_blank');
      //this.props.visVirksomhed(ejer.cvrnummer, true);
    } else {
      window.open('./#/deltager/'+ejer.enhedsnummer,'_blank');
      //this.props.visDeltager(ejer.enhedsnummer, true);
    }
    //this.setState({specifikEjer: ejer});
    //this.openModal();
  }

  render() {
    const deltagergraf = this.props.deltagerGraf;

    const rodEnhedsnummer = deltagergraf.deltager? deltagergraf.deltager.enhedsnummer: deltagergraf.virksomhed.enhedsNummer;

    const n = deltagergraf.unikkeEjere.map((ejer) => {
      var group = ejer.enhedsType == 'PERSON' ? 'personer' : 'virksomheder'
      if (ejer.enhedsType == 'ROD' || ejer.enhedsnummer == rodEnhedsnummer) {
        group = 'rod';
      }

      const navn = wrap(ejer.navn, 25);
      return {id: ejer.enhedsnummer, label: navn, group: group, level: ejer.level };
    });

    const e = deltagergraf.relationer.map((er) =>{
      return {from:er.deltagerEnhedsnummer, to: er.virksomhedEnhedsnummer, label: er.andelInterval, font: {align: 'horizontal', size: 10}};
    });

    var graph = {
      nodes: n,
      edges: e
    };


    var options = {
      physics: {
        enabled: false
      },
      layout: {
        hierarchical: {
          direction: "UD",
          parentCentralization: true,
          sortMethod: 'directed'
        },
        improvedLayout: true
      },
      edges: {
        color: "#000000"
      },
      height: '100%',
      width: '100%',
      autoResize: true,
      interaction: {
        hover:true
      },
      nodes: {
        font: {
          size: 10
        }
      },
      groups: {
        virksomheder: {
          shape: 'icon',
          icon: {
            face: 'FontAwesome',
            code: '\uf0c0',
            size: 50,
            color: '#57169a'
          }
        },
        rod: {
          shape: 'icon',
          icon: {
            face: 'FontAwesome',
            code: '\uf0c0',
            size: 50,
            color: '#336d1a'
          }
        },
        personer: {
          shape: 'icon',
          icon: {
            face: 'FontAwesome',
            code: '\uf007',
            size: 50,
            color: '#aa00ff'
          }
        }
      }
    };

    const vv = this._visEnhed;

    const events = {
      click: function(event) {
        var { nodes } = event;

        if (nodes.length==1) {
          vv(nodes[0]);
        }
      }
    }

    var styles = {
      width: '100%', height: '100%'
    }

    return(
      <div className="ejergraf">
        <div className="row">
          <div className="col ejergrafcol">
            <Graph graph={graph} options={options} events={events} style={styles}/>
          </div>
        </div>
        <Modal isOpen={this.state.isModalOpen} onClose={() => this.closeModal()} className="card ejercard" width="70%">
          <EjerVisning ejer={this.state.specifikEjer} visVirksomhed={this.props.visVirksomhed}
                       visDeltager={this.props.visDeltager} fraGraf />
        </Modal>
      </div>
    );
  }

}
