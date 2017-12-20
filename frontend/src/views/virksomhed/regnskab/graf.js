import React, { Component } from 'react';
import { Line } from 'react-chartjs-2';

export default class Graf extends Component {

  constructor(props) {
    super(props);

    Chart.defaults.global.scaleLabel = (label) => {
      return label.value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
    };

    Chart.defaults.global.multiTooltipTemplate = (label) => {
      return label.datasetLabel + ': ' + label.value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
    };

  }

  render() {
    return this.getGraf(this.props.regnskaber);
  }

  getGraf(regnskaber) {
    const aarLabels = regnskaber.map((regnskab) => {
      return regnskab.aktueltAarsregnskab.aar
    });

    const bruttofortjenester = regnskaber.map((regnskab) => {
      if (regnskab.aktueltAarsregnskab.resultatopgoerelse.bruttoresultatTal.bruttofortjeneste) {
        return regnskab.aktueltAarsregnskab.resultatopgoerelse.bruttoresultatTal.bruttofortjeneste.vaerdi;
      }

      return null;

    });

    const egenkapitaler = regnskaber.map((regnskab) => {
      if (regnskab.aktueltAarsregnskab.balance.passiver.egenkapital) {
        return regnskab.aktueltAarsregnskab.balance.passiver.egenkapital.vaerdi;
      }

      return null;

    });

    const aaretsresultater = regnskaber.map((regnskab) => {
      if (regnskab.aktueltAarsregnskab.resultatopgoerelse.aaretsresultatTal.aaretsresultat) {
        return regnskab.aktueltAarsregnskab.resultatopgoerelse.aaretsresultatTal.aaretsresultat.vaerdi;
      }

      return null;
    });

    const options = {
      tooltips: {
        enabled: true,

        callbacks: {
          label: (tooltipItems, data) => {
            return data.datasets[tooltipItems.datasetIndex].label + ' : ' + tooltipItems.yLabel.toLocaleString().replace(/,/g, ".");
          }
        }
      },
      scales: {
        yAxes: [{
          ticks: {
            // Create scientific notation labels
            callback: (value) => {
              return value.toLocaleString().replace(/,/g, ".");
            }
          },
          gridLines: {
            zeroLineColor: '#d80a0a'
          }
        }]
      }
    }

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
      <div className="graf">
        <Line data={data} options={options} />
      </div>
    );
  }


}
