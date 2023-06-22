package Quad;

import java.util.ArrayList;
import java.util.List;
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Arthur,Gabriel e Sarah<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
class Quadtree {
    private static final int MAX_ELEMENTS = 4;  // N�mero m�ximo de elementos que um n� pode conter
    private int level;  // N�vel do n� na �rvore
    private List<Element> elements;  // Lista de elementos contidos no n�
    private BoundingBox bounds;  // Limites do n�
    private Quadtree[] nodes;  // Sub-n�s da �rvore

    public Quadtree(int level, BoundingBox bounds) {
        this.level = level;
        this.bounds = bounds;
        elements = new ArrayList<>();  // Inicializa a lista de elementos vazia
        nodes = new Quadtree[4];  // Inicializa os sub-n�s como um array de tamanho 4
    }

    public void insert(Element element) {
        if (nodes[0] != null) {  // Se o n� possui sub-n�s
            int index = getIndex(element);  // Obt�m o �ndice do sub-n� para inser��o do elemento
            if (index != -1) {
                nodes[index].insert(element);  // Insere o elemento no sub-n� correspondente
                return;
            }
        }

        elements.add(element);  // Adiciona o elemento � lista de elementos do n�

        if (elements.size() > MAX_ELEMENTS && level < 5) {
            // Se a lista de elementos exceder o limite m�ximo e o n�vel atual for menor que 5 (limite arbitr�rio)
            if (nodes[0] == null) {
                split();  // Divide o n� em sub-n�s
            }

            int i = 0;
            while (i < elements.size()) {
                Element e = elements.get(i);
                int index = getIndex(e);
                if (index != -1) {
                    nodes[index].insert(e);  // Insere elementos na lista no sub-n� correspondente
                    elements.remove(i);  // Remove o elemento da lista do n� atual
                } else {
                    i++;
                }
            }
        }
    }

    public void retrieve(List<Element> result, Element element) {
        int index = getIndex(element);
        if (index != -1 && nodes[0] != null) {
            nodes[index].retrieve(result, element);  // Recupera elementos do sub-n� correspondente
        }

        result.addAll(elements);  // Adiciona os elementos do n� atual ao resultado

        for (Element e : elements) {
            if (e != element && !result.contains(e)) {
                result.add(e);  // Adiciona elementos do n� atual ao resultado se ainda n�o estiverem presentes
            }
        }
    }

    public void clear() {
        elements.clear();  // Limpa a lista de elementos do n�

        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] != null) {
                nodes[i].clear();  // Limpa os sub-n�s recursivamente
                nodes[i] = null;  // Define o sub-n� como nulo
            }
        }
    }

    private void split() {
        int subWidth = bounds.getWidth() / 2;  // Largura do sub-n�
        int subHeight = bounds.getHeight() / 2;  // Altura do sub-n�
        int x = bounds.getX();  // Coordenada x do n� atual
        int y = bounds.getY();  // Coordenada y do n� atual

        // Cria os sub-n�s com base nos limites e coordenadas calculados
        nodes[0] = new Quadtree(level + 1, new BoundingBox(x + subWidth, y, subWidth, subHeight));
        nodes[1] = new Quadtree(level + 1, new BoundingBox(x, y, subWidth, subHeight));
        nodes[2] = new Quadtree(level + 1, new BoundingBox(x, y + subHeight, subWidth, subHeight));
        nodes[3] = new Quadtree(level + 1, new BoundingBox(x + subWidth, y + subHeight, subWidth, subHeight));
    }

    private int getIndex(Element element) {
        int index = -1;
        double verticalMidpoint = bounds.getX() + bounds.getWidth() / 2;  // Ponto m�dio vertical dos limites do n�
        double horizontalMidpoint = bounds.getY() + bounds.getHeight() / 2;  // Ponto m�dio horizontal dos limites do n�

        boolean topQuadrant = element.getY() + element.getHeight() < horizontalMidpoint;  // Elemento est� no quadrante superior?
        boolean bottomQuadrant = element.getY() > horizontalMidpoint;  // Elemento est� no quadrante inferior?

        if (element.getX() + element.getWidth() < verticalMidpoint) {
            if (topQuadrant) {
                index = 1;  // Quadrante superior esquerdo
            } else if (bottomQuadrant) {
                index = 2;  // Quadrante inferior esquerdo
            }
        } else if (element.getX() > verticalMidpoint) {
            if (topQuadrant) {
                index = 0;  // Quadrante superior direito
            } else if (bottomQuadrant) {
                index = 3;  // Quadrante inferior direito
            }
        }

        return index;  // Retorna o �ndice do sub-n� correspondente ao elemento
    }

    public BoundingBox getBounds() {
        return bounds;  // Retorna os limites do n�
    }

    public Quadtree[] getQuadtreeNodes() {
        return nodes;  // Retorna os sub-n�s da �rvore
    }
}
