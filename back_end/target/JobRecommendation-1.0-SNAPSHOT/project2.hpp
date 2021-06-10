#ifndef __PROJECT_TWO_RBTREE_HPP
#define __PROJECT_TWO_RBTREE_HPP

#include "runtimeexcept.hpp"
#include <string>
#include <vector>

class ElementNotFoundException : public RuntimeException 
{
public:
	ElementNotFoundException(const std::string & err) : RuntimeException(err) {}
};


template<typename Key, typename Value>
class MyRBTree
{
private:
	// fill in private member data here
	// If you need to declare private functions, do so here too.
	num_of_element = 0;
	class Node{
		public:
			int color;
			<typename Key> key;
			<typename Value> value;
			Node parent;
			Node left_child;
			Node right_child;

			Node(c,K,V,p)
			{
				color = c;
				key = K;
				value = V;
				parent = p;
				left_child = nullptr;
				right_child = nullptr;
			}
	}
	Node * root;

	void delete_tree(Node head)
	{
		if (head == nullptr)
		{
			return;
		}
		delete_tree(head.left);
		delete_tree(head.right);
		delete head;
	}

	bool isParentRed(Node t)
	{
		return t.parent.color == 1;
	}

	bool isParentSiblingRed(Node t)
	{
		if (t.parent.parent.left_child == t.parent)
		{
			if (t.parent.parent.right_child == nullptr)
			{
				return false;
			}
			else if (t.parent.parent.right_child.color == 1){
				return true;
			}
			else{
				return false;
			}
		}
		else{
			if (t.parent.parent.left_child == nullptr)
			{
				return false;
			}
			else if (t.parent.parent.left_child.color == 1){
				return true;
			}
			else{
				return false;
			}
		}
		return false;
	}

public:
	MyRBTree();

	// In general, a copy constructor and assignment operator
	// are good things to have.
	// For this quarter, I am not requiring these. 


	// The destructor is, however, required. 
	~MyRBTree()
	{
		delete_tree(root);		
	}

	// size() returns the number of distinct keys in the tree.
	size_t size() const noexcept;

	// isEmpty() returns true if and only if the tree has no values in it. 
	bool isEmpty() const noexcept;

	// contains() returns true if and only if there
	//  is a (key, value) pair in the tree
	//	that has the given key as its key.
	bool contains(const Key & k) const; 

	// find returns the value associated with the given key
	// If !contains(k), this will throw an ElementNotFoundException
	// There needs to be a version for const and non-const trees.
	Value & find(const Key & k);
	const Value & find(const Key & k) const;

	// Inserts the given key-value pair into 
	// the tree and performs the Red-Black re-balance
	// operation, as described in lecture. 
	// If the key already exists in the tree, 
	// you may do as you please (no test cases in
	// the grading script will deal with this situation)
	void insert(const Key & k, const Value & v);

	// returns all keys in the tree that are between k1 and k2, inclusive.
	// You must return these *in order*.
	// Your running time should be O(log n + s), where s is the number of elements returned.
	std::vector<Key> reportAllInRange(const Key & k1, const Key & k2) const;


	// in general, a "remove" function would be here.
	// You would need it for line segment intersection,
	// but in Spring 2021, we aren't requiring it.
	
	// The following three functions all return
	// the set of keys in the tree as a standard vector.
	// Each returns them in a different order.
	std::vector<Key> inOrder() const;
	std::vector<Key> preOrder() const;
	std::vector<Key> postOrder() const;


};


// Many functions below are stubbed;  you should fix them.

template<typename Key, typename Value>
MyRBTree<Key,Value>::MyRBTree()
{
	this->root = new Node();
}

template<typename Key, typename Value>
size_t MyRBTree<Key, Value>::size() const noexcept
{
	return num_of_element;
}

template<typename Key, typename Value>
bool MyRBTree<Key, Value>::isEmpty() const noexcept
{
	return num_of_element == 0;
}


template<typename Key, typename Value>
bool MyRBTree<Key, Value>::contains(const Key &k) const
{
	if (root == nullptr)
	{
		return false;
	}
	Node current = root;
	while(current != nullptr)
	{
		if (k == current.key)
		{
			return true
		}
		else if (k > current.key)
		{
			current = current->right;
		}
		else(k < current.key)
		{
			current = current->left;
		}
	}
	return false;
}


// needs to throw an ElementNotFoundException if it isn't there. 
template<typename Key, typename Value>
Value & MyRBTree<Key, Value>::find(const Key & k)
{
	if (!contains(k))
	{
		throw ElementNotFoundException("Key not found!");
	}

	Node current = root;
	while (current != nullptr)
	{
		if (k == current.key)
		{
			return current.value;
		}
		else if (k < current.key)
		{
			current = current.left;
		}
		else{
			current = current.right;
		}
	}
	
	// NOTE:  do not catch this within this function!  
	//		If you need to throw, throw it.
}

template<typename Key, typename Value>
const Value & MyRBTree<Key, Value>::find(const Key & k) const
{
	if (!contains(k))
	{
		throw ElementNotFoundException("Key not found!");
	}

	Node current = root;
	while (current != nullptr)
	{
		if (k == current.key)
		{
			return current.value;
		}
		else if (k < current.key)
		{
			current = current.left;
		}
		else{
			current = current.right;
		}
	}
}

	// Inserts the given key-value pair into 
	// the tree and performs the Red-Black re-balance
	// operation, as described in lecture. 
	// If the key already exists in the tree, 
	// you may do as you please (no test cases in
	// the grading script will deal with this situation)
template<typename Key, typename Value>
void MyRBTree<Key, Value>::insert(const Key & k, const Value & v)
{
	if (num_of_element == 0)
	{
		root = Node(0,k,v,nullptr);
		num_of_element++;
		return;
	}

	Node current = root;
	while(current != nullptr)
	{
		if (current.key == k && current.value == v)
		{
			return;
		}
		else if (k < current.key && current.left == nullptr)
		{
			current.left = Node(1,k,v,current);
		}
		else if (k > current.key && current.right == nullptr)
		{
			current.right = Node(1,k,v,current);
		}
		else if (k < current.key)
		{
			current = current.left;
		}
		else if (k > current.key)
		{
			current = current.right;
		}
	}


}


// returns all keys in the tree that are between k1 and k2, inclusive.
	// You must return these *in order*.
	// Your running time should be O(log n + s), where s is the number of elements returned.
template<typename Key, typename Value>
std::vector<Key> MyRBTree<Key, Value>::reportAllInRange(const Key & k1, const Key & k2) const
{
	std::vector<Key> foo;
	return foo; 
}




template<typename Key, typename Value>
std::vector<Key> MyRBTree<Key, Value>::inOrder() const
{
	std::vector<Key> foo;
	return foo; 
}


template<typename Key, typename Value>
std::vector<Key> MyRBTree<Key, Value>::preOrder() const
{
	std::vector<Key> foo;
	return foo; 
}


template<typename Key, typename Value>
std::vector<Key> MyRBTree<Key, Value>::postOrder() const
{
	std::vector<Key> foo;
	return foo; 
}







#endif 