import React, { Component } from 'react';
import Graph from 'react-graph-vis'

export default class EjerGraf extends React.Component {

  constructor(props) {
    super(props);
    this.state = {};

    var map = new Map();

    this.props.ejergraf.ejere.forEach((ejer) =>{
      map.set(ejer.ejer.enhedsnummer, ejer.ejer.forretningsnoegle);
    });

    this.state = {ejerMap: map};
    this._visVirksomhed = this._visVirksomhed.bind(this);
  }

  _visVirksomhed(enhedsnr) {
    const cvrnummer = this.state.ejerMap.get(enhedsnr);
    if (cvrnummer) {
      this.props.opdaterCvrNummer(cvrnummer);
    }
  }

  render() {
    const ejergraf = this.props.ejergraf;

    const n = ejergraf.ejere.map((ejer) => {
      var group = ejer.ejer.ejertype == 'PERSON' ? 'personer' : 'virksomheder'
      if (ejer.ejer.ejertype == 'ROD') {
        group = 'rod';
      }
      return {id: ejer.ejer.enhedsnummer, label: ejer.ejer.navn, group: group};
    });

    const e = ejergraf.ejerRelationer.map((er) =>{
      const vnr = (er.virksomhed.ejer ? er.virksomhed.ejer.enhedsnummer:"0");
      return {from:er.ejer.ejer.enhedsnummer, to: vnr};
    });

    var graph = {
      nodes: n,
      edges: e
    };


    var options = {
      layout: {
        hierarchical: false,
        improvedLayout: true
      },
      edges: {
        color: "#000000"
      },
      height: '800px',
      width: '100%',
      autoResize: true,
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
            color: '#2058b2'
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

    var vv = this._visVirksomhed;

    var events = {
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
        <br/>
        <div className="row">
          <div className="col-12 section-header">
            <span className="fa fa-sitemap" /> &nbsp; Ejergraf
          </div>
        </div>
        <br/>
        <div className="row">
          <div className="col ejergrafcol">
            <Graph graph={graph} options={options} events={events} style={styles}/>
          </div>
        </div>
      </div>
    );
  }

}
