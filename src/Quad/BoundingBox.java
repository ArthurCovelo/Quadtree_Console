package Quad;

// Classe BoundingBox representa uma caixa delimitadora retangular
class BoundingBox {
    private int x;      // Coordenada x do canto superior esquerdo
    private int y;      // Coordenada y do canto superior esquerdo
    private int width;  // Largura da caixa delimitadora
    private int height; // Altura da caixa delimitadora

    // Construtor da classe BoundingBox que recebe os par�metros x, y, largura e altura
    public BoundingBox(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // M�todo para obter a coordenada x do canto superior esquerdo da caixa delimitadora
    public int getX() {
        return x;
    }

    // M�todo para obter a coordenada y do canto superior esquerdo da caixa delimitadora
    public int getY() {
        return y;
    }

    // M�todo para obter a largura da caixa delimitadora
    public int getWidth() {
        return width;
    }

    // M�todo para obter a altura da caixa delimitadora
    public int getHeight() {
        return height;
    }  
}
