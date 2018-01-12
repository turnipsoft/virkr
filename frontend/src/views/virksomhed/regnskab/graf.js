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
          backgroundColor: "#F19D4B",
          borderColor: "#F19D4B",
          borderCapStyle: 'butt',
          borderDash: [],
          borderDashOffset: 0.0,
          borderJoinStyle: 'miter',
          pointBorderColor: "#F19D4B",
          pointBackgroundColor: "#fff",
          pointBorderWidth: 1,
          pointHoverRadius: 5,
          pointHoverBackgroundColor: "#F19D4B",
          pointHoverBorderColor: "#113d84",
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
          backgroundColor: "#CADADE",
          borderColor: "#CADADE",
          borderCapStyle: 'butt',
          borderDash: [],
          borderDashOffset: 0.0,
          borderJoinStyle: 'miter',
          pointBorderColor: "#CADADE",
          pointBackgroundColor: "#fff",
          pointBorderWidth: 1,
          pointHoverRadius: 5,
          pointHoverBackgroundColor: "#CADADE",
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
          backgroundColor: "#52767c",
          borderColor: "#52767c",
          borderCapStyle: 'butt',
          borderDash: [],
          borderDashOffset: 0.0,
          borderJoinStyle: 'miter',
          pointBorderColor: "#52767c",
          pointBackgroundColor: "#fff",
          pointBorderWidth: 1,
          pointHoverRadius: 5,
          pointHoverBackgroundColor: "#52767c",
          pointHoverBorderColor: "#113d84",
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
