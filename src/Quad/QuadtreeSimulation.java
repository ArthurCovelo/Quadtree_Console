package Quad;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class QuadtreeSimulation extends JPanel implements KeyListener {
    private static final int FRAME_WIDTH = 800; // Largura da janela
    private static final int FRAME_HEIGHT = 600; // Altura da janela
    private static final int PLAYER_SIZE = 20; // Tamanho do jogador
    private static final int NUM_STATIC_ELEMENTS = 50; // N�mero de elementos est�ticos
    private static final int NUM_DYNAMIC_ELEMENTS = 50; // N�mero de elementos din�micos
    private static final int ELEMENT_SIZE_RANGE = 20; // Faixa de tamanho dos elementos
    private static final int MAX_VELOCITY = 5; // Velocidade m�xima
    private static final int UPDATE_DELAY = 20; // Atraso de atualiza��o (em milissegundos)
    private static final Color PLAYER_COLOR = Color.BLUE; // Cor do jogador
    private static final Color STATIC_ELEMENT_COLOR = Color.GREEN; // Cor dos elementos est�ticos
    private static final Color DYNAMIC_ELEMENT_COLOR = Color.YELLOW; // Cor dos elementos din�micos
    private static final Color COLLISION_COLOR = Color.RED; // Cor das colis�es

    private Element player; // Jogador
    private List<Element> staticElements; // Lista de elementos est�ticos
    private List<Element> dynamicElements; // Lista de elementos din�micos
    private Quadtree quadtree; // Quadtree para otimiza��o

    public QuadtreeSimulation() {
        setFocusable(true);
        addKeyListener(this);

        player = new Element(FRAME_WIDTH / 2, FRAME_HEIGHT / 2, PLAYER_SIZE, PLAYER_SIZE, 0, 0, PLAYER_COLOR); // Cria��o do jogador
        staticElements = createStaticElements(); // Cria��o dos elementos est�ticos
        dynamicElements = createDynamicElements(); // Cria��o dos elementos din�micos
        quadtree = new Quadtree(0, new BoundingBox(0, 0, FRAME_WIDTH, FRAME_HEIGHT)); // Cria��o da quadtree com os limites da janela

        // Inser��o dos elementos est�ticos na quadtree
        for (Element element : staticElements) {
            quadtree.insert(element);
        }
        // Inser��o dos elementos din�micos na quadtree
        for (Element element : dynamicElements) {
            quadtree.insert(element);
        }

        // Thread respons�vel pela atualiza��o cont�nua do jogo
        Thread updateThread = new Thread(() -> {
            while (true) {
                update(); // Atualiza o estado do jogo
                repaint(); // Redesenha a tela

                try {
                    Thread.sleep(UPDATE_DELAY); // Aguarda o tempo definido pelo atraso de atualiza��o
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        updateThread.start(); // Inicia a thread de atualiza��o
    }

    // Cria��o dos elementos est�ticos
    private List<Element> createStaticElements() {
        List<Element> elements = new ArrayList<>();

        for (int i = 0; i < NUM_STATIC_ELEMENTS; i++) {
            int x = (int) (Math.random() * FRAME_WIDTH);
            int y = (int) (Math.random() * FRAME_HEIGHT);
            int size = (int) (Math.random() * ELEMENT_SIZE_RANGE) + 1;
            elements.add(new Element(x, y, size, size, 0, 0, STATIC_ELEMENT_COLOR));
        }

        return elements;
    }

    // Cria��o dos elementos din�micos
    private List<Element> createDynamicElements() {
        List<Element> elements = new ArrayList<>();

        for (int i = 0; i < NUM_DYNAMIC_ELEMENTS; i++) {
            int x = (int) (Math.random() * FRAME_WIDTH);
            int y = (int) (Math.random() * FRAME_HEIGHT);
            int size = (int) (Math.random() * ELEMENT_SIZE_RANGE) + 1;
            int velocityX = (int) (Math.random() * MAX_VELOCITY * 2) - MAX_VELOCITY;
            int velocityY = (int) (Math.random() * MAX_VELOCITY * 2) - MAX_VELOCITY;
            elements.add(new Element(x, y, size, size, velocityX, velocityY, DYNAMIC_ELEMENT_COLOR));
        }

        return elements;
    }

    // Atualiza o estado do jogo
    private void update() {
        quadtree.clear(); // Limpa a quadtree

        // Reinsere os elementos est�ticos na quadtree
        for (Element element : staticElements) {
            quadtree.insert(element);
        }

        // Move os elementos din�micos e reinsere na quadtree
        for (Element element : dynamicElements) {
            moveElement(element);
            quadtree.insert(element);
        }

        // Move o jogador e reinsere na quadtree
        moveElement(player);
        quadtree.insert(player);

        checkCollisions(); // Verifica colis�es
    }

    // Move um elemento adicionando sua velocidade � posi��o atual
    private void moveElement(Element element) {
        element.setX(element.getX() + element.getVelocityX());
        element.setY(element.getY() + element.getVelocityY());

        // Verifica colis�es com as bordas da janela e inverte a velocidade caso necess�rio
        if (element.getX() <= 0 || element.getX() >= FRAME_WIDTH - element.getWidth()) {
            element.setVelocityX(-element.getVelocityX());
        }
        if (element.getY() <= 0 || element.getY() >= FRAME_HEIGHT - element.getHeight()) {
            element.setVelocityY(-element.getVelocityY());
        }
    }

    // Verifica colis�es entre elementos
    private void checkCollisions() {
        List<Element> nearElements = new ArrayList<>();
        quadtree.retrieve(nearElements, player);

        // Verifica colis�es com elementos pr�ximos ao jogador
        for (Element element : nearElements) {
            if (element != player && isColliding(player, element)) {
                player.setColor(COLLISION_COLOR);
                element.setColor(COLLISION_COLOR);
            } else {
                player.setColor(PLAYER_COLOR);
            }
        }

        // Verifica colis�es com elementos est�ticos
        for (Element element : staticElements) {
            if (isColliding(player, element)) {
                player.setColor(COLLISION_COLOR);
                element.setColor(COLLISION_COLOR);
            } else {
                element.setColor(STATIC_ELEMENT_COLOR);
            }
        }

        // Verifica colis�es com elementos din�micos
        for (Element element : dynamicElements) {
            if (isColliding(player, element)) {
                player.setColor(COLLISION_COLOR);
                element.setColor(COLLISION_COLOR);
            } else {
                element.setColor(DYNAMIC_ELEMENT_COLOR);
            }
        }

        // Verifica colis�es entre elementos din�micos
        for (Element dynamicElement : dynamicElements) {
            moveElement(dynamicElement);
            quadtree.insert(dynamicElement);
            quadtree.retrieve(nearElements, dynamicElement);

            for (Element element : nearElements) {
                if (element != dynamicElement && isColliding(dynamicElement, element)) {
                    // C�lculo da resposta � colis�o com base no ponto de contato
                    double collisionX = Math.max(dynamicElement.getX(), element.getX());
                    double collisionY = Math.max(dynamicElement.getY(), element.getY());

                    double collisionWidth = Math.min(dynamicElement.getX() + dynamicElement.getWidth(), element.getX() + element.getWidth()) - collisionX;
                    double collisionHeight = Math.min(dynamicElement.getY() + dynamicElement.getHeight(), element.getY() + element.getHeight()) - collisionY;

                    // Ajuste das velocidades com base no ponto de contato
                    double dynamicElementCenterX = dynamicElement.getX() + dynamicElement.getWidth() / 2;
                    double dynamicElementCenterY = dynamicElement.getY() + dynamicElement.getHeight() / 2;
                    double elementCenterX = element.getX() + element.getWidth() / 2;
                    double elementCenterY = element.getY() + element.getHeight() / 2;

                    double dx = dynamicElementCenterX - elementCenterX;
                    double dy = dynamicElementCenterY - elementCenterY;

                    if (Math.abs(dx) > Math.abs(dy)) {
                        // Colis�o pelas laterais
                        if (dx < 0) {
                            dynamicElement.setVelocityX(-Math.abs(dynamicElement.getVelocityX()));
                            element.setVelocityX(Math.abs(element.getVelocityX()));
                        } else {
                            dynamicElement.setVelocityX(Math.abs(dynamicElement.getVelocityX()));
                            element.setVelocityX(-Math.abs(element.getVelocityX()));
                        }
                    } else {
                        // Colis�o por cima/por baixo
                        if (dy < 0) {
                            dynamicElement.setVelocityY(-Math.abs(dynamicElement.getVelocityY()));
                            element.setVelocityY(Math.abs(element.getVelocityY()));
                        } else {
                            dynamicElement.setVelocityY(Math.abs(dynamicElement.getVelocityY()));
                            element.setVelocityY(-Math.abs(element.getVelocityY()));
                        }
                    }
                }
            }
        }

        // Verifica se o jogador est� colidindo e redefinir a cor
        if (!isPlayerColliding()) {
            player.setColor(PLAYER_COLOR);
        }
    }

    // Verifica se o jogador est� colidindo com algum elemento
    private boolean isPlayerColliding() {
        for (Element element : staticElements) {
            if (isColliding(player, element)) {
                return true;
            }
        }

        for (Element element : dynamicElements) {
            if (isColliding(player, element)) {
                return true;
            }
        }

        return false;
    }

    // Verifica se dois elementos est�o colidindo
    private boolean isColliding(Element e1, Element e2) {
        return e1.getX() < e2.getX() + e2.getWidth() &&
                e1.getX() + e1.getWidth() > e2.getX() &&
                e1.getY() < e2.getY() + e2.getHeight() &&
                e1.getY() + e1.getHeight() > e2.getY();
    }

    // Desenha os elementos na tela
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawQuadtree(g, quadtree);

        g.setColor(player.getColor());
        g.fillRect(player.getX(), player.getY(), player.getWidth(), player.getHeight());

        for (Element element : staticElements) {
            g.setColor(element.getColor());
            g.fillRect(element.getX(), element.getY(), element.getWidth(), element.getHeight());
        }

        for (Element element : dynamicElements) {
            g.setColor(element.getColor());
            g.fillRect(element.getX(), element.getY(), element.getWidth(), element.getHeight());
        }
    }

    // Desenha a quadtree na tela
    private void drawQuadtree(Graphics g, Quadtree quadtree) {
        BoundingBox bounds = quadtree.getBounds();
        g.setColor(Color.GRAY);
        g.drawRect(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());

        Quadtree[] nodes = quadtree.getQuadtreeNodes();
        if (nodes[0] != null) {
            for (Quadtree node : nodes) {
                drawQuadtree(g, node);
            }
        }
    }

    // M�todos do KeyListener para controlar o jogador com as setas do teclado
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            player.setVelocityX(-MAX_VELOCITY);
        } else if (key == KeyEvent.VK_RIGHT) {
            player.setVelocityX(MAX_VELOCITY);
        } else if (key == KeyEvent.VK_UP) {
            player.setVelocityY(-MAX_VELOCITY);
        } else if (key == KeyEvent.VK_DOWN) {
            player.setVelocityY(MAX_VELOCITY);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
            player.setVelocityX(0);
        } else if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN) {
            player.setVelocityY(0);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public static void main(String[] args) {
    	 long startTime = System.currentTimeMillis();

         JFrame frame = new JFrame("Quadtree Simulation");
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
         frame.getContentPane().add(new QuadtreeSimulation());
         frame.setVisible(true);

         long endTime = System.currentTimeMillis();
         long executionTime = endTime - startTime;
         System.out.println("Tempo total de execu��o: " + executionTime + "ms");

       
    }
}
