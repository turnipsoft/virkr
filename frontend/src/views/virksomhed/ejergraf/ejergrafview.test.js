const rewire = require("rewire")
const ejergrafview = rewire("./ejergrafview")
const EjerGrafView = ejergrafview.__get__("EjerGrafView")
const mapStateToProps = ejergrafview.__get__("mapStateToProps")
// @ponicode
describe("componentWillMount", () => {
    let inst

    beforeEach(() => {
        inst = new EjerGrafView()
    })

    test("0", () => {
        let callFunction = () => {
            inst.componentWillMount()
        }
    
        expect(callFunction).not.toThrow()
    })
})

// @ponicode
describe("componentWillReceiveProps", () => {
    let inst

    beforeEach(() => {
        inst = new EjerGrafView()
    })

    test("0", () => {
        let callFunction = () => {
            inst.componentWillReceiveProps({ ejerGraf: { virksomhed: "Nile Crocodile" } })
        }
    
        expect(callFunction).not.toThrow()
    })

    test("1", () => {
        let callFunction = () => {
            inst.componentWillReceiveProps({ ejerGraf: { virksomhed: "Australian Freshwater Crocodile" } })
        }
    
        expect(callFunction).not.toThrow()
    })

    test("2", () => {
        let callFunction = () => {
            inst.componentWillReceiveProps({ ejerGraf: { virksomhed: "Spectacled Caiman" } })
        }
    
        expect(callFunction).not.toThrow()
    })

    test("3", () => {
        let callFunction = () => {
            inst.componentWillReceiveProps({ ejerGraf: { virksomhed: "Dwarf Crocodile" } })
        }
    
        expect(callFunction).not.toThrow()
    })

    test("4", () => {
        let callFunction = () => {
            inst.componentWillReceiveProps({ ejerGraf: { virksomhed: "Saltwater Crocodile" } })
        }
    
        expect(callFunction).not.toThrow()
    })

    test("5", () => {
        let callFunction = () => {
            inst.componentWillReceiveProps(undefined)
        }
    
        expect(callFunction).not.toThrow()
    })
})

// @ponicode
describe("mapStateToProps", () => {
    test("0", () => {
        let callFunction = () => {
            mapStateToProps({}, "George")
        }
    
        expect(callFunction).not.toThrow()
    })

    test("1", () => {
        let callFunction = () => {
            mapStateToProps("Île-de-France", "George")
        }
    
        expect(callFunction).not.toThrow()
    })

    test("2", () => {
        let callFunction = () => {
            mapStateToProps("Île-de-France", "Pierre Edouard")
        }
    
        expect(callFunction).not.toThrow()
    })

    test("3", () => {
        let callFunction = () => {
            mapStateToProps({}, "Pierre Edouard")
        }
    
        expect(callFunction).not.toThrow()
    })

    test("4", () => {
        let callFunction = () => {
            mapStateToProps("Île-de-France", "Anas")
        }
    
        expect(callFunction).not.toThrow()
    })

    test("5", () => {
        let callFunction = () => {
            mapStateToProps(undefined, undefined)
        }
    
        expect(callFunction).not.toThrow()
    })
})
