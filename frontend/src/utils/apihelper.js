import 'whatwg-fetch';

export default class APIHelper {

  static host() {
    return API_URL
  }

  static url(path) {
    return this.host()+path;
  }

  static hentDictionary() {
    return this._call(this.url('/dictionary'))
  }

  static hentNoegletal(cvrnummer) {
    return this._call(this.url('/regnskab/' + cvrnummer))
  }

  static hentVirksomhedsdata(cvrnummer) {
    return this._call(this.url('/cvr/' + cvrnummer))
  }

  static hentEjerGraf(cvrnummer) {
    return this._call(this.url('/cvr/graf/' + cvrnummer))
  }

  static hentDeltagerGraf(enhedsnummer) {
    return this._call(this.url('/cvr/deltagergraf/' + enhedsnummer))
  }

  static hentVirksomhedGraf(cvrnummer) {
    return this._call(this.url('/cvr/virksomhedgraf/' + cvrnummer))
  }

  static soeg(soegning) {
    return this._call(this.url('/cvr/search/' + soegning))
  }

  static soegVirkr(soegning, page=1, pagesize=20) {
    const url = '/cvr/searchVirkr/'+soegning+'?page='+page+'&pagesize='+pagesize;
    return this._call(this.url(url));
  }

  static hentDeltager(enhedsnummer) {
    return this._call(this.url('/cvr/deltager/'+enhedsnummer))
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

