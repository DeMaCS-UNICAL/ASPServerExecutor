package model;

import java.util.concurrent.locks.Lock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.websocket.RemoteEndpoint;
import javax.websocket.RemoteEndpoint.Async;

import it.unical.mat.embasp.languages.asp.AnswerSets;
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
	private String REGEX_PATH = "(\\/[a-zA-Z0-9\\._\\-]+)+(:)*";
	private String REGEX_PATH2 = "usage:\\s((\\/[a-zA-Z0-9\\._\\-]+)+)";

	/**
	 * Cotruttore della classe, inizializza l'oggetto RemoteEndpoint.Async
	 * 
	 * @param remote
	 *            Riceve l'oggetto RemoteEndpoint.Async per poter inviare i
	 *            messaggi in maniera asincrona
	 * @param lock
	 */
	public MyCallback(RemoteEndpoint.Async remote, Lock lock) {
		this.remote = remote;
		this.lock = lock;

	}
	
	@Override
	public void callback(Output output) {
		AnswerSets answerSets = (AnswerSets) output;
		Result r = new Result();
		if (answerSets.getAnswersets().size() == 0) {
			String error = hidePath(answerSets.getErrors());
			r.setError(error);
			System.out.println("error: "+ error); // debug string
		} else {
			String result = hidePath(answerSets.getAnswerSetsString());
			r.setModel(result);
			System.out.println(result); // debug string
		}
		lock.lock();
		remote.sendText(r.toJson());
		lock.unlock();
	}
	
	public String hidePath(String result)
	{
		String newResult = result;
		Pattern regex = Pattern.compile(REGEX_PATH);
		Pattern regex2 = Pattern.compile(REGEX_PATH2);
		Matcher m = regex.matcher(result);
		Matcher m2 = regex2.matcher(result);
		if (m.find())
		{
			newResult = result.replaceAll(m.group(0), "");
		}
		if (m2.find()) {
			newResult = result.replaceAll(m2.group(1), "dlv");
		}
		return newResult;
	}

}
