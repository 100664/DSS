package ui;

import business.funcionarios.Funcionario;
import business.workshop.Workshop;
import org.w3c.dom.ls.LSOutput;

import java.util.*;


public class Menu{

    private int j;
    private Map<Integer, List<Handler>> handlers;

    private static Scanner is = new Scanner(System.in);
    public interface Handler{
        void execute();
    }

    public void setJ(int j) {
        this.j = j;
    }

    public Menu(){

        this.j = 1;

        this.handlers = new HashMap<>();
    }
    public void setHandler(int j, Handler h) {
        if (this.handlers.containsKey(j)){
            List<Handler> handler = this.handlers.get(j);
            handler.add(h);
            this.handlers.put (j, handler);
        }
        else {
            List<Handler> handler = new ArrayList<>();
            handler.add(h);
            this.handlers.put (j, handler);
        }
    }

    public void runW(String workshop) {
        int i = this.j;
        System.out.println("\n *** BEM VINDO À OFICINA" + workshop.toUpperCase() + " *** ");
        int op = 1;
        do {
            switch (this.j) {
                case 1:
                    displayOptions();
                    break;
                case 2:
                    displayOptions2();
                    break;
                case 3:
                    displayOptions3();
                    break;
                case 4:
                    displayOptions4();
                    break;
                case 5:
                    displayOptions5();
                    break;
                case 6:
                    displayOptions6();
                    break;
                case 7:
                    displayOptions4();
                    break;
                case 8:
                    displayOptions5();
                    break;
            }
            if (this.j == 7 || this.j == 8){
                i = this.j - 3;
            }
            else {
                i = this.j;
            }
            op = readOption();
            if (op == -1) {
                System.out.println("Opção indisponível! Tente novamente.");
            }
            else if (op != 0 && op-1 < this.handlers.get(i).size()){
                this.handlers.get(i).get(op-1).execute();
            }

        }while (this.j != 0);
    }
    private void displayOptions(){
        System.out.print("*********************     OPÇOES     *********************: \n");
        System.out.println("1- LOGIN");
        System.out.println("0- SAIR");
    }
    private void displayOptions2(){
        System.out.print("*********************     OPÇOES     *********************: \n");
        System.out.println("1- REGISTAR FUNCIONARIO");
        System.out.println("2- INICIAR TURNO");
        System.out.println("3- ATUALIZAR CLIENTE");
        System.out.println("0- SAIR");
    }

    private void displayOptions3(){
        System.out.print("*********************     OPÇOES     *********************: \n");
        System.out.println("1- INICIAR TURNO");
        System.out.println("0- SAIR");
    }
    private void displayOptions4(){
        System.out.print("*********************     OPÇOES     *********************: \n");
        System.out.println("1- TERMINAR TURNO");
        System.out.println("2- REGISTAR SERVIÇO");
        System.out.println("3- INICIAR SERVIÇO");
        System.out.println("4- REGISTAR CLIENTE");
        System.out.println("0- SAIR");
    }
    private void displayOptions5(){
        System.out.print("*********************     OPÇOES     *********************: \n");
        System.out.println("1- TERMINAR SERVICO");
        System.out.println("0- SAIR");
    }
    private void displayOptions6(){
        System.out.print("*********************     OPÇOES     *********************: \n");
        System.out.println("1- ATUALIZAR MORADA");
        System.out.println("2- ATUALIZAR CONTACTO");
        System.out.println("3- ADICIONAR VEICULO");
        System.out.println("0- SAIR");
    }
    /** Ler uma opção válida */
    private int readOption() {
        int op;

        System.out.print("Opção: ");
        try {
            String line = is.nextLine();
            op = Integer.parseInt(line);
        }
        catch (NumberFormatException e) { // Não foi inscrito um int
            op = -1;
        }
        if (op<0) {
            op = -1;
        }
        if (op == 0){
            MudaJ();
        }
        return op;
    }

    private void MudaJ(){
        if (this.j == 1){
            this.j = 0;
        }
        if (this.j == 2 || this.j == 3 || this.j == 4 || this.j == 5 || this.j == 7 || this.j == 8){
            this.j = 1;
        }
        if (this.j == 6){
            this.j = 4;
        }
    }


}
