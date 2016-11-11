package model;

import javax.websocket.RemoteEndpoint;
import javax.websocket.RemoteEndpoint.Async;

import it.unical.mat.embasp.asp.AnswerSets;
import it.unical.mat.embasp.base.Callback;
import it.unical.mat.embasp.base.Output;
/**
 * Implementa la classe Callback per comunicare il risultato del solver ed eseguirlo in maniera asincrona
 *
 */
public class MyCallback implements Callback {
	private Async remote;

	/**
	 * Costruttore della classe, inizializza l'oggetto RemoteEndpoint.Async
	 * @param remote Riceve l'oggetto RemoteEndpoint.Async per poter inviare i messaggi in maniera asincrona
	 */
	public MyCallback(RemoteEndpoint.Async remote) {
		this.remote = remote;
		
	}

	@Override
	public void callback(Output output) {
		AnswerSets answerSets = (AnswerSets) output;
		Result r = new Result();
		if (answerSets.getAnswersets().isEmpty()) {
			 r.setError("Sorry, there aren't answer sets for this program");
			
		} else {
			r.setModel(answerSets.getAnswerSetsString());

		}
		remote.sendText(r.toJson());
	}

}
