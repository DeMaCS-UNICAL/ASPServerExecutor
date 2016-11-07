package control;

import java.lang.reflect.Type;
import java.util.ArrayList;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import model.MyCallback;
import model.Option;
import model.Result;
import service.SolverDLV;

@ServerEndpoint("/home")
public class SocketController {

	@OnOpen
	public void onOpen(Session session) {
		// Metodo eseguito all'apertura della connessione

	}

	@OnMessage
	public String onMessage(String message, Session session) { 	// Metodo eseguito alla ricezione di un messaggio, la stringa 'message' rappresenta il messaggio
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(message);
		JsonObject object = element.getAsJsonObject();
		String model = "";
		Gson gson = new Gson();
		String engine = gson.fromJson(object.get("engine"), String.class);
		String program = gson.fromJson(object.get("program"), String.class);
		Type optionType = new TypeToken<ArrayList<Option>>() {
		}.getType();
		ArrayList<Option> options = gson.fromJson(object.get("option"), optionType);
		Result result = new Result();
		switch (engine) {
		case "dlv":
			SolverDLV service = new SolverDLV(program);
			MyCallback callback = new MyCallback(result);
			if (service.checkOptionsDLV(options)) {
				service.solveAsync(options, callback);
				while (result.getModel().equals("")) {
					model = result.toJson();
				}
				model = result.toJson();
				
			} else {
				result.setError("Sorry, these options aren't valid");
				model = result.toJson();
			}
			break;

		default:
			break;
		}

		return model;

	}

	@OnClose
	public void onClose(Session session) {
		// Metodo eseguito alla chiusura della connessione
	}

	@OnError
	public void onError(Throwable exception, Session session) {
		// Metodo eseguito in caso di errore
	}
}