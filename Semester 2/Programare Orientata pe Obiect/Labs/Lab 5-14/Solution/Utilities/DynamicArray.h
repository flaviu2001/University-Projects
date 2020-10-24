#ifndef DYNAMIC_ARRAY
#define DYNAMIC_ARRAY

template <typename TypeOfElement>
class DynamicArray{
private:
    TypeOfElement *array;
    int length, capacity;
public:

    DynamicArray(): length{0}, capacity{1}{
        this->array = new TypeOfElement[this->capacity];
    }

    ~DynamicArray(){
        delete[] this->array;
    }

    DynamicArray(const DynamicArray &copy): length{copy.length}, capacity{copy.capacity}{
        this->array = new TypeOfElement[this->capacity];
        for (int i = 0; i < this->length; ++i)
            this->array[i] = copy.array[i];
    }

    bool operator==(const DynamicArray &other){
        bool return_value = true;
        return_value &= this->length == other.length;
        for (int i = 0; i < this->length && return_value; ++i)
            return_value &= this->array[i] == other.array[i];
        return return_value;
    }

    DynamicArray& operator=(const DynamicArray &other){
        if (this == &other)
            return *this;
        this->length = other.length;
        this->capacity = other.capacity;
        delete[] this->array;
        this->array = new TypeOfElement[this->capacity];
        for (int i = 0; i < this->length; ++i)
            this->array[i] = other.array[i];
        return *this;
    }

    TypeOfElement& operator[](const int position){
        return this->array[position];
    }

    [[nodiscard]] int size() const{
        return this->length;
    }

    void add(const TypeOfElement &element){
        if (this->length == this->capacity){
            this->capacity *= 2;
            auto* new_array = new TypeOfElement[this->capacity];
            for (int i = 0; i < this->length; ++i)
                new_array[i] = this->array[i];
            delete[] this->array;
            this->array = new_array;
        }
        this->array[this->length] = element;
        ++this->length;
    }

    void remove(const TypeOfElement &element){
        int position = -1;
        for (int i = 0; i < this->length; ++i)
            if (this->array[i] == element){
                position = i;
                break;
            }
        if (position == -1)
            return;
        for (int i = position; i+1 < this->length; ++i)
            this->array[i] = this->array[i+1];
        --this->length;
    }

    class iterator{
    private:
        int position;
        DynamicArray<TypeOfElement> &parent;
    public:
        iterator(DynamicArray<TypeOfElement> &the_parent, int this_position): parent{the_parent}, position{this_position}{}

        iterator(const iterator &copy): position{copy.position}, parent{copy.parent}{}

        TypeOfElement& operator*()const{
            return this->parent.array[this->position];
        }

        iterator& operator++(){
            ++this->position;
            return *this;
        }

        bool operator!=(const iterator &other_iterator)const{
            return this->position != other_iterator.position;
        }

        bool operator==(const iterator &other_iterator)const{
            return this->position == other_iterator.position;
        }
    };

    iterator begin() {
        return iterator(*this, 0);
    }

    iterator end() {
        return iterator(*this, this->length);
    }
};

#endif