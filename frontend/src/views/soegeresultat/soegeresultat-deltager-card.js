import React from 'react';
import { connect } from 'react-redux'
import { visDeltager } from '../../actions/';

const DeltagerCard = (props) => {
  const { deltager, dispatch } = props;

  const navn = deltager.navn;
  var adresse = ""
  if (deltager.adresselinie) {
    adresse = deltager.adresselinie;
  }

  if (deltager.postnr && deltager.postnr!='0') {
    adresse += ", "+ deltager.bylinie;
  }

  const virksomheder = deltager.virksomhedsliste;

  return (
    <div className="card soegeresultatcard" onClick={() => dispatch(visDeltager(deltager.enhedsNummer))} >
      <div className="card-block">
        <h5 className="card-title">{navn}</h5>
        <div className="card-text">
          {adresse? <p>{adresse}</p>:null}
          {virksomheder?<p><b>Associerede virksomheder:</b> {virksomheder}</p>:null}
        </div>
      </div>
    </div>
  )
}
const mapStateToProps = (state, ownProps) => {
  return ownProps
}

export default connect(mapStateToProps)(DeltagerCard);
