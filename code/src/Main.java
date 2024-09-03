import business.workshop.Workshop;
import ui.ESIdealUI;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        try {
            new ESIdealUI().run();
        } catch (Exception e) {
        e.printStackTrace();
        System.out.println("Não foi possível arrancar: " + e.getMessage());
        }
    }
}