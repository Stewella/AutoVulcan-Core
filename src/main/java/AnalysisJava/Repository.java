package AnalysisJava;

public class Repository {
	public String load() {
        Utils.log("Loading data...");
        return "sample data";
    }

    public void save(String data) {
        Utils.log("Saving: " + data);
    }
}
