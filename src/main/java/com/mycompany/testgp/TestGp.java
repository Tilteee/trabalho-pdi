/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.testgp;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.*;

public class TestGp {
    public static BufferedImage filtrar(BufferedImage imagem, int tamanhoJanela) {
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
                    if(line == null) break;
                }
            }
        } else if (magicNumber.equals("P3")) {
            // Formato PPM ASCII
            for (int y = 0; y < altura; y++) {
                for (int x = 0; x < largura; x++) {
                    int valorR = Integer.parseInt(line);
                    line = reader.readLine();
                    if(line == null) break;
                    int valorG = Integer.parseInt(line);
                    line = reader.readLine();
                    if(line == null) break;
                    int valorB = Integer.parseInt(line);
                    line = reader.readLine();
                    if(line == null) break;
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
        writer.write(ascii ? "P3" : "P5");
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
                int corCombinada = (cor.getRed() << 16) | (cor.getGreen() << 8) | cor.getBlue();
                writer.write(corCombinada + " ");
            }
            writer.newLine();
        }
        }

        writer.close();
    }

    public static void main(String[] args) {
        try {
//            BufferedImage imagem = lerImagemPGM("C:\\Users\\carlo\\Documents\\NetBeansProjects\\TestGp\\src\\main\\java\\com\\mycompany\\testgp\\cameraman.pgm");
//            BufferedImage imagemFiltrada = filtrar(imagem, 9);
//            escreverImagemPGM(imagemFiltrada, "C:\\Users\\carlo\\Documents\\NetBeansProjects\\TestGp\\src\\main\\java\\com\\mycompany\\testgp\\cameramanTeste.pgm", true);
//
            BufferedImage imagemPPM = lerImagemPGM("C:\\Users\\carlo\\Documents\\NetBeansProjects\\TestGp\\src\\main\\java\\com\\mycompany\\testgp\\Autumn.ppm");
            BufferedImage imagemFiltradaPPM = filtrar(imagemPPM, 3);
            escreverImagemPGM(imagemFiltradaPPM, "C:\\Users\\carlo\\Documents\\NetBeansProjects\\TestGp\\src\\main\\java\\com\\mycompany\\testgp\\AutumnTeste.ppm", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

