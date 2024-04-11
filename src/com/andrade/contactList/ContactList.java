package src.com.andrade.contactList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * AGENDA
 */
public class ContactList {

  private static final String NOME_ARQUIVO = "src/com/andrade/contactList/data/dados.txt"; //Nome do arquivo de dados
  private static final String SEPARADOR = System.lineSeparator(); //definição do separador de cada item da classe
  private static final List<Contato> listaContatos = new ArrayList<>(); //criação do ArrayList de forma global
  private static long idCounter = 0; //contador dos Ids, foi escolhido o tipo long por não saber o tamanho da agenda da pessoa

  public static class Contato {

    private Long id;
    private String nome;
    private String sobreNome;
    private final List<Telefone> telefones = new ArrayList<>();
  }

  public static class Telefone {

    private String ddd;
    private Long numero;
  }

  public static void main(String[] args) {
    try {
      carregarDados();
      Scanner scanner = new Scanner(System.in);
      menu(scanner);
    } catch (IOException e) {
      System.err.println("Erro de I/O: " + e.getMessage());
    } catch (NumberFormatException e) {
      System.err.println("Erro de formato numérico: " + e.getMessage());
    } catch (Exception e) {
      System.err.println("Erro desconhecido: " + e.getMessage());
    }
  }

  //*FUNÇOES PRINCIPAIS */

  //MENU
  public static void menu(Scanner sc) throws IOException {
    int aux;
    do {
      System.out.println("####################");
      System.out.println("###### AGENDA ######");
      System.out.println("####################");
      imprimeContatosMenu();
      System.out.println(">>>>> Menu <<<<<");
      System.out.println("1 - Adicionar Contato");
      System.out.println("2 - Remover Contato");
      System.out.println("3 - Editar Contato");
      System.out.println("4 - Sair");

      System.out.print("Opção: ");
      aux = (int) garanteRange(sc, 1, 4);

      switch (aux) {
        case 1 -> adicionarContato(sc);
        case 2 -> removerContato(sc);
        case 3 -> editarContato(sc);
        case 4 -> System.out.println("Saindo...");
        default -> System.out.println("Opção inválida. Tente novamente.");
      }
    } while (aux != 4);
  }

  //ADICIONA CONTATOS
  public static void adicionarContato(Scanner sc) throws IOException {
    Contato novoContato = new Contato();
    novoContato.id = ++idCounter;

    System.out.println("Digite o nome do contato:");
    novoContato.nome = sc.nextLine();

    System.out.println("Digite o sobrenome do contato:");
    novoContato.sobreNome = sc.nextLine();

    System.out.println(
            "Digite quantos números de telefone deseja cadastrar (0 para nenhum):"
    );

    int aux = (int) garanteRange(sc, 0, 20);

    //Easter egg para contados com mais de 10 números de telefone haha.
    if (aux > 10) {
      System.out.println(
              "\nUau! Seu contato tem mais de 10 números de telefone?? o.O"
      );
      System.out.println("Bom! vamos cadastrar os números:\n");
    }

    for (int i = 0; i < aux; i++) {
      Telefone telefone = obtemTelefone(sc, i + 1); //Função obtem telefone e compara para ver se não existem repitidos
      novoContato.telefones.add(telefone);
    }

    listaContatos.add(novoContato);
    atualizarArquivo(); //função para atualizar o banco de dados
    System.out.println("Contato adicionado com sucesso!");
  }

  //REMOVE CONTATO
  public static void removerContato(Scanner sc) throws IOException {
    System.out.println("Digite o ID do contato que você deseja remover:");

    Contato contatoRemover = encontrarContato(
            garanteRange(sc, 0, (int) idCounter)
    );

    if (contatoRemover != null) {
      imprimeContato(contatoRemover.id);
      //confirmação da exclusão
      System.out.println("Tem certeza que deseja remover o contato?");
      System.out.println("1 - Sim");
      System.out.println("2 - Não");

      long aux = garanteRange(sc, 1, 2);

      if (aux == 1) {
        listaContatos.remove(contatoRemover);
        atualizarArquivo();
        System.out.println("Contato removido com sucesso!");
      } else if (aux == 2) {
        System.out.println("Remoção cancelada.");
      }
    } else {
      System.out.println("Contato não encontrado.");
    }
  }

  //EDITA CONTATO
  public static void editarContato(Scanner sc) throws IOException {
    System.out.println("Digite o ID do contato que você deseja editar:");

    Contato contatoEditar = encontrarContato(
            garanteRange(sc, 0, (int) idCounter)
    );

    if (contatoEditar != null) {
      imprimeContato(contatoEditar.id);
    } else {
      System.out.println("Contato não encontrado.");
      return;
    }

    System.out.println("Qual informação você deseja editar?");
    System.out.println("1 - Nome");
    System.out.println("2 - Sobrenome");
    System.out.println("3 - Telefones");
    System.out.println("4 - Nenhuma");

    int op = (int) garanteRange(sc, 1, 4);

    switch (op) {
      case 1:
        System.out.println("Digite o novo nome:");
        contatoEditar.nome = sc.nextLine();
        break;
      case 2:
        System.out.println("Digite o novo sobrenome:");
        contatoEditar.sobreNome = sc.nextLine();
        break;
      case 3:
        System.out.println(
                "O número possui " +
                contatoEditar.telefones.size() +
                (
                        contatoEditar.telefones.size() > 1
                                ? " telefones cadastrados:"
                                : " telefone cadastrado:"
                )
        );

        for (Telefone telefone : contatoEditar.telefones) {
          System.out.println(
                  "ID: " +
                  (contatoEditar.telefones.indexOf(telefone) + 1) +
                  "- Telefone - DDD: " +
                  telefone.ddd +
                  ", Número: " +
                  telefone.numero
          );
        }

        System.out.println("\nDigite o ID do telefone que deseja editar ou");
        System.out.println("Digite 0 (zero) para adcionar um novo número:");

        int aux = (int) garanteRange(sc, 0, contatoEditar.telefones.size());

        if (aux == 0) {
          contatoEditar.telefones.add(
                  obtemTelefone(sc, (contatoEditar.telefones.size() + 1))
          );
        } else {
          System.out.println(
                  "O que deseja fazer com o telefone de ID " + aux + "?"
          );
          System.out.println("0 - Apagar número");
          System.out.println("1 - Editar o numero existente :");

          int i = (int) garanteRange(sc, 0, 1);

          if (i == 0) {
            contatoEditar.telefones.remove(aux - 1);
          } else {
            contatoEditar.telefones.set(aux - 1, obtemTelefone(sc, aux));
          }
        }
        break;
      case 4:
        System.out.println("Edição cancelada. Nenhum contato foi editado.");
        return;
      default:
        System.out.println("Opção inválida.");
    }

    atualizarArquivo();
    System.out.println("Contato editado com sucesso!");
  }

  //*FUNÇOES AUXILIARES*/

  //CARREGA DADOS DO BANCO DE DADOS
  public static void carregarDados() throws IOException {
    File arquivo = new File(NOME_ARQUIVO); //procura o arquivo

    if (arquivo.exists()) { //se o arquivo existir ele carrega para a memoria os dados.
      BufferedReader leitor = new BufferedReader(new FileReader(arquivo));

      String linha;

      Contato contato = null;

      while ((linha = leitor.readLine()) != null) {
        if (linha.startsWith("ID: ")) {
          contato = new Contato();
          contato.id = Long.parseLong(linha.substring(4));
          idCounter = Math.max(idCounter, contato.id); // atualiza o id para o maior no banco de dados
          listaContatos.add(contato);
        } else if (linha.startsWith("Nome: ")) {
          contato.nome = linha.substring(6);
        } else if (linha.startsWith("Sobrenome: ")) {
          contato.sobreNome = linha.substring(11);
        } else if (linha.startsWith("Telefone - DDD: ")) {
          Telefone telefone = new Telefone();
          String[] partes = linha.substring(16).split(", Número: ");
          telefone.ddd = partes[0];
          telefone.numero = Long.parseLong(partes[1]);
          contato.telefones.add(telefone);
        }
      }
      leitor.close();
    }
  }

  //GARANTE QUE O USUARIO SO PODE DIGITAR UM NUMERO E QUE ESSE ESTEJA DENTRE DAS OPÇOES FORNECIDAS
  public static long garanteRange(Scanner sc, int rangeInf, int rangeMax) {
    String numStr = sc.nextLine();

    if (numStr.matches("\\d+")) {
      if (
              Long.parseLong(numStr) >= rangeInf && Long.parseLong(numStr) <= rangeMax
      ) {
        return Long.parseLong(numStr);
      } else {
        System.out.println("Opção invalida. Tente novamente!");
        return garanteRange(sc, rangeInf, rangeMax);
      }
    } else {
      System.out.println("Dígito inválido. Tente novamente!");
      return garanteRange(sc, rangeInf, rangeMax);
    }
  }

  //ENCONTRA CONTATO
  public static Contato encontrarContato(long id) {
    for (Contato contato : listaContatos) {
      if (contato.id == id) {
        return contato;
      }
    }
    return null; // Retorna null se o contato não for encontrado
  }

  //IMPRIME A LISTA DE CONTATOS NO MENU
  public static void imprimeContatosMenu() {
    System.out.println("\n>>>> Contatos <<<<");
    System.out.println("id | Nome");
    for (Contato aux : listaContatos) {
      System.out.println(aux.id + "  | " + aux.nome + " " + aux.sobreNome);
    }
    System.out.println();
  }

  //MOSTRA O CONTATO NA TELA
  public static void imprimeContato(long id) {
    Contato contatoImprimir = encontrarContato(id);

    if (contatoImprimir != null) {
      System.out.println("/----------------------------");
      System.out.println("Informações do contato:");
      System.out.println("ID: " + contatoImprimir.id);
      System.out.println("Nome: " + contatoImprimir.nome);
      System.out.println("Sobrenome: " + contatoImprimir.sobreNome);
      for (Telefone telefone : contatoImprimir.telefones) {
        System.out.println(
                "Telefone - DDD: " + telefone.ddd + ", Número: " + telefone.numero
        );
      }
      System.out.println("----------------------------------------/");
    } else {
      System.out.println("Contato não encontrado.");
    }
  }

  //OBTEM O NUMERO DE TELEFONE E VERIFICA SE ELE EXISTE NA LISTA
  private static Telefone obtemTelefone(Scanner sc, int numeroTelefone) {
    Telefone telefone = new Telefone();

    System.out.println(
            "Digite o DDD do telefone " + numeroTelefone + " (ex:024):"
    );

    String ddd = sc.nextLine();

    if (ddd.matches("\\d{3}")) {
      telefone.ddd = ddd;
      System.out.println(
              "Digite o número do telefone " + numeroTelefone + " (ex:912345678):"
      );
      String numero = sc.nextLine();
      if (numero.matches("\\d{9}")) {
        telefone.numero = Long.parseLong(numero);
        if (verificarNumeroExistente(telefone)) {
          System.out.println(
                  "Número já cadastrado em outro usuário. Tente outro número."
          );
          return obtemTelefone(sc, numeroTelefone);
        } else {
          return telefone;
        }
      } else {
        System.out.println("Número de telefone inválido. Tente novamente.");
        return obtemTelefone(sc, numeroTelefone);
      }
    } else {
      System.out.println("Número de telefone inválido. Tente novamente.");
      return obtemTelefone(sc, numeroTelefone);
    }
  }

  //VERIFICA SE O NUMERO DE TELEFONE JA EXISTE EM OUTRO CONTATO
  public static boolean verificarNumeroExistente(Telefone novoTelefone) {
    for (Contato contato : listaContatos) {
      for (Telefone telefone : contato.telefones) {
        if (
                telefone.ddd.equals(novoTelefone.ddd) &&
                telefone.numero.equals(novoTelefone.numero)
        ) {
          return true;
        }
      }
    }
    return false;
  }

  //ATUALIZA BANCO DE DADOS
  public static void atualizarArquivo() throws IOException {
    FileWriter escritor = new FileWriter(NOME_ARQUIVO, false);
    for (Contato contato : listaContatos) {
      escritor.write("ID: " + contato.id + SEPARADOR);
      escritor.write("Nome: " + contato.nome + SEPARADOR);
      escritor.write("Sobrenome: " + contato.sobreNome + SEPARADOR);

      for (Telefone telefone : contato.telefones) {
        escritor.write(
                "Telefone - DDD: " +
                telefone.ddd +
                ", Número: " +
                telefone.numero +
                SEPARADOR
        );
      }

      escritor.write(SEPARADOR);
    }
    escritor.close();
  }
}
