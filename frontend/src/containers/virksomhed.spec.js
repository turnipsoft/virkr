import React from 'react';
import Virksomhed from './virksomhed';
import CvrSoegebox from '../views/cvrsoegebox';
import CvrVisning from '../views/cvrvisning';
import { shallow } from 'enzyme';

describe("Komponenten Virksomhed", () => {

  it("Indeholder en søgeboks", () => {
    const wrapper = shallow(<Virksomhed />);

    expect(wrapper.find(CvrSoegebox).length).toBe(1);
  })

  it("Indeholder initielt ikke visning af en virksomhed", () => {
    const wrapper = shallow(<Virksomhed />);

    expect(wrapper.find(CvrVisning).length).toBe(0);
  })
})

