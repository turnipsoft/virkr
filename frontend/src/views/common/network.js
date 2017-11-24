import React, {Component} from 'react';
import vis from 'vis';

export default class Network extends Component {

  constructor(props) {
    super(props);
    const { identifier } = props;
    this.updateGraph = this.updateGraph.bind(this);
    this.state = {
      identifier: identifier
    };
  }

  componentDidMount() {
    this.edges = new vis.DataSet();
    this.edges.add(this.props.graph.edges);
    this.nodes = new vis.DataSet();
    this.nodes.add(this.props.graph.nodes);
    this.updateGraph();
  }

  updateGraph() {
    let container = document.getElementById(this.state.identifier);
    let defaultOptions = {
      physics: {
        stabilization: false
      },
      autoResize: false,
      edges: {
        smooth: false,
        color: "#000000",
        width: 0.5,
        arrows: {
          to: {
            enabled: true,
            scaleFactor: 0.5
          }
        }
      }
    };

    // merge user provied options with our default ones
    let options = defaultsDeep(defaultOptions, this.props.options);

    this.Network = new vis.Network(
      container,
      Object.assign({}, this.props.graph, {
        edges: this.edges,
        nodes: this.nodes
      }),
      options
    );

    if (this.props.getNetwork) {
      this.props.getNetwork(this.Network);
    }

    if (this.props.getNodes) {
      this.props.getNodes(this.nodes);
    }

    // Add user provied events to network
    let events = this.props.events || {};
    for (let eventName of Object.keys(events)) {
      this.Network.on(eventName, events[eventName]);
    }
  }

  render() {
    const {identifier} = this.state;
    const {style} = this.props;
    return React.createElement(
      "div",
      {
        id: identifier,
        style
      },
      identifier
    );
  }
}
