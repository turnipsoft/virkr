import 'whatwg-fetch';

export default class APIHelper {

  static hentNoegletal(cvrnummer) {
    return this._call('http://virkr.dk:9092/regnskab/' + cvrnummer)
  }

  static hentVirksomhedsdata(cvrnummer) {
    return this._call('http://virkr.dk:9092/cvr/' + cvrnummer)
  }

  static hentEjerGraf(cvrnummer) {
    return this._call('http://virkr.dk:9092/cvr/graf/' + cvrnummer)
  }

  static soeg(soegning) {
    return this._call('http://virkr.dk:9092/cvr/search/' + soegning)
  }

  static _call(url) {
    return new Promise((resolve, reject) => {
      fetch(url).then(response => {
        if (response.ok) {
          resolve(response.json())
        } else {
          reject(Error(response.statusText))
        }
      }, error => {
        reject(Error(error.message))
      }
      )
    })
  }

}

