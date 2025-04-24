package util;

import java.io.*;
import java.util.*;

public class CsvUtil {
    public static List<String[]> lerArquivo(String caminho) throws IOException {
        List<String[]> linhas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (!linha.trim().isEmpty()) {
                    linhas.add(linha.split(";"));
                } else {
                    linhas.add(new String[]{""}); // separador de blocos
                }
            }
        }
        return linhas;
    }

    public static void escreverArquivo(String caminho, List<String> linhas) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminho))) {
            for (String linha : linhas) {
                bw.write(linha);
                bw.newLine();
            }
        }
    }
}
