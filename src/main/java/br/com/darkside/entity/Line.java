package br.com.darkside.entity;

public class Line implements Comparable<Line> {
	private int numberOfLine;
	private String content;
	private Path path;
	private Variable variable;
	
	public Line() {
		super();
	}

	public Line(String content) {
		super();				
		Variable variable = new Variable();
		String[] pieces = content.split("=");
		if(pieces.length == 2){
			variable.setName(pieces[0].replace("export", "").replaceAll(" ", ""));
			variable.setValue(pieces[1].replace("\"", ""));		
			if(variable.getName().equals(Path.PATH_NAME)) {
				Path path = new Path();
	    		path.setName(variable.getName());
	    		path.setValue(variable.getValue());
	    		path.setAdditionalPath(variable.getAdditionalPath());		    		
	    		this.path = path;	    			
			} else {
				this.variable = variable;
			}
		} else {
			this.content = content;
		}
	}

	public String getContent() {
		return content;
	}

	public Path getPath() {
		return path;
	}

	public Variable getVariable() {
		return variable;
	}

	public void setPath(Path path) {
		this.path = path;
	}

	public void setVariable(Variable variable) {
		this.variable = variable;
	}

	public int getNumberOfLine() {
		return numberOfLine;
	}

	public void setNumberOfLine(int numberOfLine) {
		this.numberOfLine = numberOfLine;
	}

	@Override
	public int compareTo(Line line ) {
		if (numberOfLine > line.getNumberOfLine())
			return 1;
	
		if (numberOfLine == line.getNumberOfLine())
			return 0;
		
		return -1;			
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + numberOfLine;
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		result = prime * result + ((variable == null) ? 0 : variable.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Line other = (Line) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (numberOfLine != other.numberOfLine)
			return false;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		if (variable == null) {
			if (other.variable != null)
				return false;
		} else if (!variable.equals(other.variable))
			return false;
		return true;
	}
}