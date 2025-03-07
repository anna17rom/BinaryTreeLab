

/**
 * Klasa generyczna węzła drzewa
 * @param <T> Typ generyczny określa wartości węzła
 */
class NodeOfTree<T extends Comparable<T>> {
    /**
     * Wartosc wezła
     */
    T key;
    /**
     * Lewy syn
     */
    NodeOfTree<T> left;
    /**
     * Prawy syn
     */
    NodeOfTree<T> right;
    /**
     * Ojciec
     */
    NodeOfTree<T> father;

    /**
     * Konstruktor klasa węzła drzewa
     * @param key Wartość węzła
     */
    NodeOfTree(T key) {
        this.key = key;
    }

    /**
     * Metoda toSring zmienia typ wartości węzła na string
     * @return Wartość węzła typu string
     */
    public String toString() {
        return " " + key.toString();
    }

}

/**
 * Klasa drzewa binarnego
 * @param <T> Typ generyczny określa typ drzewa
 */
public class Tree<T extends Comparable<T>> {
    /**
     * Korzeń drzewa
     */
    NodeOfTree<T> root;

    /**
     * Metoda treeInsert wstawia wartości do drzewa
     * @param key Wartość do wstawiania
     */
    public void treeInsert(T key) {
        //Tworzymy nowy węzeł
        NodeOfTree<T> newNode = new NodeOfTree<>(key);

        NodeOfTree<T> xNode = root;
        NodeOfTree<T> yNode = null;
        //Szukamy ojca dla nowego węzła
        while (xNode != null) {
            yNode = xNode;
            if (key.compareTo(xNode.key) < 0)
                xNode = xNode.left;
            else
                xNode = xNode.right;
        }
        //Porównujemy nowy węzeł z ojcem i wstawiamy w odpowiednim miejscu
        newNode.father = yNode;
        if (yNode == null)
            root = newNode;
        else if (key.compareTo(yNode.key) < 0) {
            yNode.left = newNode;
        } else
            yNode.right = newNode;
    }

    /**
     * Metoda treeSearch szuka węzeł w drzewie
     * @param x Węzieł, od którego zaczynamy szukać
     * @param key Wartość węzeł, jakiej szukamy
     * @return Znaleziony węzeł
     */
    public NodeOfTree<T> treeSearch(NodeOfTree<T> x, T key) {
        NodeOfTree<T> searchNode = x;
        if (searchNode == null)
            return null;
        else if (key == searchNode.key)
            return searchNode;
        else if (key.compareTo(searchNode.key) < 0)
            return treeSearch(searchNode.left, key);
        else
            return treeSearch(searchNode.right, key);

    }

    /**
     * Metoda elementSearch podaje informacje, czy w drzewie zawarty podany element, czy nie
     * @param key Element, którego szukamy
     * @return Czy zawiera się podany element w drzewie
     */
    public boolean elementSearch(T key) {
        NodeOfTree<T> startNode = root;
        while (startNode.key != key) {
            if (key.compareTo(startNode.key) < 0) {
                startNode = startNode.left;
            } else {
                startNode = startNode.right;
            }
            if (startNode == null)
                return false;
        }
        return true;
    }


    /**
     * Metoda treeDelete usuwa podany elemnt
     * @param key Element, który usuwamy
     */
    public void treeDelete(T key) {
        //Czy w ogóle istnieje taki węzeł w drzewie
        NodeOfTree<T> deleteNode = treeSearch(root, key);
        //Węzeł, który zastąpi węzeł do usunięcia
        NodeOfTree<T> yNode;
        //Syny
        NodeOfTree<T> xNode;
        //Sprawdzamy, czy posiada jednego syna lub w ogóle nie posiada
        if (deleteNode.left == null || deleteNode.right == null)
            yNode = deleteNode;
        //Jeżeli posiada dwa syna, to szukamy następnika
        else
            yNode = treeSuccessor(deleteNode);
        //Wyliczamy wartość xNode,to lub lewy, lub prawy syn węzła
        if (yNode.left != null)
            //Bierzemy mniejszy
            xNode = yNode.left;
        else
            //Jeżeli nie ma mniejszego bierzemy większy
            xNode = yNode.right;
        //Jeżeli xNode istnieje, to wskaźnik na jego ojca to wskażnik na ojca yNode
        if (xNode != null)
            xNode.father = yNode.father;
        //Jeżeli yNode nie ma ojca, to xNode jest korzeniem
        if (yNode.father == null)
            root = xNode;
        //Jeżeli yNode to lewy syn
        else if (yNode == yNode.father.left) {
            yNode.father.left = xNode;
            //Jeżeli yNode to prawy syn
        } else
            yNode.father.right = xNode;
        if (yNode != deleteNode)
            deleteNode.key = yNode.key;

    }

    /**
     * Metoda treeDepth wylicza głębokosć drzewa
     * @param node Węzeł od którego liczymy głębokość
     * @return Ile poziomów zawiera drzewo
     */
    public int treeDepth(NodeOfTree node) {
        if (node == null)
            return 0;
        else {

            int LDepth = treeDepth(node.left);
            int RDepth = treeDepth(node.right);

            if (LDepth > RDepth)
                return LDepth + 1;
            else
                return RDepth + 1;
        }
    }

    /**
     * Metoda treeSuccessor szuka następnika podanego węzła
     * @param currNode Węzeł, następnik którego szukamy
     * @return Następnik węzła
     */
    public NodeOfTree<T> treeSuccessor(NodeOfTree<T> currNode) {
        //Sprawdzamy, czy ma prawego syna
        if (currNode.right != null)
            //Szukamy najmniejszego klucza, zaczynając od prawego syna danego węzła
            return treeMinimum(currNode.right);

        //Ojciec danego węzła
        NodeOfTree<T> fatherNode = currNode.father;
        //Szukamy najmniejszego klucza większego od klucza danego węzła (najniższy przodek węzła)
        while (fatherNode != null && currNode == fatherNode.right) {
            currNode = fatherNode;
            fatherNode = fatherNode.father;
        }

        return fatherNode;
    }

    /**
     * Metoda treeMinimum wylicza minimalne znaczenie w drzewie
     * @param mainNode Węzeł, od którego zaczynamy szukać
     * @return Minimalny element drzewa
     */
    public NodeOfTree<T> treeMinimum(NodeOfTree<T> mainNode) {
        while (mainNode.left != null) {
            mainNode = mainNode.left;
        }
        return mainNode;
    }

}
