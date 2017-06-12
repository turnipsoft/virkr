import 'whatwg-fetch';

export default class APIHelper {

  static hentNoegletal(cvrnummer) {
    const localhost  = "localhost";

    console.log(localhost)

    const host = __APIHOST__;

    const url = 'http://'+host+':9092/regnskab/' + cvrnummer;

    return new Promise((resolve, reject) => {
      fetch(url, { code: 'cors' }).then(response => {
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

  static hentVirksomhedsdata(cvrnummer) {
    const url = 'http://virkr.dk:9092/cvr/' + cvrnummer;

    return new Promise((resolve, reject) => {
      fetch(url, { code: 'cors' }).then(response => {
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

