package voina_i_mir_multi_thread_reading_commas;


import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CommasAnalyzer {
    private int threads;
    private AtomicInteger commaCounter;
    private File file;

    public CommasAnalyzer(String filePath, int threads) {
        if (filePath.endsWith(".txt")) {
            File file = new File(filePath);
            if (file.exists()) {
                this.file = file;
            }
        } else {
            throw new ExceptionInInitializerError("Ops, check your file!");
        }
        this.commaCounter = new AtomicInteger(0);
        if (threads > 0) {
            this.threads = threads;
        } else {
            throw new ExceptionInInitializerError("Incorrect thread numbers");
        }
    }

    private void countCommas(String string) {
        int commaCount = 0;

        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == ',') {
                commaCount++;
            }
        }
        this.commaCounter.getAndAdd(commaCount);
    }

    public void countCommasWithProvidedThreads() {
        long startTime = System.currentTimeMillis();
        String[] textPerThread = new String[this.threads];

        StringBuilder allText = new StringBuilder();
        try (FileReader fr = new FileReader(this.file)) {
            int content;
            while ((content = fr.read()) != -1) {
                allText.append((char) content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int charsPerThread = allText.length() / this.threads;

        int currentChar = 0;
        for (int i = 0; i < threads; i++) {
            if (i == threads - 1) {
                textPerThread[i] = allText.substring(currentChar);
                break;
            }
            textPerThread[i] = allText.substring(currentChar, currentChar + charsPerThread);
            currentChar += charsPerThread;
        }

        for (int i = 0; i < this.threads; i++) {
            int finalI = i;
            Thread t = new Thread(() -> countCommas(textPerThread[finalI]));
            t.start();
            try{
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println(endTime-startTime);

    }


}
