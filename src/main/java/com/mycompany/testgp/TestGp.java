/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.testgp;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.*;

public class TestGp {
    public static BufferedImage aplicarFiltroMedia(BufferedImage imagem, int tamanhoJanela) {
        int largura = imagem.getWidth();
        int altura = imagem.getHeight();
        BufferedImage novaImagem = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_RGB);

        // Loop pelos pixels da imagem
        for (int y = 0; y < altura; y++) {
            for (int x = 0; x < largura; x++) {
                // Calcula a média dos valores RGB dos pixels vizinhos
                int mediaR = 0;
                int mediaG = 0;
                int mediaB = 0;
                int totalPixels = 0;

                // Define os limites da janela
                int inicioX = x - tamanhoJanela / 2;
                int fimX = x + tamanhoJanela / 2;
                int inicioY = y - tamanhoJanela / 2;
                int fimY = y + tamanhoJanela / 2;

                // Loop pelos pixels vizinhos dentro da janela
                for (int j = inicioY; j <= fimY; j++) {
                    for (int i = inicioX; i <= fimX; i++) {
                        // Verifica se o pixel está dentro da imagem
                        if (i >= 0 && i < largura && j >= 0 && j < altura) {
                            // Obtém o valor RGB do pixel vizinho
                            Color cor = new Color(imagem.getRGB(i, j));
                            mediaR += cor.getRed();
                            mediaG += cor.getGreen();
                            mediaB += cor.getBlue();
                            totalPixels++;
                        }
                    }
                }

                // Calcula a média final dos valores RGB
                int mediaFinalR = mediaR / totalPixels;
                int mediaFinalG = mediaG / totalPixels;
                int mediaFinalB = mediaB / totalPixels;

                // Define o novo valor RGB para o pixel na nova imagem
                Color novaCor = new Color(mediaFinalR, mediaFinalG, mediaFinalB);
                novaImagem.setRGB(x, y, novaCor.getRGB());
            }
        }

        return novaImagem;
    }

    public static BufferedImage aplicarFiltroMediana(BufferedImage imagem, int tamanhoJanela) {
        int largura = imagem.getWidth();
        int altura = imagem.getHeight();
        BufferedImage novaImagem = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_RGB);

        // Loop pelos pixels da imagem
        for (int y = 0; y < altura; y++) {
            for (int x = 0; x < largura; x++) {
                // Obtém os valores RGB dos pixels vizinhos dentro da janela
                int[] valoresR = new int[tamanhoJanela * tamanhoJanela];
                int[] valoresG = new int[tamanhoJanela * tamanhoJanela];
                int[] valoresB = new int[tamanhoJanela * tamanhoJanela];
                int index = 0;

                // Define os limites da janela
                int inicioX = x - tamanhoJanela / 2;
                int fimX = x + tamanhoJanela / 2;
                int inicioY = y - tamanhoJanela / 2;
                int fimY = y + tamanhoJanela / 2;

                // Loop pelos pixels vizinhos dentro da janela
                for (int j = inicioY; j <= fimY; j++) {
                    for (int i = inicioX; i <= fimX; i++) {
                        // Verifica se o pixel está dentro da imagem
                        if (i >= 0 && i < largura && j >= 0 && j < altura) {
                            // Obtém o valor RGB do pixel vizinho
                            Color cor = new Color(imagem.getRGB(i, j));
                            valoresR[index] = cor.getRed();
                            valoresG[index] = cor.getGreen();
                            valoresB[index] = cor.getBlue();
                            index++;
                        }
                    }
                }

                // Ordena os valores RGB em ordem crescente
                ordenar(valoresR);
                ordenar(valoresG);
                ordenar(valoresB);

                // Obtém a mediana dos valores RGB
                int medianaR = obterMediana(valoresR);
                int medianaG = obterMediana(valoresG);
                int medianaB = obterMediana(valoresB);

                // Define o novo valor RGB para o pixel na nova imagem
                Color novaCor = new Color(medianaR, medianaG, medianaB);
                novaImagem.setRGB(x, y, novaCor.getRGB());
            }
        }

        return novaImagem;
    }

    public static BufferedImage aplicarFiltroLaplaciano(BufferedImage imagem) {
        int largura = imagem.getWidth();
        int altura = imagem.getHeight();
        BufferedImage novaImagem = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_RGB);

        // Máscara do filtro Laplaciano
        int[][] mascara = {{0, 1, 0}, {1, -4, 1}, {0, 1, 0}};

        // Loop pelos pixels da imagem
        for (int y = 1; y < altura - 1; y++) {
            for (int x = 1; x < largura - 1; x++) {
                int somaR = 0;
                int somaG = 0;
                int somaB = 0;

                // Aplica a máscara do filtro Laplaciano
                for (int j = -1; j <= 1; j++) {
                    for (int i = -1; i <= 1; i++) {
                        Color cor = new Color(imagem.getRGB(x + i, y + j));
                        int valorR = cor.getRed();
                        int valorG = cor.getGreen();
                        int valorB = cor.getBlue();
                        int peso = mascara[j + 1][i + 1];

                        somaR += valorR * peso;
                        somaG += valorG * peso;
                        somaB += valorB * peso;
                    }
                }

                // Verifica os limites dos valores RGB
                int novoValorR = Math.min(Math.max(somaR, 0), 255);
                int novoValorG = Math.min(Math.max(somaG, 0), 255);
                int novoValorB = Math.min(Math.max(somaB, 0), 255);

                // Define o novo valor RGB para o pixel na nova imagem
                Color novaCor = new Color(novoValorR, novoValorG, novoValorB);
                novaImagem.setRGB(x, y, novaCor.getRGB());
            }
        }

        return novaImagem;
    }
    
    public static BufferedImage aplicarFiltroAltoReforco(BufferedImage imagem, int a) {
        int largura = imagem.getWidth();
        int altura = imagem.getHeight();
        BufferedImage novaImagem = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_RGB);

        // Filtro passa-alta (Laplaciano)
        int[][] mascaraLaplaciano = {{0, -1, 0}, {-1, 5, -1}, {0, -1, 0}};

        // Loop pelos pixels da imagem
        for (int y = 1; y < altura - 1; y++) {
            for (int x = 1; x < largura - 1; x++) {
                int somaR = 0;
                int somaG = 0;
                int somaB = 0;

                // Aplica a máscara do filtro Laplaciano
                for (int j = -1; j <= 1; j++) {
                    for (int i = -1; i <= 1; i++) {
                        Color cor = new Color(imagem.getRGB(x + i, y + j));
                        int valorR = cor.getRed();
                        int valorG = cor.getGreen();
                        int valorB = cor.getBlue();
                        int peso = mascaraLaplaciano[j + 1][i + 1];

                        somaR += valorR * peso;
                        somaG += valorG * peso;
                        somaB += valorB * peso;
                    }
                }

                // Verifica os limites dos valores RGB
                int novoValorR = Math.min(Math.max((int) (imagem.getRGB(x, y) >> 16 & 0xFF) + a * somaR, 0), 255);
                int novoValorG = Math.min(Math.max((int) (imagem.getRGB(x, y) >> 8 & 0xFF) + a * somaG, 0), 255);
                int novoValorB = Math.min(Math.max((int) (imagem.getRGB(x, y) & 0xFF) + a * somaB, 0), 255);

                // Define o novo valor RGB para o pixel na nova imagem
                Color novaCor = new Color(novoValorR, novoValorG, novoValorB);
                novaImagem.setRGB(x, y, novaCor.getRGB());
            }
        }

        return novaImagem;
    }



    
    private static void ordenar(int[] valores) {
        int n = valores.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (valores[j] > valores[j + 1]) {
                    // Troca os valores
                    int temp = valores[j];
                    valores[j] = valores[j + 1];
                    valores[j + 1] = temp;
                }
            }
        }
    }

    private static int obterMediana(int[] valores) {
        int n = valores.length;
        if (n % 2 == 0) {
            // Número par de valores, calcula a média dos dois valores centrais
            int index1 = n / 2 - 1;
            int index2 = n / 2;
            return (valores[index1] + valores[index2]) / 2;
        } else {
            // Número ímpar de valores, retorna o valor central
            int index = n / 2;
            return valores[index];
        }
    }

    public static BufferedImage lerImagemPGM(String caminho) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(caminho));
        String magicNumber = reader.readLine(); // Lê o número mágico (P2 ou P3)
        String comment = reader.readLine(); // Lê o comentário
        String[] size = reader.readLine().split(" "); // Lê a largura e altura da imagem
        int largura = Integer.parseInt(size[0]);
        int altura = Integer.parseInt(size[1]);
        int maxVal = Integer.parseInt(reader.readLine()); // Lê o valor máximo

        BufferedImage imagem = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_RGB);
        String line = reader.readLine();
        if (magicNumber.equals("P2")) {
            // Formato PGM ASCII
            for (int y = 0; y < altura; y++) {
                for (int x = 0; x < largura; x++) {
                    int valor = Integer.parseInt(line);
                    Color cor = new Color(valor, valor, valor);
                    imagem.setRGB(x, y, cor.getRGB());
                    line = reader.readLine();
                    if (line == null) break;
                }
            }
        } else if (magicNumber.equals("P3")) {
            // Formato PPM ASCII
            for (int y = 0; y < altura; y++) {
                for (int x = 0; x < largura; x++) {
                    int valorR = Integer.parseInt(line);
                    line = reader.readLine();
                    if (line == null) break;
                    int valorG = Integer.parseInt(line);
                    line = reader.readLine();
                    if (line == null) break;
                    int valorB = Integer.parseInt(line);
                    line = reader.readLine();
                    if (line == null) break;
                    Color cor = new Color(valorR, valorG, valorB);
                    imagem.setRGB(x, y, cor.getRGB());
                }
            }
        }

        reader.close();
        return imagem;
    }

    public static void escreverImagemPGM(BufferedImage imagem, String caminho, boolean ascii) throws IOException {
        int largura = imagem.getWidth();
        int altura = imagem.getHeight();

        BufferedWriter writer = new BufferedWriter(new FileWriter(caminho));
        writer.write(ascii ? "P2" : "P3");
        writer.newLine();
        writer.write("# Imagem filtrada");
        writer.newLine();
        writer.write(largura + " " + altura);
        writer.newLine();
        writer.write("255");
        writer.newLine();

        if (ascii) {
            // Formato PGM ASCII
            for (int y = 0; y < altura; y++) {
                StringBuilder linha = new StringBuilder();
                for (int x = 0; x < largura; x++) {
                    Color cor = new Color(imagem.getRGB(x, y));
                    int valor = (cor.getRed() + cor.getGreen() + cor.getBlue()) / 3;
                    linha.append(valor).append(" ");
                }
                writer.write(linha.toString().trim());
                writer.newLine();
            }
        } else {
            // Formato PGM RAW
            for (int y = 0; y < altura; y++) {
                for (int x = 0; x < largura; x++) {
                    Color cor = new Color(imagem.getRGB(x, y));
                    writer.write(Integer.toString(cor.getRed()));
                    writer.newLine();
                    writer.write(Integer.toString(cor.getGreen()));
                    writer.newLine();
                    writer.write(Integer.toString(cor.getBlue()));
                    writer.newLine();
                }
            }
        }

        writer.close();
    }

    public static void main(String[] args) {
        try {
            BufferedImage imagem = lerImagemPGM("C:\\Users\\carlo\\Documents\\NetBeansProjects\\TestGp\\src\\main\\java\\com\\mycompany\\testgp\\cameraman.pgm");
            BufferedImage imagemFiltradaMedia = aplicarFiltroMedia(imagem, 3);
            escreverImagemPGM(imagemFiltradaMedia,"C:\\Users\\carlo\\Documents\\NetBeansProjects\\TestGp\\src\\main\\java\\com\\mycompany\\testgp\\cameramanTeste.pgm", true);

            BufferedImage imagemFiltradaMediana = aplicarFiltroMediana(imagem, 3);
            escreverImagemPGM(imagemFiltradaMediana, "C:\\Users\\carlo\\Documents\\NetBeansProjects\\TestGp\\src\\main\\java\\com\\mycompany\\testgp\\AutumnTeste.ppm", true);

            BufferedImage imagemFiltradaLaplaciano = aplicarFiltroLaplaciano(imagem);
            escreverImagemPGM(imagemFiltradaLaplaciano, "C:\\Users\\carlo\\Documents\\NetBeansProjects\\TestGp\\src\\main\\java\\com\\mycompany\\testgp\\laplaciano.ppm", true);

            BufferedImage imagemFiltradaAltoReforco = aplicarFiltroAltoReforco(imagem, 2);
            escreverImagemPGM(imagemFiltradaAltoReforco, "C:\\Users\\carlo\\Documents\\NetBeansProjects\\TestGp\\src\\main\\java\\com\\mycompany\\testgp\\/imagemFiltradaAltoReforco.pgm", true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


