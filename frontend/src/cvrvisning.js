import React, { Component } from 'react';
import Regnskabstal from './regnskabstal';
import { Line } from 'react-chartjs-2';

export default class CvrVisning extends Component {

  constructor(props) {
    super(props)
  }

  render() {
    const r = this._getRegnskabstal();
    const c = this._getChartJsLineChart();

    return (<div className="virksomhedsdata">
      {c}
      <br />
      <br />
      {r}
    </div>);
  }

  _getRegnskabstal() {
    return this.props.regnskaber.slice().reverse().map((regnskab) => {
      return <Regnskabstal
        id={regnskab.id}
        aar={regnskab.aar}
        startDato={regnskab.startDato}
        slutDato={regnskab.slutDato}
        bruttofortjeneste={regnskab.bruttofortjeneste}
        driftsresultat={regnskab.driftsresultat}
        egenkapital={regnskab.egenkapital}
        aaretsresultat={regnskab.aaretsresultat}
        resultatfoerskat={regnskab.resultatfoerskat}
        skatafaaretsresultat={regnskab.skatafaaretsresultat}
        gaeldsforpligtelser={regnskab.gaeldsforpligtelser}
        pdfUrl={regnskab.pdfUrl}
        key={regnskab.startDato} />
    });
  }

  _getChartJsLineChart() {

    if (this.props.regnskaber.length > 0) {

      Chart.defaults.global.scaleLabel = function (label) {
        return label.value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
      };

      Chart.defaults.global.multiTooltipTemplate = function (label) {
        return label.datasetLabel + ': ' + label.value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
      };

      const options = {
        tooltips: {
          enabled: true,

          callbacks: {
            label: function (tooltipItems, data) {
              return data.datasets[tooltipItems.datasetIndex].label + ' : ' + tooltipItems.yLabel.toLocaleString().replace(/,/g, ".");
            }
          }
        },
        scales: {
          yAxes: [{
            ticks: {
              // Create scientific notation labels
              callback: function (value) {
                return value.toLocaleString().replace(/,/g, ".");
              }
            }
          }]
        }
      }

      const aarLabels = this.props.regnskaber.map((regnskab) => {
        return regnskab.aar
      });

      const bruttofortjenester = this.props.regnskaber.map((regnskab) => {
        return regnskab.bruttofortjeneste
      });

      const egenkapitaler = this.props.regnskaber.map((regnskab) => {
        return regnskab.egenkapital;
      });

      const aaretsresultater = this.props.regnskaber.map((regnskab) => {
        return regnskab.aaretsresultat;
      });

      const data = {
        labels: aarLabels,
        datasets: [
          {
            label: "Bruttofortjeneste",
            fill: false,
            lineTension: 0.1,
            backgroundColor: "rgba(75,192,192,0.4)",
            borderColor: "rgba(75,192,192,1)",
            borderCapStyle: 'butt',
            borderDash: [],
            borderDashOffset: 0.0,
            borderJoinStyle: 'miter',
            pointBorderColor: "rgba(75,192,192,1)",
            pointBackgroundColor: "#fff",
            pointBorderWidth: 1,
            pointHoverRadius: 5,
            pointHoverBackgroundColor: "rgba(75,192,192,1)",
            pointHoverBorderColor: "rgba(220,220,220,1)",
            pointHoverBorderWidth: 2,
            pointRadius: 1,
            pointHitRadius: 10,
            data: bruttofortjenester,
            spanGaps: false
          },
          {
            label: "Egenkapital",
            fill: false,
            lineTension: 0.1,
            backgroundColor: "#113d84",
            borderColor: "#113d84",
            borderCapStyle: 'butt',
            borderDash: [],
            borderDashOffset: 0.0,
            borderJoinStyle: 'miter',
            pointBorderColor: "#113d84",
            pointBackgroundColor: "#fff",
            pointBorderWidth: 1,
            pointHoverRadius: 5,
            pointHoverBackgroundColor: "#113d84",
            pointHoverBorderColor: "#113d84",
            pointHoverBorderWidth: 2,
            pointRadius: 1,
            pointHitRadius: 10,
            data: egenkapitaler,
            spanGaps: false
          },
          {
            label: "Årets resultat",
            fill: false,
            lineTension: 0.1,
            backgroundColor: "#8884d8",
            borderColor: "#8884d8",
            borderCapStyle: 'butt',
            borderDash: [],
            borderDashOffset: 0.0,
            borderJoinStyle: 'miter',
            pointBorderColor: "#8884d8",
            pointBackgroundColor: "#fff",
            pointBorderWidth: 1,
            pointHoverRadius: 5,
            pointHoverBackgroundColor: "#8884d8",
            pointHoverBorderColor: "#8884d8",
            pointHoverBorderWidth: 2,
            pointRadius: 1,
            pointHitRadius: 10,
            data: aaretsresultater,
            spanGaps: false
          }
        ]
      };

      return (
        <div>
          <h2>Nøgletal for virksomheden</h2>
          <Line data={data} options={options} />
        </div>
      );
    }
  }

}
