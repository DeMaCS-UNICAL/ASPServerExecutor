package control;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.lang.reflect.Type;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import it.unical.mat.embasp.asp.AnswerSets;
import it.unical.mat.embasp.base.Callback;
import it.unical.mat.embasp.base.Output;
import model.MyCallback;
import model.Option;
import model.Result;
import service.SolverDLV;

/**
 * Servlet implementation class provaServelt
 */
@WebServlet("/home")
public class HomeController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public HomeController() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		BufferedReader br = new BufferedReader(request.getReader());
		String json = null;
		if (br != null) {
			json = br.readLine();
		}
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(json);
		JsonObject object = element.getAsJsonObject();
		String model = "";
		Gson gson = new Gson();
		String engine = gson.fromJson(object.get("engine"), String.class);
		String program = gson.fromJson(object.get("program"), String.class);
		Type optionType = new TypeToken<ArrayList<Option>>() {
		}.getType();
		ArrayList<Option> options = gson.fromJson(object.get("option"), optionType);
		Result result= new Result();
		switch (engine) {
		case "dlv":
			SolverDLV service = new SolverDLV(program);
			if (options.get(0).getName().equals("option")){
				MyCallback callback = new MyCallback(result, response);
				service.solve(callback);
			}else{
				MyCallback callback = new MyCallback(result, response);
				service.solveWithOption(options,callback);}
		
			break;

		default:
			break;
		}

	}
	
		
	}


