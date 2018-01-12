import React, { Component } from 'react';

export default class SectionHeader extends Component {
  render() {
    const { label, detail } = this.props;

    const onClick = this.props.onClick? this.props.onClick : ()=>{};
    const iconClass = this.props.iconClass + " white fa-2x";
    const vheader = this.props.headerClass?this.props.headerClass:'vheader';
    const vheaderCircle = this.props.headerClassCircle?this.props.headerClassCircle:'vheader-icon-circle';
    const vheaderlabel = 'col ' + (this.props.labelClass?this.props.labelClass:'vheader-label');

    return(
      <div className="row">
        <div className="col-12">
            <div className={vheader} onClick={onClick} >
              <div className="row">
                <div className="col-1">
                  <div className={vheaderCircle}>
                    <div className="vheader-icon">
                      <span className={iconClass} />
                    </div>
                  </div>
                </div>
                <div className="col">
                  <div className="row">
                    <div className={vheaderlabel} >
                      {label}
                    </div>
                  </div>
                  <div className="row">
                    <div className="col vheader-desc">
                      {detail}
                    </div>
                  </div>
                </div>
              </div>
            </div>
        </div>
      </div>
    );
  }
}
