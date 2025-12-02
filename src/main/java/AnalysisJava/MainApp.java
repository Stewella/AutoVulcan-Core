package AnalysisJava;

public class MainApp {
	public static void main(String[] args) {
        System.out.println("Starting App...");

        ServiceA serviceA = new ServiceA();
        ServiceB serviceB = new ServiceB();
        Repository repo = new Repository();

        serviceA.processData();
        serviceB.runTask(repo);

        Utils.printSummary();
    }
}
