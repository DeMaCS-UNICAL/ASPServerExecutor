package model;

import it.unical.mat.embasp.asp.AnswerSets;
import it.unical.mat.embasp.base.Callback;
import it.unical.mat.embasp.base.Output;

public class MyCallback implements Callback {

	private Result result;

	public MyCallback(Result result) {
		this.result = result;
	}

	@Override
	public void callback(Output output) {
		AnswerSets answerSets = (AnswerSets) output;
		if (answerSets.getAnswersets().isEmpty()) {
			result.setModel("Sorry, there aren't answer sets for this program");
		} else {
			result.setModel(answerSets.getAnswerSetsString());

		}

	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

}
