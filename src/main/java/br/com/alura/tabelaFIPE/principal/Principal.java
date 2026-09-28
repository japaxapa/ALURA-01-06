package br.com.alura.tabelaFIPE.principal;

import br.com.alura.tabelaFIPE.model.Dados;
import br.com.alura.tabelaFIPE.model.Modelos;
import br.com.alura.tabelaFIPE.model.Veiculo;
import br.com.alura.tabelaFIPE.service.ConsumoAPI;
import br.com.alura.tabelaFIPE.service.ConversorDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Principal {
    private Scanner scanner = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConversorDados conversor = new ConversorDados();


    public void exibirMenu() {
        var menu = """
                *** OPÇÕES ***
                Carro
                Moto
                Caminhão
                
                Digite uma das opções para consulta:
                """;

        System.out.println(menu);
        var opcao = scanner.nextLine();

        String endereco = consumoAPI.escolhaURLTipo(opcao);

        var json = consumoAPI.obterDados(endereco);
        System.out.println(json);

        var marcas = conversor.converterLista(json, Dados.class);
        marcas.stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("Informe o código da marca para a consulta: ");
        var codigoMarca = scanner.nextLine();

        endereco = endereco + "/" + codigoMarca + "/modelos/";

        json = consumoAPI.obterDados(endereco);
        var modelosLista = conversor.converterDados(json, Modelos.class);

        System.out.println("Lista dos modelos da marca: ");
        modelosLista.modelos().stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("Digite um trecho do nome do veículo para busca: ");
        var trechoDoNome = scanner.nextLine();

        modelosLista.modelos().stream()
                .filter(m -> m.nome().toLowerCase().contains(trechoDoNome.toLowerCase()))
                .forEach(System.out::println);


        System.out.println("Digite o código do veículo para continuar a busca: ");
        var codCarro = scanner.nextLine();

        endereco = endereco + "/" + codCarro + "/anos";
        json = consumoAPI.obterDados(endereco);
        List<Dados> anos = conversor.converterLista(json, Dados.class);
        List<Veiculo> veiculos = new ArrayList<>();

        for (Dados ano : anos) {
            var enderecoAnos = endereco + "/" + ano.codigo();
            json = consumoAPI.obterDados(enderecoAnos);
            Veiculo veiculo = conversor.converterDados(json, Veiculo.class);
            veiculos.add(veiculo);
        }

        System.out.println("\nTodos os veículos filtrados com avaliações por ano: ");
        veiculos.forEach(System.out::println);
    }
}
