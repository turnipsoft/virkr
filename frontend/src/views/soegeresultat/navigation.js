import React, { Component } from 'react'

export default class Navigation extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    const {
      current,
      pagesize
    } = this.props.links

    const { antalDeltagere, antalVirksomheder, soeg, soegning } = this.props
    const maxAntal = antalDeltagere > antalVirksomheder ? antalDeltagere : antalVirksomheder;
    const antalSider = Math.floor( maxAntal / pagesize );
    const previous  = current>1? current-1: current;
    const next = current>maxAntal? current: current+1;

    return(
      <div className="row">
        <div className="col-4 navigation-text">
          Viser side {current} af {antalSider}
        </div>
        <div className="col-4">
          <nav aria-label="Navigation">
            <ul className="pagination justify-content-center">
              <li className="page-item" >
                <a className="page-link lightblue" href="#" onClick={(e)=> {e.preventDefault(); soeg(soegning, false, 1, pagesize)}} aria-label="First">
                  <span aria-hidden="true">&lt;</span>
                  <span className="sr-only">First</span>
                </a>
              </li>
              <li className="page-item">
                <a className="page-link" href="#" onClick={(e)=> {e.preventDefault(); soeg(soegning, false, previous, pagesize)}} aria-label="Previous">
                  <span aria-hidden="true">&laquo;</span>
                  <span className="sr-only">Previous</span>
                </a>
              </li>
              <li className="page-item">
                <a className="page-link" href="#" onClick={(e)=> {e.preventDefault(); soeg(soegning, false, next, pagesize)}} aria-label="Next">
                  <span aria-hidden="true">&raquo;</span>
                  <span className="sr-only">Next</span>
                </a>
              </li>
              <li className="page-item">
                <a className="page-link" href="#" onClick={(e)=> {e.preventDefault(); soeg(soegning, false, antalSider, pagesize)}} aria-label="Last">
                  <span aria-hidden="true">&gt;</span>
                  <span className="sr-only">Last</span>
                </a>
              </li>
            </ul>
          </nav>
        </div>

        <div className="col-4 navigation-text">
          <p className="pull-right ">Antal virksomheder: {antalVirksomheder}, Antal deltagere: {antalDeltagere}</p>
        </div>
      </div>
      )
  }

}
