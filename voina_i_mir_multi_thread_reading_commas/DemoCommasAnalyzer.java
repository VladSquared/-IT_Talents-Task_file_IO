package voina_i_mir_multi_thread_reading_commas;

public class DemoCommasAnalyzer {
    public static void main(String[] args) {
        for (int i = 1; i < 50; i++) {
            CommasAnalyzer test2 = new CommasAnalyzer("Voina_i_mir_xN.txt", i);
            test2.countCommasWithProvidedThreads();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
