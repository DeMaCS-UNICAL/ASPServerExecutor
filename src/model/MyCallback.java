package model;

import java.util.concurrent.locks.Lock;

import javax.websocket.RemoteEndpoint;
import javax.websocket.RemoteEndpoint.Async;

import it.unical.mat.embasp.asp.AnswerSets;
import it.unical.mat.embasp.base.Callback;
import it.unical.mat.embasp.base.Output;

/**
 * Implementa la classe Callback per comunicare il risultato del solver ed
 * eseguirlo in maniera asincrona
 *
 */
public class MyCallback implements Callback {
	private Async remote;
	private Lock lock;
	

	/**
	 * Cotruttore della classe, inizializza l'oggetto RemoteEndpoint.Async
	 * 
	 * @param remote
	 *            Riceve l'oggetto RemoteEndpoint.Async per poter inviare i
	 *            messaggi in maniera asincrona
	 * @param lock 
	 */
	public MyCallback(RemoteEndpoint.Async remote,Lock lock) {
		this.remote = remote;
		this.lock = lock;

	}

	@Override
	public void callback(Output output) {
		AnswerSets answerSets = (AnswerSets) output;
		Result r = new Result();
		if (answerSets.getAnswersets().equals("")) {
			r.setError("Sorry, there is an error");
		} else {
			r.setModel(answerSets.getAnswerSetsString());
		}
		System.out.println(answerSets.getAnswerSetsString()); // debug string
		lock.lock();
		remote.sendText(r.toJson());
		lock.unlock();
	}

}
