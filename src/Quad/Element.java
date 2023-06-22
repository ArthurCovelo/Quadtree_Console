package Quad;

import java.awt.Color;

// Classe Element que representa um elemento gr�fico
class Element {
    // Vari�veis de inst�ncia privadas
    private int x; // posi��o x do elemento
    private int y; // posi��o y do elemento
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

    // M�todo getter para obter o valor de x
    public int getX() {
        return x;
    }

    // M�todo setter para definir o valor de x
    public void setX(int x) {
        this.x = x;
    }

    // M�todo getter para obter o valor de y
    public int getY() {
        return y;
    }

    // M�todo setter para definir o valor de y
    public void setY(int y) {
        this.y = y;
    }

    // M�todo getter para obter a largura
    public int getWidth() {
        return width;
    }

    // M�todo getter para obter a altura
    public int getHeight() {
        return height;
    }

    // M�todo getter para obter a velocidade no eixo x
    public int getVelocityX() {
        return velocityX;
    }

    // M�todo getter para obter a velocidade no eixo y
    public int getVelocityY() {
        return velocityY;
    }

    // M�todo setter para definir a velocidade no eixo x
    public void setVelocityX(int velocityX) {
        this.velocityX = velocityX;
    }

    // M�todo setter para definir a velocidade no eixo y
    public void setVelocityY(int velocityY) {
        this.velocityY = velocityY;
    }

    // M�todo getter para obter a cor do elemento
    public Color getColor() {
        return color;
    }

    // M�todo setter para definir a cor do elemento
    public void setColor(Color color) {
        this.color = color;
    }
}
