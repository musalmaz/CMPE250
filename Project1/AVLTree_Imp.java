import java.util.ArrayList;

class AVLTree_Imp_Node {
    int key;
    ParkingLotClass lot_obj;
    int height;
    int leftChildIndex;  // Index in the ArrayList of the left child (-1 if null)
    int rightChildIndex; // Index in the ArrayList of the right child (-1 if null)

    public AVLTree_Imp_Node(int key, ParkingLotClass value) {
        this.key = key;
        this.lot_obj = value;
        this.height = 1;
        this.leftChildIndex = -1;
        this.rightChildIndex = -1;
    }
}
public class AVLTree_Imp
{
    private ArrayList<AVLTree_Imp_Node> nodes;
    private int rootIndex;

    public AVLTree_Imp() {
        nodes = new ArrayList<>();
        rootIndex = -1; // -1 indicates the tree is empty
    }

    public void insert(int key, ParkingLotClass ParkingLotClass_obj) {
        rootIndex = insert(rootIndex, key, ParkingLotClass_obj);
    }
    public void delete(int key) {
        rootIndex = delete(rootIndex, key);
    }

    public ParkingLotClass searchLot(int key) {
        int index = search(rootIndex, key);
        return index != -1 ? nodes.get(index).lot_obj : null;
    }

    public ParkingLotClass getNextLargerLot(int key) {
        int index = getNextLarger(rootIndex, key);
        return index != -1 ? nodes.get(index).lot_obj : null;
    }

    public ParkingLotClass findFirstAvailableSmallerLot(int key) {
        return findFirstAvailableSmallerLot(rootIndex, key);
    }

    private ParkingLotClass findFirstAvailableSmallerLot(int nodeIndex, int key) {
        if (nodeIndex == -1) {
            return null;
        }

        AVLTree_Imp_Node node = nodes.get(nodeIndex);

        if (node.key < key) {
            // Try right subtree first (larger keys but still less than 'key')
            ParkingLotClass result = findFirstAvailableSmallerLot(node.rightChildIndex, key);
            if (result != null) {
                return result;
            }

            // Check current node
            if (!node.lot_obj.isLotFull()) {
                return node.lot_obj;
            }

            // Then try left subtree
            return findFirstAvailableSmallerLot(node.leftChildIndex, key);
        } else {
            // Continue searching in the left subtree
            return findFirstAvailableSmallerLot(node.leftChildIndex, key);
        }
    }

    public void getNodesGreaterThan(int key, ArrayList<AVLTree_Imp_Node> result) {
        getNodesGreaterThan(rootIndex, key, result);
    }

    private void getNodesGreaterThan(int nodeIndex, int key, ArrayList<AVLTree_Imp_Node> result) {
        if (nodeIndex == -1) {
            return;
        }

        AVLTree_Imp_Node node = nodes.get(nodeIndex);

        if (node.key > key) {
            // Traverse left subtree
            getNodesGreaterThan(node.leftChildIndex, key, result);
            // Include current node
            result.add(node);
            // Traverse right subtree
            getNodesGreaterThan(node.rightChildIndex, key, result);
        } else {
            // Continue searching in the right subtree
            getNodesGreaterThan(node.rightChildIndex, key, result);
        }
    }

//    public ParkingLotClass getNextSmallerLot(int key) {
//        int index = getNextSmaller(rootIndex, key);
//        return index != -1 ? nodes.get(index).lot_obj : null;
//    }

    private int insert(int nodeIndex, int key, ParkingLotClass ParkingLotClass_obj) {
        if(nodeIndex == rootIndex)
        {
            nodes.add(new AVLTree_Imp_Node(key, ParkingLotClass_obj));
            return 0;
        }

        AVLTree_Imp_Node node = nodes.get(nodeIndex);

        if(key < node.key)
        {
            node.leftChildIndex = insert(nodeIndex, key, ParkingLotClass_obj);
        }
        else if(key > node.key)
        {
            node.rightChildIndex = insert(nodeIndex, key, ParkingLotClass_obj);
        }
        else
        {
            node.lot_obj = ParkingLotClass_obj;
            return nodeIndex;
        }

        updateHeigthAVLTree(node);
        return balanceAVLNode(nodeIndex);
    }

    private int delete(int nodeIndex, int key)
    {
        if (nodeIndex == -1) {
            return -1;
        }
        AVLTree_Imp_Node node = nodes.get(nodeIndex);

        if(key < node.key)
        {
            node.leftChildIndex = delete(node.leftChildIndex, key);
        }
        else if(key > node.key)
        {
            node.rightChildIndex = delete(node.rightChildIndex, key);
        }
        else
        {
            if (node.leftChildIndex == -1 || node.rightChildIndex == -1) {
                int tempIndex = node.leftChildIndex != -1 ? node.leftChildIndex : node.rightChildIndex;
                return tempIndex;
            } else {
                int minIndex = getMinValueNode(node.rightChildIndex);
                AVLTree_Imp_Node minNode = nodes.get(minIndex);
                node.key = minNode.key;
                node.lot_obj = minNode.lot_obj;
                node.rightChildIndex = delete(node.rightChildIndex, minNode.key);
            }
        }

        updateHeigthAVLTree(node);
        return balanceAVLNode(nodeIndex);
    }

//    private int search(int nodeIndex, int key) {
//        if (nodeIndex == rootIndex) {
//            return rootIndex;
//        }
//
//        AVLTree_Imp_Node node = nodes.get(nodeIndex);
//
//        if (key < node.key) {
//            return search(node.leftChildIndex, key);
//        } else if (key > node.key) {
//            return search(node.rightChildIndex, key);
//        } else {
//            return nodeIndex;
//        }
//    }
    private int search(int nodeIndex, int key) {
        while (nodeIndex != -1) {
            AVLTree_Imp_Node node = nodes.get(nodeIndex); 

            if (key == node.key) {
                return nodeIndex; // Key found
            } else if (key < node.key) {
                nodeIndex = node.leftChildIndex; // Move to left child
            } else {
                nodeIndex = node.rightChildIndex; // Move to right child
            }
        }
        return -1; // Key not found
    }

    private int getNextLarger(int nodeIndex, int key) {
        int currentIndex = nodeIndex;
        int tempIndex = -1;

        while (currentIndex != -1) {
            AVLTree_Imp_Node currentNode = nodes.get(currentIndex);
            if (key < currentNode.key) {
                tempIndex = currentIndex;
                currentIndex = currentNode.leftChildIndex;
            } else {
                currentIndex = currentNode.rightChildIndex;
            }
        }

        return tempIndex;
    }

    private int getNextSmaller(int nodeIndex, int key) {
        int currentIndex = nodeIndex;
        int tempIndex = -1;

        while (currentIndex != -1) {
            AVLTree_Imp_Node currentNode = nodes.get(currentIndex);
            if (key > currentNode.key) {
                tempIndex = currentIndex;
                currentIndex = currentNode.rightChildIndex;
            } else {
                currentIndex = currentNode.leftChildIndex;
            }
        }

        return tempIndex;
    }

    private void updateHeigthAVLTree(AVLTree_Imp_Node node) {
        int leftHeight = node.leftChildIndex != -1 ? nodes.get(node.leftChildIndex).height : 0;
        int rightHeight = node.rightChildIndex != -1 ? nodes.get(node.rightChildIndex).height : 0;
        node.height = 1 + Math.max(leftHeight, rightHeight);
    }

    private int getBalanceFactorNode(AVLTree_Imp_Node node) {
        int leftHeight = node.leftChildIndex != -1 ? nodes.get(node.leftChildIndex).height : 0;
        int rightHeight = node.rightChildIndex != -1 ? nodes.get(node.rightChildIndex).height : 0;
        return (leftHeight - rightHeight);
    }

    private int balanceAVLNode(int nodeIndex) {
        AVLTree_Imp_Node node = nodes.get(nodeIndex);
        int balanceFactor = getBalanceFactorNode(node);

        // Left heavy
        if (balanceFactor > 1) {
            if (getBalanceFactorNode(nodes.get(node.leftChildIndex)) >= 0) {
                // Left Left Case
                return rightRotate(nodeIndex);
            } else {
                // Left Right Case
                node.leftChildIndex = leftRotate(node.leftChildIndex);
                return rightRotate(nodeIndex);
            }
        }

        // Right heavy
        if (balanceFactor < -1) {
            if (getBalanceFactorNode(nodes.get(node.rightChildIndex)) <= 0) {
                // Right Right Case
                return leftRotate(nodeIndex);
            } else {
                // Right Left Case
                node.rightChildIndex = rightRotate(node.rightChildIndex);
                return leftRotate(nodeIndex);
            }
        }

        return nodeIndex;
    }

    private int rightRotate(int index) {
        AVLTree_Imp_Node y = nodes.get(index);
        int leftChildIndex = y.leftChildIndex;
        AVLTree_Imp_Node x = nodes.get(leftChildIndex);
        int rightChildIndex = x.rightChildIndex;

        // Rotation
        x.rightChildIndex = index;
        y.leftChildIndex = rightChildIndex;

        // Update heights
        updateHeigthAVLTree(y);
        updateHeigthAVLTree(x);

        return leftChildIndex;
    }

    private int leftRotate(int index) {
        AVLTree_Imp_Node x = nodes.get(index);
        int rightChildIndex = x.rightChildIndex;
        AVLTree_Imp_Node y = nodes.get(rightChildIndex);
        int leftChildIndex = y.leftChildIndex;

        // Rotation
        y.leftChildIndex = index;
        x.rightChildIndex = leftChildIndex;

        // Update heights
        updateHeigthAVLTree(x);
        updateHeigthAVLTree(y);

        return rightChildIndex;
    }

    private int getMinValueNode(int index) {
        int currentIndex = index;
        while (nodes.get(currentIndex).leftChildIndex != -1) {
            currentIndex = nodes.get(currentIndex).leftChildIndex;
        }
        return currentIndex;
    }

    public int sumTruckCountsAbove(int key) {
        return sumTruckCountsAbove(rootIndex, key);
    }

    private int sumTruckCountsAbove(int nodeIndex, int key) {
        if (nodeIndex == -1) {
            return 0;
        }

        AVLTree_Imp_Node node = nodes.get(nodeIndex);
        if (node.key > key) {
            // Sum counts from left subtree, current node, and right subtree
            int sum = 0;
            sum += sumTruckCountsAbove(node.leftChildIndex, key);
            sum += node.lot_obj.getTruckCount();
            sum += sumTruckCountsAbove(node.rightChildIndex, key);
            return sum;
        } else {
            // Continue searching in the right subtree
            return sumTruckCountsAbove(node.rightChildIndex, key);
        }
    }

}
