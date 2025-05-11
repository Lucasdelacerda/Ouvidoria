

package br.com.dio;

import javax.swing.JOptionPane;
import java.awt.*;
import java.sql.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Ouvidoria {



    public static void main(String[] args) {
        int opcao;
        Connection conn;
        Properties props = new Properties();
        try (InputStream input = new FileInputStream("config.properties")) {
            props.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao carregar arquivo de configuração.");
            return;
        }

        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");

        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados.");
            return;
        }

        do {
            String opcaoStr = JOptionPane.showInputDialog("ouvidoria\nOpções:\n1)Exibir Todas Ocorrências\n2)Procurar ocorrência\n3)Adicionar nova ocorrência\n4)Remover ocorrência\n5)Sair\nDigite a sua opção:");
            opcao = Integer.parseInt(opcaoStr);

            try {
                if (opcao == 1) {
                    listarManifestacoes(conn);
                } else if (opcao == 2) {
                    procurarOcorrencia(conn);
                } else if (opcao == 3) {
                    inserirManifestacao(conn);
                } else if (opcao == 4) {

                    excluirManifestacao(conn);
                } else if (opcao != 5) {
                    JOptionPane.showMessageDialog(null, "Opção Inválida");
                }
            } catch (SQLException | HeadlessException e) {
                throw new RuntimeException(e);
            }
        } while (opcao != 5);

    }

    private static void excluirManifestacao(Connection conn) throws SQLException {
        String id = JOptionPane.showInputDialog("Informe o ID da ocorrência que deseja excluir");

        // SQL para excluir a ocorrência
        String sql = "DELETE FROM reclamacoes WHERE id = ?";

        // Preparando o PreparedStatement
        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setInt(1, Integer.parseInt(id)); // Define o parâmetro do id para exclusão

        // Executando a exclusão
        int linhasAfetadas = pstm.executeUpdate();

        // Verificando se a exclusão foi bem-sucedida
        if (linhasAfetadas > 0) {
            JOptionPane.showMessageDialog(null, "Ocorrência com ID " + id + " foi excluída com sucesso!");
        } else {
            JOptionPane.showMessageDialog(null, "Nenhuma ocorrência encontrada com o ID " + id);
        }

        // Fechando o PreparedStatement
        pstm.close();
    }


    private static void procurarOcorrencia(Connection conn) throws SQLException {

        String pesquisa = JOptionPane.showInputDialog("Informe o nome, CPF, tipo de reclamação, ID ou palavra chave");


        String sql = """
                SELECT * FROM reclamacoes\s
                WHERE id LIKE ?\s
                   OR nome LIKE ?\s
                   OR cpf LIKE ?\s
                   OR tipo LIKE ?\s
                   OR email LIKE ?;""";


        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setString(1, "%" + pesquisa + "%");
        pstm.setString(2, "%" + pesquisa + "%");
        pstm.setString(3, "%" + pesquisa + "%");
        pstm.setString(4, "%" + pesquisa + "%");
        pstm.setString(5, "%" + pesquisa + "%");

        ResultSet rs = pstm.executeQuery();


        StringBuilder manifestacoesSelecionadas = new StringBuilder();


        while (rs.next()) {
            int id = rs.getInt(1);
            String nome = rs.getString(2);
            String cpf = rs.getString(3);
            String tipo = rs.getString(4);
            String email = rs.getString(5);
            String descricao = rs.getString(6);

            manifestacoesSelecionadas.append("ID: " + id + " Nome: " + nome + " CPF: " + cpf + " Tipo de Reclamação: " + tipo + " Email: " + email + " Descrição: " + descricao + "\n");
        }


        JOptionPane.showMessageDialog(null, manifestacoesSelecionadas);


        rs.close();
        pstm.close();
    }


    private static void inserirManifestacao(Connection conn) throws SQLException {
        String tipo = "";
        String sql = "insert into reclamacoes (nome, cpf, tipo,email,descricao) value (?,?,?,?,?);";
        PreparedStatement pstm = conn.prepareStatement(sql);

        String nomeCliente = JOptionPane.showInputDialog("Informe o nome do cliente");
        String cpfCliente = JOptionPane.showInputDialog("Informe o CPF do cliente");
        String tipoReclamacao = JOptionPane.showInputDialog("Informe o nome do cliente:\n1)Reclamação\n2)Elogio\n3)Sugestão");
        //consertar o erro daqui
        switch (tipoReclamacao) {
            case "1" -> tipo = "Reclamação";
            case "2" -> tipo = "Elogio";
            case "3" -> tipo = "Sugestão";
            default -> JOptionPane.showInputDialog(null, "Opção inválida, selecione uma das opções: ");
        }
        String emailCliente = JOptionPane.showInputDialog("Informe o email do cliente: ");
        String descricaoManifestacao = JOptionPane.showInputDialog("Informe a descrição: ");

        pstm.setString(1, nomeCliente);
        pstm.setString(2, cpfCliente);
        pstm.setString(3, tipo);
        pstm.setString(4, emailCliente);
        pstm.setString(5, descricaoManifestacao);

        int linhasAlteradas = pstm.executeUpdate();
        if (linhasAlteradas > 0) {
            JOptionPane.showMessageDialog(null, "Manifestação cadastrada com sucesso");
        } else {
            JOptionPane.showMessageDialog(null, "Manifestação não cadastrada, fale com o gestor!");
        }
        pstm.close();
    }

    private static void listarManifestacoes(Connection conn) throws SQLException {
        String sql = "select id,nome,tipo, cpf, email, descricao from reclamacoes;";

        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery(sql);
        StringBuilder todasManifestacoes = new StringBuilder();

        while (rs.next()) {
            int id = rs.getInt(1);
            String nome = rs.getString(2);
            String cpf = rs.getString(3);
            String tipo = rs.getString(4);
            String email = rs.getString(5);
            String descricao = rs.getString(6);

            todasManifestacoes.append("ID: " + id + " Nome: " + nome + " CPF: " + cpf + " Tipo de Reclamação: " + tipo + " Email: " + email + " Descrição: " + descricao + "\n");
        }
        JOptionPane.showMessageDialog(null, todasManifestacoes);
        rs.close();
        stm.close();
    }
}
