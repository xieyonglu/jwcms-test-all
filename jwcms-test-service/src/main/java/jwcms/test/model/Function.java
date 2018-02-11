package jwcms.test.model;

public class Function {

	private Long id;
	
	private String functionName;
	
	private String functionPath;

	public Function(Long id, String functionName, String functionPath) {
		this.id = id;
		this.functionName = functionName;
		this.functionPath = functionPath;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public String getFunctionPath() {
		return functionPath;
	}

	public void setFunctionPath(String functionPath) {
		this.functionPath = functionPath;
	}
	
}
