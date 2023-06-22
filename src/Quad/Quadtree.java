package Quad;

import java.util.ArrayList;
import java.util.List;
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Arthur,Gabriel e Sarah<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
class Quadtree {
    private static final int MAX_ELEMENTS = 4;  // Número máximo de elementos que um nó pode conter
    private int level;  // Nível do nó na árvore
    private List<Element> elements;  // Lista de elementos contidos no nó
    private BoundingBox bounds;  // Limites do nó
    private Quadtree[] nodes;  // Sub-nós da árvore

    public Quadtree(int level, BoundingBox bounds) {
        this.level = level;
        this.bounds = bounds;
        elements = new ArrayList<>();  // Inicializa a lista de elementos vazia
        nodes = new Quadtree[4];  // Inicializa os sub-nós como um array de tamanho 4
    }

    public void insert(Element element) {
        if (nodes[0] != null) {  // Se o nó possui sub-nós
            int index = getIndex(element);  // Obtém o índice do sub-nó para inserção do elemento
            if (index != -1) {
                nodes[index].insert(element);  // Insere o elemento no sub-nó correspondente
                return;
            }
        }

        elements.add(element);  // Adiciona o elemento à lista de elementos do nó

        if (elements.size() > MAX_ELEMENTS && level < 5) {
            // Se a lista de elementos exceder o limite máximo e o nível atual for menor que 5 (limite arbitrário)
            if (nodes[0] == null) {
                split();  // Divide o nó em sub-nós
            }

            int i = 0;
            while (i < elements.size()) {
                Element e = elements.get(i);
                int index = getIndex(e);
                if (index != -1) {
                    nodes[index].insert(e);  // Insere elementos na lista no sub-nó correspondente
                    elements.remove(i);  // Remove o elemento da lista do nó atual
                } else {
                    i++;
                }
            }
        }
    }

    public void retrieve(List<Element> result, Element element) {
        int index = getIndex(element);
        if (index != -1 && nodes[0] != null) {
            nodes[index].retrieve(result, element);  // Recupera elementos do sub-nó correspondente
        }

        result.addAll(elements);  // Adiciona os elementos do nó atual ao resultado

        for (Element e : elements) {
            if (e != element && !result.contains(e)) {
                result.add(e);  // Adiciona elementos do nó atual ao resultado se ainda não estiverem presentes
            }
        }
    }

    public void clear() {
        elements.clear();  // Limpa a lista de elementos do nó

        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] != null) {
                nodes[i].clear();  // Limpa os sub-nós recursivamente
                nodes[i] = null;  // Define o sub-nó como nulo
            }
        }
    }

    private void split() {
        int subWidth = bounds.getWidth() / 2;  // Largura do sub-nó
        int subHeight = bounds.getHeight() / 2;  // Altura do sub-nó
        int x = bounds.getX();  // Coordenada x do nó atual
        int y = bounds.getY();  // Coordenada y do nó atual

        // Cria os sub-nós com base nos limites e coordenadas calculados
        nodes[0] = new Quadtree(level + 1, new BoundingBox(x + subWidth, y, subWidth, subHeight));
        nodes[1] = new Quadtree(level + 1, new BoundingBox(x, y, subWidth, subHeight));
        nodes[2] = new Quadtree(level + 1, new BoundingBox(x, y + subHeight, subWidth, subHeight));
        nodes[3] = new Quadtree(level + 1, new BoundingBox(x + subWidth, y + subHeight, subWidth, subHeight));
    }

    private int getIndex(Element element) {
        int index = -1;
        double verticalMidpoint = bounds.getX() + bounds.getWidth() / 2;  // Ponto médio vertical dos limites do nó
        double horizontalMidpoint = bounds.getY() + bounds.getHeight() / 2;  // Ponto médio horizontal dos limites do nó

        boolean topQuadrant = element.getY() + element.getHeight() < horizontalMidpoint;  // Elemento está no quadrante superior?
        boolean bottomQuadrant = element.getY() > horizontalMidpoint;  // Elemento está no quadrante inferior?

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

        return index;  // Retorna o índice do sub-nó correspondente ao elemento
    }

    public BoundingBox getBounds() {
        return bounds;  // Retorna os limites do nó
    }

    public Quadtree[] getQuadtreeNodes() {
        return nodes;  // Retorna os sub-nós da árvore
    }
}
