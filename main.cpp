#include <iostream>

using namespace std;

/**
 * Klasa generyczna drzewa
 * @param <T> Typ generyczny okresla typ drzewa
 */
template<typename T>
class Tree {
   /**
    * Struktura węzła drzewa
   */
public:
    struct Node_of_tree {
        /**
         * Wartość drzewa
        */
        T key;
        /**
         * Lewy syn
        */
        Node_of_tree *left;
        /**
         * Prawy syn
        */
        Node_of_tree *right;
        /**
         * Ojciec
        */
        Node_of_tree *father;

        Node_of_tree(T key) : key(key), left(NULL), right(NULL) {}
    };
/**
 * Korzeń drzewa
*/
    Node_of_tree *root = NULL;
/**
     * Metoda tree_insert wstawia wartości do drzewa
     * @param key Wartość do wstawiania
     */
public:
    bool tree_insert(const T &key) {
        Node_of_tree *newNode = new Node_of_tree(key);
        Node_of_tree *currNode = root;
        Node_of_tree *fatherNode = NULL;
        while (currNode != NULL) {
            fatherNode = currNode;
            if (key < currNode->key)
                currNode = currNode->left;
            else
                currNode = currNode->right;
        }
        newNode->father = fatherNode;
        if (fatherNode == NULL)
            root = newNode;
        else if (key < fatherNode->key) {
            fatherNode->left = newNode;
        } else
            fatherNode->right = newNode;
        return true;
    }

    /**
     * Metoda element_search podaje informacje, czy w drzewie zawarty podany element, czy nie
     * @param key Element, którego szukamy
     * @return Czy zawiera się podany element w drzewie
     */
public:
    bool element_search(T key) {
        Node_of_tree *focusNode = root;
        while (focusNode->key != key) {
            if (key < focusNode->key) {
                focusNode = focusNode->left;
            } else {
                focusNode = focusNode->right;
            }
            if (focusNode == NULL)
                return false;
        }
        return true;
    }
    /**
     * Metoda tree_in_order wypisuje elementy drzewa
     * @param currNode Węzeł od którego zaczynamy wypisywać
    */
public:
    void tree_in_order(Node_of_tree *currNode) {
        if (currNode != NULL) {
            tree_in_order(currNode->left);
            cout << currNode->key << " ";
            tree_in_order(currNode->right);
        }
    }
    /**
     * Metoda tree_minimum wylicza minimalne znaczenie w drzewie
     * @param mainNode Węzeł, od którego zaczynamy szukać
     * @return Minimalny element drzewa
     */
public:
    Node_of_tree *tree_minimum(Node_of_tree *mainNode) {
        while (mainNode->left != NULL) {
            mainNode = mainNode->left;
        }
        return mainNode;
    }
    /**
     * Metoda tree_successor szuka następnika podanego węzła
     * @param currNode Węzeł, następnik którego szukamy
     * @return Następnik węzła
     */
public:
    Node_of_tree *tree_successor(Node_of_tree *currNode) {
        if (currNode->right != NULL)
            return tree_minimum(currNode->right);
        Node_of_tree *fatherNode = currNode->father;
        while (fatherNode != NULL && currNode == fatherNode->right) {
            currNode = fatherNode;
            fatherNode = fatherNode->father;
        }

        return fatherNode;
    }
    /**
     * Metoda tree_delete usuwa podany elemnt
     * @param key Element, który usuwamy
     */
public:
    void tree_delete(T key) {
        Node_of_tree *deleteNode = tree_search(root, key);
        Node_of_tree *yNode;
        Node_of_tree *xNode;

        if (deleteNode->left == NULL || deleteNode->right == NULL)
            yNode = deleteNode;
        else
            yNode = tree_successor(deleteNode);
        if (yNode->left != NULL)
            xNode = yNode->left;
        else
            xNode = yNode->right;
        if (xNode != NULL)
            xNode->father = yNode->father;
        if (yNode->father == NULL)
            root = xNode;
        else if (yNode == yNode->father->left) {
            yNode->father->left = xNode;
        } else
            yNode->father->right = xNode;
        if (yNode != deleteNode)
            deleteNode->key = yNode->key;

    }
  /**
     * Metoda tree_search szuka węzeł w drzewie
     * @param x Węzieł, od którego zaczynamy szukać
     * @param key Wartość węzeł, jakiej szukamy
     * @return Znaleziony węzeł
     */
public:
    Node_of_tree *tree_search(Node_of_tree *x, T key) {
        Node_of_tree *searchNode = x;
        if (searchNode == NULL)
            return searchNode;
        else if (key == searchNode->key)
            return searchNode;
        else if (key < searchNode->key)
            return tree_search(searchNode->left, key);
        else
            return tree_search(searchNode->right, key);
    }

};
 /**
 * Metoda main to metoda główna
 * @param argc argumenty, które były przekazane do programu
 * @param argv
  */
int main(int argc, char *argv[]) {
    Tree<int> newTree_I;
    Tree<double> newTree_D;
    Tree<string> newTree_S;
    string type;
    int element_I;
    string element_S;
    double element_D;
    cout << "Entre tree type: " << endl;
    cin >> type;
    if (type.compare("integer") == 0) { newTree_I = *new Tree<int>; }
    else if (type.compare("double") == 0) { newTree_D = *new Tree<double>; }
    else if (type.compare("string") == 0) { newTree_S = *new Tree<string>; }
    else cout << "Wrong type" << endl;

    string command;
    while (true) {
        cout<<""<<endl;
        cout <<"Waiting for the command" << endl;
        cin >> command;
        if (command.compare("search") == 0) {
if (type.compare("integer") == 0) {
                if (newTree_I.root == NULL) {
                    cout << "Integer tree is empty" << endl;
                } else {
                    cout << "Enter the element you want to find: " << endl;
                    cin >> element_I;
                    cout << element_I << "-->" << newTree_I.element_search(element_I) << endl;
                }
            }
            if (type.compare("double") == 0) {
                if (newTree_D.root == NULL) {
                    cout << "Double tree is empty" << endl;
                } else {
                    cout << "Enter the element you want to find: " << endl;
                    cin >> element_D;
                    cout << element_D << "-->" << newTree_D.element_search(element_D) << endl;
                }
            }
            if (type.compare("string") == 0) {
                if (newTree_S.root == NULL) {
                    cout << "String tree is empty" << endl;
                } else {
                    cout << "Enter the element you want to find: " << endl;
                    cin >> element_S;
                    cout << element_D << "-->" << newTree_S.element_search(element_S) << endl;
                }
            }


        } else if (command.compare("delete") == 0) {
            if (type.compare("integer") == 0) {
                if (newTree_I.root == NULL) {
                    cout << "Integer tree is empty" << endl;
                } else {
                    cout << "Enter the element you want to delete: " << endl;
                    cin >> element_I;
                    newTree_I.tree_delete(element_I);
                }
            }
            if (type.compare("double") == 0) {
                if (newTree_D.root == NULL) {
                    cout << "Double tree is empty" << endl;
                } else {
                    cout << "Enter the element you want to delete: " << endl;
                    cin >> element_D;
                    newTree_D.tree_delete(element_D);
                }
            }
            if (type.compare("string") == 0) {
                if (newTree_S.root == NULL) {
                    cout << "String tree is empty" << endl;
                } else {
                    cout << "Enter the element you want to delete: " << endl;
                    cin >> element_S;
                    newTree_S.tree_delete(element_S);
                }
            }

        } else if (command.compare("insert") == 0) {
            cout << "Enter the element you want to insert: " << endl;
            if (type.compare("integer") == 0) {
                cin >> element_I;
                newTree_I.tree_insert(element_I);
            }
            if (type.compare("double") == 0) {
                cin >> element_D;
                newTree_D.tree_insert(element_D);
            }
            if (type.compare("string") == 0) {
                cin >> element_S;
                newTree_S.tree_insert(element_S);
            }

        } else if (command.compare("draw") == 0) {
            if (type.compare("integer") == 0) {
                if (newTree_I.root == NULL) {
                    cout << "Integer tree is empty" << endl;
                } else { newTree_I.tree_in_order(newTree_I.root); }
            }
            if (type.compare("double") == 0) {
                if (newTree_D.root == NULL) {
                    cout << "Double tree is empty" << endl;
                } else { newTree_D.tree_in_order(newTree_D.root); }
            }
            if (type.compare("string") == 0) {
                if (newTree_S.root == NULL) {
                    cout << "String tree is empty" << endl;
                } else { newTree_S.tree_in_order(newTree_S.root); }
            }
        } else if (command.compare("bye")==0) { break; }
        else cout << "Wrong command" << endl;
    }
}