import React from 'react';
import App from './app';
import CvrSoegebox from '../views/cvrsoegebox';
import CvrVisning from '../views/cvrvisning';
import { shallow } from 'enzyme';

describe("Komponenten App", () => {

  it("Indeholder en søgeboks", () => {
    const wrapper = shallow(<App />);

    expect(wrapper.find(CvrSoegebox).length).toBe(1);
  })

  it("Indeholder initielt ikke visning af en virksomhed", () => {
    const wrapper = shallow(<App />);

    expect(wrapper.find(CvrVisning).length).toBe(0);
  })
})

