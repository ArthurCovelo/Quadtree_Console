package Quad;

import java.awt.Color;

// Classe Element que representa um elemento gráfico
class Element {
    // Variáveis de instância privadas
    private int x; // posição x do elemento
    private int y; // posição y do elemento
    private int width; // largura do elemento
    private int height; // altura do elemento
    private int velocityX; // velocidade no eixo x
    private int velocityY; // velocidade no eixo y
    private Color color; // cor do elemento

    // Construtor da classe Element
    public Element(int x, int y, int width, int height, int velocityX, int velocityY, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.color = color;
    }

    // Método getter para obter o valor de x
    public int getX() {
        return x;
    }

    // Método setter para definir o valor de x
    public void setX(int x) {
        this.x = x;
    }

    // Método getter para obter o valor de y
    public int getY() {
        return y;
    }

    // Método setter para definir o valor de y
    public void setY(int y) {
        this.y = y;
    }

    // Método getter para obter a largura
    public int getWidth() {
        return width;
    }

    // Método getter para obter a altura
    public int getHeight() {
        return height;
    }

    // Método getter para obter a velocidade no eixo x
    public int getVelocityX() {
        return velocityX;
    }

    // Método getter para obter a velocidade no eixo y
    public int getVelocityY() {
        return velocityY;
    }

    // Método setter para definir a velocidade no eixo x
    public void setVelocityX(int velocityX) {
        this.velocityX = velocityX;
    }

    // Método setter para definir a velocidade no eixo y
    public void setVelocityY(int velocityY) {
        this.velocityY = velocityY;
    }

    // Método getter para obter a cor do elemento
    public Color getColor() {
        return color;
    }

    // Método setter para definir a cor do elemento
    public void setColor(Color color) {
        this.color = color;
    }
}
