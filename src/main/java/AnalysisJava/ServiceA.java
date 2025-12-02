package AnalysisJava;

public class ServiceA {
	public void processData() {
        System.out.println("ServiceA: processData()");
        int value = computeValue(5);
        Utils.log("Computed value: " + value);
    }

    private int computeValue(int x) {
        return MathHelper.square(x) + MathHelper.increment(x);
    }
}
