import 'whatwg-fetch';

export default class APIHelper {

  static hentNoegletal(cvrnummer) {
    const url = 'http://localhost:9092/regnskab/' + cvrnummer;

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

