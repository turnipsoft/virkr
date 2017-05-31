import React from 'react';
import Virksomhed from './virksomhed';
import CvrSoegebox from './cvrsoegebox';
import CvrVisning from './cvrvisning';
import { shallow } from 'enzyme';

describe("Komponenten Virksomhed", () => {

  it("Indeholder en søgeboks", () => {
    const wrapper = shallow(<Virksomhed />);

    expect(wrapper.find(CvrSoegebox).length).toBe(1);
  })

  it("Indeholder visning af en virksomhed", () => {
    const wrapper = shallow(<Virksomhed />);

    expect(wrapper.find(CvrVisning).length).toBe(1);
  })
})
