package ui;

import java.time.format.DateTimeFormatter;

import business.clientes.*;
import business.funcionarios.Funcionario;
import business.funcionarios.ISSFuncionarios;
import business.funcionarios.SSFuncionariosFacade;
import business.servicos.ISSServicos;
import business.servicos.SSServicosFacade;
import business.servicos.Servico;
import business.workshop.Horario;
import business.workshop.ISSWorkshop;
import business.workshop.SSWorkshopFacade;
import business.workshop.Workshop;

import java.time.LocalDateTime;
import java.util.*;

public class ESIdealUI{

    private Menu menu;

    private Scanner is;

    private ISSClientes clientes;
    private ISSFuncionarios funcionarios;
    private ISSServicos servicos;
    private ISSWorkshop workshops;

    private Workshop workshop;

    private Funcionario funcionario;


    public ESIdealUI(){
        this.menu = new Menu();
        this.menu.setHandler(1,  () -> LogIn());
        this.menu.setHandler(2,  () -> RegistarFuncionário());
        this.menu.setHandler(2,  () -> IniciarTurno());
        this.menu.setHandler(2,  () -> AtualizarCleinte());
        this.menu.setHandler(3,  () -> IniciarTurno());
        this.menu.setHandler(4,  () -> TerminarTurno());
        this.menu.setHandler(4,  () -> PedirServico());
        this.menu.setHandler(4,  () -> IniciarServico());
        this.menu.setHandler(4,  () -> RegistarCliente());
        this.menu.setHandler(5,  () -> TerminarServico());
        this.menu.setHandler(6,  () -> AtualizarMorada());
        this.menu.setHandler(6,  () -> AtualizarContacto());
        this.menu.setHandler(6,  () -> AtualizarVeiculos());
        this.is = new Scanner(System.in);
        this.clientes = new SSClientesFacade();
        this.funcionarios = new SSFuncionariosFacade();
        this.servicos = new SSServicosFacade();
        this.workshops = new SSWorkshopFacade();
        this.workshop = null;
        this.funcionario = null;
    }

    private void LogIn() {
        if (funcionario != null) {
            funcionarios.atualizafuncionario(funcionario);
        }
        boolean b = false;
        while (!b) {
            is.nextLine();
            System.out.println("\n *** Insira o seu username *** ");
            String username = is.nextLine();
            System.out.println("***************************************************************** ");
            System.out.println("\n *** Insira a sua password ***");
            String password = is.nextLine();
            if (workshop.getFuncionarios().containsKey(username)) {
                b = funcionarios.validafuncionariopassword(username, password);
                if (!b) {
                    System.out.println("username ou password incorreta, tente novamente!");
                } else {
                    this.funcionario = funcionarios.getFuncionarios().get(username);
                }
                if (this.funcionario.getIsAdmin()) {
                    this.menu.setJ(2);
                    if (this.funcionario.getEmTurno().isEmTurno()){
                        this.menu.setJ(7);
                    }
                    if (this.funcionario.getEmTurno().getFazerServico().isFazerServico()){
                        this.menu.setJ(8);
                    }

                } else {
                    this.menu.setJ(3);
                    if (this.funcionario.getEmTurno().isEmTurno()){
                        this.menu.setJ(4);
                    }
                    if (this.funcionario.getEmTurno().getFazerServico().isFazerServico()){
                        this.menu.setJ(5);
                    }
                }
            }
            else {
                System.out.println("Funcionário nao registado nesta Workshop ou nao existente");
            }
        }
    }

    private void RegistarFuncionário () {
        System.out.println("Indique o nome do novo funcionario: ");
        String nome = is.nextLine();
        System.out.println("Indique a idade do novo funcionario: ");
        int idade = is.nextInt();
        is.nextLine();
        System.out.println("Atribua um username do novo funcionário: ");
        String username = is.nextLine();
        System.out.println("Atribua uma password ao novo funcionário: ");
        String password = is.nextLine();
        boolean b = true;
        boolean isAdmin = false;
        while (b) {
            System.out.println("O novo funcionário é um Admin (1 = sim, 0 = nao)");
            int admin = is.nextInt();
            is.nextLine();
            if (admin == 1) {
                isAdmin = true;
                b = false;
            } else if (admin == 0) {
                b = false;
            } else {
                System.out.println("valor invalido");
            }
        }
        funcionarios.registafuncionario(nome, idade, username, password, isAdmin);
        workshop.RegistaFuncionarios(nome, idade, username, password, isAdmin);
    }

    private void IniciarTurno (){
        System.out.println("\n *** Insira o numero do posto de trabalho: *** ");
        int posto = is.nextInt();
        is.nextLine();
        if(workshop.getPosto().containsKey(posto)) {
            this.funcionario = this.funcionarios.iniciarTurno(this.funcionario.getUsername(), workshop.getPosto().get(posto));
            if (this.funcionario.getEmTurno().isEmTurno()) {
                if(funcionario.isAdmin()){
                    this.menu.setJ(7);
                }
                else {
                    this.menu.setJ(4);
                }
            } else {
                System.out.println("Nao tem competencia para trabalhar nesse posto");
            }
        }
        else{
            System.out.println("*******   posto invalido   *******");
        }
    }

    private void TerminarTurno (){
        this.funcionario = this.funcionarios.finalizarTurno(this.funcionario.getUsername());
        if (this.funcionario.isAdmin()){
            this.menu.setJ(2);
        }
        else {
            this.menu.setJ(3);
        }
    }

    private void IniciarServico (){
        int posto = this.funcionario.getEmTurno().getId_posto();
        if (this.workshop.getPosto().get(posto).getServicosPorFazer().isEmpty()){
            System.out.println("***** Não há serviços para realizar *****");
        }
        else {
            System.out.println("***** LISTA DE VEICULOS E SERVICOS QUE PODE REALIZAR *****");
            Map<String, Veiculo> veiculos = new HashMap<>();
            for (Map.Entry<Veiculo, List<Servico>> entry : this.workshop.getPosto().get(posto).getServicosPorFazer().entrySet()) {
                veiculos.put(entry.getKey().getMatricula(), entry.getKey());
                System.out.println("\n" + entry.getKey().getMarca() + " " + entry.getKey().getModelo() + " " + entry.getKey().getMatricula());
                for (Servico servico : entry.getValue()) {
                    if(funcionario.getServicos().containsKey(servico.getId())) {
                        System.out.println("\n" + servico.getId() + "-" + servico.getNome());
                    }
                }
            }
            System.out.println("*** Digite a matricula do veiculo ***");
            String matricula = is.nextLine();
            if (veiculos.containsKey(matricula)) {
                System.out.println("*** Digite o ID do serviço ***");
                int servico = is.nextInt();
                is.nextLine();
                boolean b = false;
                for (Servico s : this.workshop.getPosto().get(posto).getServicosPorFazer().get(veiculos.get(matricula))){
                    if (s.getId() == servico){
                        b = true;
                        break;
                    }
                }
                if(b) {
                    this.funcionario = this.funcionarios.iniciarServico(veiculos.get(matricula), servicos.getServicos().get(servico), funcionario.getUsername());
                    if (this.funcionario.isAdmin()){
                        this.menu.setJ(8);
                    }else {
                        this.menu.setJ(5);
                    }
                }
                else {
                    System.out.println("Serviço nao compativel com veiculo");
                }
            }
            else{
                System.out.println("Veiculo selecionado nao existe");
            }
        }
    }
    private void TerminarServico(){
        this.funcionario = this.funcionarios.finalizarServico(funcionario.getUsername());
        this.workshop = this.workshops.finalizarServico(this.workshop.getId(),this.funcionario.getEmTurno().getId_posto() ,this.funcionario.getEmTurno().getFazerServico().getVeiculo(),this.funcionario.getEmTurno().getFazerServico().getServico());
        if (this.funcionario.isAdmin()){
            this.menu.setJ(7);
        }else{
            this.menu.setJ(4);
        }
    }

    private void PedirServico(){
        System.out.println("*** Digite o nif do cliente ***");
        String nif = is.nextLine();
        if (clientes.getClientes().containsKey(nif)) {
            Cliente c = clientes.getClientes().get(nif);
            for (Map.Entry<String, Veiculo> entry : c.getVeiculos().entrySet()) {
                System.out.println("\n" + entry.getValue().getMarca() + " " + entry.getValue().getModelo() + " " + entry.getValue().getMatricula());
            }
            System.out.println("*** Digite a matricula do veiculo ***");
            String matricula = is.nextLine();
            if (c.getVeiculos().containsKey(matricula)) {
                List<Map.Entry<Integer, Servico>> sortedServicos = new ArrayList<>(servicos.getServicos().entrySet());

                Collections.sort(sortedServicos, Comparator.comparingInt(Map.Entry::getKey));

                for (Map.Entry<Integer, Servico> entry : sortedServicos) {
                    System.out.println(entry.getKey() + "- " + entry.getValue().getNome());
                }
                System.out.println("*** Digite o ID do servico ***");
                int id = is.nextInt();
                is.nextLine();
                if (servicos.getServicos().containsKey(id)) {
                    clientes.PedirServico(nif, matricula, servicos.getServicos().get(id));
                    this.workshop = workshops.PedirServico(clientes.getClientes().get(nif).getVeiculos().get(matricula), servicos.getServicos().get(id), workshop.getId());
                }
                else {
                    System.out.println("ID de servico inserido invalido");
                }
            }
            else {
                System.out.println("matricula inserida invalida");
            }
        }
        else{
            System.out.println("nif do cliente inserido invalido");
        }

    }

    public void AtualizarCleinte (){
        this.menu.setJ(6);
    }

    public void AtualizarMorada (){
        System.out.println("\n Insira o nif do cliente ");
        String nif = is.nextLine();
        System.out.println("\n Insira a nova morada do cliente ");
        String morada = is.nextLine();
        clientes.getClientes().get(nif).setMorada(morada);
    }
    public void AtualizarContacto (){
        System.out.println("\n Insira o nif do cliente ");
        String nif = is.nextLine();
        System.out.println("\n Insira o novo contacto do cliente ");
        String contacto = is.nextLine();
        clientes.getClientes().get(nif).setContacto(contacto);
    }
    public void AtualizarVeiculos (){
        System.out.println("\n Insira o nif do cliente ");
        String nif = is.nextLine();
        System.out.println("\n ******   REGISTAR NOVO VEICULO   ****** ");
        System.out.println("\n*****   Tipo de Combustivel    *****\n" +
                "     *  1- gasolina\n" +
                "     *  2- diesel\n" +
                "     *  3- eletrico\n" +
                "     *  4- hibrido gasolina\n" +
                "     *  5- hibrido diesel");
        int type = is.nextInt();
        is.nextLine();
        System.out.println("\n Insira a matricula do veiculo ");
        String matricula = is.nextLine();
        System.out.println("\n insira a marca do veiculo ");
        String marca = is.nextLine();
        System.out.println("\n Insira o modelo do veiculo ");
        String modelo = is.nextLine();
        System.out.println("\n Insira a quilometragem do veiculo ");
        int quilometragem = is.nextInt();
        is.nextLine();
        System.out.println("\n Insira o ano de fabrico do veiculo ");
        int ano = is.nextInt();
        is.nextLine();
        System.out.println("\n Insira a data da ultima revisao do veiculo (formato yyyy-MM-ddTHH:mm:ss) ");
        String input = is.nextLine();
        LocalDateTime data_ultima_revisao = LocalDateTime.parse(input, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        Veiculo veiculo = new Veiculo(type, matricula, marca, modelo, quilometragem, ano, data_ultima_revisao,new FichaVeiculo(), nif);
        clientes.getClientes().get(nif).addVeiculo(matricula, veiculo);
    }
    public void RegistarCliente (){
        System.out.println("\n ******   REGISTAR CLIENTE   ****** ");
        System.out.println("\n ******   Escolha uma Estaçao de Serviço    ***** ");
        for(Map.Entry<Integer, Workshop> entry : workshops.getWorkshops().entrySet()){
            System.out.println(entry.getKey() + "- " + entry.getValue());
        }
        int n = is.nextInt();
        is.nextLine();
        System.out.println("\n Insira o primeiro e ultimo nome do cliente ");
        String nome = is.nextLine();
        System.out.println("\n Insira o nif do cliente ");
        String nif = is.nextLine();
        System.out.println("\n Insira a morada do cliente ");
        String morada = is.nextLine();
        System.out.println("\n Insira o contacto do cliente ");
        String contacto = is.nextLine();
        System.out.println("\n Insira o numero de veiculos que ficarão associados ao cliente ");
        int num = is.nextInt();
        is.nextLine();
        Map<String, Veiculo> veiculos = new HashMap<>();
        while (num > 0) {
            System.out.println("\n ******   REGISTAR NOVO VEICULO   ****** ");
            System.out.println("\n*****   Tipo de Combustivel    *****\n" +
                    "     *  1- gasolina\n" +
                    "     *  2- diesel\n" +
                    "     *  3- eletrico\n" +
                    "     *  4- hibrido gasolina\n" +
                    "     *  5- hibrido diesel");
            int type = is.nextInt();
            is.nextLine();
            System.out.println("\n Insira a matricula do veiculo ");
            String matricula = is.nextLine();
            System.out.println("\n insira a marca do veiculo ");
            String marca = is.nextLine();
            System.out.println("\n Insira o modelo do veiculo ");
            String modelo = is.nextLine();
            System.out.println("\n Insira a quilometragem do veiculo ");
            int quilometragem = is.nextInt();
            is.nextLine();
            System.out.println("\n Insira o ano de fabrico do veiculo ");
            int ano = is.nextInt();
            is.nextLine();
            System.out.println("\n Insira a data da ultima revisao do veiculo (formato yyyy-MM-ddTHH:mm:ss) ");
            String input = is.nextLine();
            LocalDateTime data_ultima_revisao = LocalDateTime.parse(input, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            Veiculo veiculo = new Veiculo(type, matricula, marca, modelo, quilometragem, ano, data_ultima_revisao,new FichaVeiculo(), nif);
            veiculos.put(matricula, veiculo);
            num--;
        }
        int valorvouchers = 0;
        if (workshops.getWorkshops().get(n).getLocalidade().equals("Gualtar")){
            valorvouchers = 50;
        }
        clientes.registacliente(nif, nome, morada, contacto, valorvouchers, veiculos);
        workshops.registacliente(n, nif, nome, morada, contacto, valorvouchers, veiculos);
    }

    public void run() {
        boolean b = true;
        while (b) {
            System.out.println("\n ******   BEM VINDO À ESIDEAL   ****** ");
            System.out.println("\n Insira o id correspondente á oficina ou digite 0 pra fechar o programa: ");
            int id = is.nextInt();
            is.nextLine();

            if (id != 0) {
                if (workshops.getWorkshops().containsKey(id)) {
                    if (this.workshop != null) {
                        workshops.atualizaworkshop(workshop);
                    }
                    this.workshop = workshops.getWorkshops().get(id);
                    this.menu.setJ(1);
                    this.menu.runW(this.workshop.getNome());
                }
                else {
                    System.out.println("ID de Workshop invalido");
                }
            }
            else {
                b = false;
            }

        }
        System.out.println("Até breve!...");
    }
    
}