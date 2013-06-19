package com.beinformed.research.labs.dialogprogress;

class Path implements Comparable<Path>{
	private String lookup;
	private String[] path;
	
	public Path(Observation obs, boolean answers) {
		path = new String[obs.getNoQuestions()];
		fillPath(obs, answers);
		createLookup();
	}
	private Path() {
	}

	private void fillPath(Observation obs, boolean answers) {
		int i = 0;
		for(Question q : obs.getQuestions())
			path[i++] = q.getId() + (answers ? "=" + q.getAnswer() : "");			
	}
	private void createLookup() {
		String result = path[0];
		for(int i = 1; i < path.length; i++)
			result += "-" + path[i];
		lookup = result;
	}

	public Path getParent() {
		if(path.length < 2)
			return this;
		
		Path parent = new Path();
		parent.path = new String[path.length - 1];
		
		for(int i = 0; i < path.length - 1; i++)
			parent.path[i] = path[i];
		parent.createLookup();
		
		return parent;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Path)) return false;
		
		Path p = (Path) obj;
		return p.lookup.equals(lookup);
	}
	@Override
	public int hashCode() {
		return lookup.hashCode();
	}
	@Override
	public int compareTo(Path other) {
		return lookup.compareTo(other.lookup);
	}
}