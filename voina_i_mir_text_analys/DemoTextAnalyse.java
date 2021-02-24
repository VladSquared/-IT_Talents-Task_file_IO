package voina_i_mir_text_analys;

public class DemoTextAnalyse {
    public static void main(String[] args) {
        TextAnalyzer test1 = new TextAnalyzer("Voina_i_mir.txt");

        test1.printLinesCount();
        test1.printWordCount();
        test1.printCommaCount();
        test1.printVoinaIliMir();
        test1.printCommon10Words();
        test1.saveWordsInFiles();
    }
}
