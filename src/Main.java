
public class Main {

    public static void main(String[] args) {

        // create the markov chain
        MarChain chain = new MarChain("./TestFile.txt");
        chain.print(20, "Output.txt");

    }
}