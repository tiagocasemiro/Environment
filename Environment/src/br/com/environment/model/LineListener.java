package br.com.environment.model;

import br.com.environment.model.entity.Path;
import br.com.environment.model.entity.Variable;

public interface LineListener {
	void onPathListener(Path path);
	void onLineListener(Variable variable);
	void onFinish();
}
