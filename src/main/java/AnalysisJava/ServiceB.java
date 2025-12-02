package AnalysisJava;

public class ServiceB {
	public void runTask(Repository repo) {
        System.out.println("ServiceB: runTask()");
        String data = repo.load();
        String result = transform(data);
        repo.save(result);
    }

    private String transform(String input) {
        return input.toUpperCase() + "!";
    }
}
