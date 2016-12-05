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
		if (answerSets.getAnswerSetsString().equals("")) {
			r.setError("Sorry, there is an error");
		} else {
			StringBuilder model = new StringBuilder(answerSets.getAnswerSetsString());
			int p = answerSets.getAnswerSetsString().indexOf("clingo");
			if (p != -1) {
				int v = answerSets.getAnswerSetsString().indexOf("Reading");
				int s = answerSets.getAnswerSetsString().indexOf("Solving");
				int a = answerSets.getAnswerSetsString().indexOf("Answer");
				int sa= answerSets.getAnswerSetsString().indexOf("SATISFIABLE");
				int m = answerSets.getAnswerSetsString().indexOf("Models");
				int c = answerSets.getAnswerSetsString().indexOf("Calls");
				int t = answerSets.getAnswerSetsString().indexOf("Time");
				int cpu= answerSets.getAnswerSetsString().indexOf("CPU");
										
				model.insert(cpu, '\n');
				model.insert(t, '\n');
				model.insert(c, '\n');
				model.insert(m, '\n');
				model.insert(sa, '\n');
				model.insert(a, '\n');
				model.insert(s, '\n');
				model.insert(v, '\n');
			}
			r.setModel(model.toString());
		}
		System.out.println(answerSets.getAnswerSetsString()); // debug string
		lock.lock();
		remote.sendText(r.toJson());
		lock.unlock();
	}

}
