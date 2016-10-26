package model;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import it.unical.mat.embasp.asp.AnswerSets;
import it.unical.mat.embasp.base.Callback;
import it.unical.mat.embasp.base.Output;

public class MyCallback implements Callback {

	public Result result;
	HttpServletResponse response;
	
	public MyCallback(Result result,HttpServletResponse response ) {
		this.result=result;
		this.response=response;
	}
	
	@Override
	public void callback(Output output) {
		AnswerSets answerSets=(AnswerSets)output;
		if (answerSets.getAnswersets().isEmpty()){
			result.setModel("Sorry, there aren't answer sets for this program");
		}else{
			result.setModel(answerSets.getAnswerSetsString());
			
		}
		try {
			response.getWriter().write(result.toJson());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

}
